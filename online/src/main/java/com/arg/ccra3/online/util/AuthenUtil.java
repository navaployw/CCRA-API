
package com.arg.ccra3.online.util;

import ch.qos.logback.classic.Logger;
import com.arg.ccra3.model.CdiToken;
import com.arg.ccra3.model.CtrlKey;
import com.arg.ccra3.model.MessageError;
import com.arg.ccra3.model.ResponseConfig;

import com.arg.ccra3.model.TrnJson;
import com.arg.ccra3.model.security.ViewApiUser;
import com.arg.ccra3.online.form.ReportAPIRequest;
import com.arg.ccra3.online.service.AesEcbEncryptDecrypt;
import com.arg.ccra3.online.service.CdiTokenService;
import com.arg.ccra3.online.service.CtrlKeyService;
import com.arg.ccra3.online.service.MessageErrorService;
import com.arg.ccra3.online.service.TrnJsonService;
import com.arg.ccra3.online.service.ViewApiUserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.json.JSONObject;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;

public class AuthenUtil {
    private final TrnJsonService trnJson;
    private final CdiTokenService cdiTokenService;
    private final MessageErrorService messageErrorService;
    private final ViewApiUserService userService;
    private final CtrlKeyService ctrlKey;
    
    private final Logger logger = (Logger) LoggerFactory.getLogger(AuthenUtil.class);
    
    public AuthenUtil(
        TrnJsonService trnJson, 
        CdiTokenService cdiTokenService,
        MessageErrorService messageErrorService,
        ViewApiUserService userService,
        CtrlKeyService ctrlKey){
        if(trnJson == null)
            logger.info("trnJson is null-------------------------------------------");
        if(cdiTokenService == null)
            logger.info("cdiTokenService is null-------------------------------------------");
        if(messageErrorService == null)
            logger.info("messageErrorService is null-------------------------------------------");
        if(userService == null)
            logger.info("userService is null-------------------------------------------");
        if(ctrlKey == null)
            logger.info("ctrlKey is null-------------------------------------------");
        
        this.trnJson = trnJson;
        this.cdiTokenService = cdiTokenService;
        this.messageErrorService = messageErrorService;
        this.userService = userService;
        this.ctrlKey = ctrlKey;
    }
    
    
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    
    public ResponseEntity<Object> doAfterAuthen(
            ReportAPIRequest requestJson, 
            RequestAttributes requestAttr, 
            Function<ReportAPIRequest, ResponseEntity<Object>> func){
        try{
            requestJson.setUserID(tryGettingUserID(requestJson, requestAttr));
            requestJson.setMatchTranId(18147l);//dummy fix me
            return func.apply(requestJson);
        }
        catch(IllegalArgumentException e){
            logger.error(e.getMessage());
            return handleError(e);
        }
    }
    private ResponseEntity<Object> handleError(IllegalArgumentException e){
        String code = e.getMessage();
        ResponseConfig res = new ResponseConfig();
        List<MessageError> message = messageErrorService.getMessageByCode(code);
        if(!message.isEmpty()){
            res.setError_code(message.get(0).getErrorCode());
            res.setError_message(message.get(0).getErrorMessage());
        }else{
            res.setError_code(code);
            switch(code){
                case "00040" -> res.setError_message("Access token expired.");
                case "00042" -> res.setError_message("CDI token expired.");
                case "00043" -> res.setError_message("Invalid CDI token signature.");
                case "00045" -> res.setError_message("The company ID of CDI token is not equal to the DUNS number of the request.");
                case "00046" -> res.setError_message("The AI code of CDI token is invalid.");
                case "00047" -> res.setError_message("The AI code of CCRA-API token is not equal to the AI code of the request.");
            }
        }
        return  ResponseEntity.status(401).body(res);
    }
    
    public String tryGettingUserID(ReportAPIRequest requestJson, RequestAttributes requestAttr) throws JSONException {
        logger.info("tryGettingUserID");
        String authorization = ((ServletRequestAttributes) requestAttr).getRequest().getHeader("Authorization");
        String accessToken = "";
        if (authorization != null && authorization.toLowerCase().startsWith("bearer")) 
            accessToken = authorization.split("Bearer ")[1];
        
        String aiJSON = requestJson.getAiCode();
        logger.info("aiJSON>>>" + aiJSON);
        String cdiToken = requestJson.getCdiToken();
   
        
        String CDITokenBody = deCodeCDIToken(cdiToken);
        JSONObject jsonObjCDI = new JSONObject(CDITokenBody);

        String CCRATokenBody = deCodeCCRAToken(accessToken);
        JSONObject jsonObjCCRA = new JSONObject(CCRATokenBody);
        
  
        logger.info("AI_CODE_CCRA>>>" + jsonObjCCRA.get("ai_code"));
        String userId = jsonObjCCRA.get("client_id").toString();
        logger.info("Username>>>"+userId);
   
        logger.info("AI_CODE_CDI>>>" + jsonObjCDI.get("ai_code"));

        logger.info("companyCDI>>>" + jsonObjCDI.get("company_id"));
        String jti = jsonObjCDI.get("jti").toString();
        logger.info("jti>>>" + jti);
        Date expCDI = new Date(Long.valueOf(jsonObjCDI.get("exp").toString()) * 1000);
        logger.info("expCDI>>>" + jsonObjCDI.get("exp"));
        logger.info("expCDI>>>" + expCDI);
        Date expCCRA = new Date(Long.valueOf(jsonObjCCRA.get("exp").toString()) * 1000);
        logger.info("expCCRA>>>" + jsonObjCCRA.get("exp"));
        logger.info("expCCRA>>>" + expCCRA);
        String logText = String.format("TrnJson:: %s",trnJson);
        logger.info(logText);
        
        try{
           
            List<ViewApiUser> userList = userService.getUserByAICode(aiJSON);
            if(userList.size()== 1){                
                AesEcbEncryptDecrypt encryptObject = new AesEcbEncryptDecrypt();
                AesEcbEncryptDecrypt.setKey(userList.get(0).getSecretKey());
                userId = encryptObject.decrypt(userId);
               
            }
          
        }catch(Exception e){
            logger.info("Exception::"+e);
        }
        
        return userId;
    }

    public String deCodeCCRAToken(String jwtToken) {
        Base64 base64Url = new Base64(true);
        logger.info("------------ Decode JWT ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        logger.info("~~~~~~~~~ JWT Header ~~~~~~~");

        String header = new String(base64Url.decode(base64EncodedHeader));
        logger.info("JWT Header : " + header);

        logger.info("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        logger.info("JWT Body : " + body);

        logger.info("JWT Signature : " + base64EncodedSignature);
        return body;
    }

    public String deCodeCDIToken(String cdiToken) {
        Base64 base64Url = new Base64(true);
        String jwtToken = cdiToken;
        logger.info("------------ Decode JWT cdiToken ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        logger.info("~~~~~~~~~ JWT Header cdiToken ~~~~~~~");

        String header = new String(base64Url.decode(base64EncodedHeader));
        logger.info("JWT Header : " + header);

        logger.info("~~~~~~~~~ JWT Body cdiToken~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        logger.info("JWT Body : " + body);

        logger.info("JWT Signature : " + base64EncodedSignature);
        return body;
    }

    public boolean verifyToken(String token) throws InvalidKeySpecException {
        try {
            List<CtrlKey> keyInfo = ctrlKey.getKey();
            String publicKey = keyInfo.get(0).getCtrlValue();
            logger.info("publicKey::"+publicKey);
            Algorithm algorithm = Algorithm.HMAC256(publicKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            logger.info("jwt>>>"+jwt);
            return true;
        } catch (Exception ex) {
           logger.error("Error:"+ex);
           return false;
        }
    }
    
   
}