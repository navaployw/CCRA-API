package com.arg.ccra3.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.arg.ccra3.models.CdiToken;
import com.arg.ccra3.models.CtrlKey;
import com.arg.ccra3.models.MessageError;
import com.arg.ccra3.models.ResponseModel;
import com.arg.ccra3.models.TrnJson;
import com.arg.ccra3.models.api.TokenAPI;
import com.arg.ccra3.models.security.ViewApiUser;
import com.arg.ccra3.online.form.ReportAPIRequest;
import com.arg.ccra3.online.service.AesEcbEncryptDecrypt;
import com.arg.ccra3.online.service.ApiCtrlService;
import com.arg.ccra3.online.service.BeforeGetReport;
import com.arg.ccra3.online.service.CdiTokenService;
import com.arg.ccra3.online.service.CtrlKeyService;
import com.arg.ccra3.online.service.MessageErrorService;
import com.arg.ccra3.online.service.ReportService;
import com.arg.ccra3.online.service.TrnJsonService;
import com.arg.ccra3.online.service.ViewApiUserService;
import com.arg.ccra3.online.util.ResponseEntityFactory;
import com.auth0.jwt.JWT;
import org.json.JSONObject;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.ArrayList;
import java.util.Map;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
public class CcraapiController {

    @Autowired
    private TrnJsonService trnJson;
    @Autowired
    private CdiTokenService cdiTokenService;
    @Autowired
    private MessageErrorService messageErrorService;
    @Autowired
    private ViewApiUserService userService;
    @Autowired
    private CtrlKeyService ctrlKey;
    @Autowired
    private ApiCtrlService apiCtrlService;
    @Autowired
    @Lazy
    private ReportService reportService;

    @Autowired
    @Lazy
    private BeforeGetReport beforeGetReport;
    private MessageError msgErr = new MessageError();
    private final Logger logger = (Logger) LoggerFactory.getLogger(CcraapiController.class);
    private String loggerText = "";
    private final String error_00051 = "00051";
    private final String error_00041 = "00041";

    @RequestMapping(value = "/get_ccra_report", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Object> getCCRAReport(Model model, @RequestBody ReportAPIRequest requestJson) throws JSONException {
        TrnJson trnJsonObjRequest = new TrnJson();
        TrnJson trnJsonObjResponse;
        ReportAPIRequest requestModelTemp = requestJson;
        HttpServletRequest requestHttp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        loggerText = String.format("IP:: %s", requestHttp.getRemoteAddr());
        logger.info(loggerText);
        Boolean validateBefore = false;
        logger.info(">>>>>getCCRAReport<<<");
        ResponseModel res = new ResponseModel();
        loggerText = String.format("requestModelTemp:: %s", requestModelTemp.toString());
        logger.info(loggerText);
        String aiCode = requestModelTemp.getAiCode();
        String cdiToken = requestModelTemp.getCdiToken();
        String companyID = requestModelTemp.getCompanyID();
        Long tokenCdiId = null;
        loggerText = String.format("AI_CODE_OUT : %s", aiCode);
        logger.info(loggerText);
        loggerText = String.format("CDI_TOKEN : %s", cdiToken);
        logger.info(loggerText);
        loggerText = String.format("COMPANY_ID : %s", companyID);
        logger.info(loggerText);
        String accessToken = "";
        String authorization = requestHttp.getHeader("Authorization");
        if (authorization != null && authorization.toLowerCase().startsWith("bearer")) {
            accessToken = authorization.split("Bearer ")[1];
            loggerText = String.format("accessToken:: %s", accessToken);
            logger.info(loggerText);
        }
        CdiToken cdiTokenObj = new CdiToken();
        List<TokenAPI> tokenApi = beforeGetReport.getTokenIdFromCCRAToken(accessToken);
        String CDITokenBody = deCodeCDIToken(cdiToken);
        logger.info(">>>CDITokenBody:113:" + CDITokenBody);
        Long tokenId = null;
        if (!CDITokenBody.equals("Error")) {
            JSONObject jsonObjCDI = new JSONObject(CDITokenBody);
            List<CdiToken> cdiTokenFound = new ArrayList<>();
            try {
                if (tokenApi.isEmpty()) 
                {
                    logger.info("##### TOKEN API IS Empty #####");

                    List<MessageError> message = messageErrorService.getMessageByCode("00041");
                    res.setCode(message.get(0).getErrorCode());
                    res.setMessage(message.get(0).getErrorMessage());
                    trnJsonObjRequest.setModuleNo(2L);
                    trnJsonObjRequest.setRequestTime(new Date());
                    trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                    trnJsonObjRequest.setAccessToken(accessToken);
                    trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                    trnJsonObjResponse.setResponseTime(new Date());
                    trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                    trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                    trnJson.saveJsonResponse(trnJsonObjResponse);

                    return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                } 
                else 
                {
                    logger.info("##### TOKEN API DOING... #####");
                    tokenId = Long.parseLong(tokenApi.get(0).getTokenId().toString());
                    logger.info("Get User by arcode "+ aiCode);
                    List<ViewApiUser> userList = userService.getUserByAICode(aiCode);

                    logger.info("Data size "+ userList.size());
                    if (userList.size() == 1) 
                    {
                        if (!apiCtrlService.allowCDITokenDuplicate()) 
                        {
                            List<CdiToken> cdiTokenList = cdiTokenService.checkCdiTokenUsed(cdiToken);
                            if (!cdiTokenList.isEmpty()) 
                            {
                                List<MessageError> message = messageErrorService
                                                                .getMessageByCode("00044");
                                res.setCode(message.get(0).getErrorCode());
                                res.setMessage(message.get(0).getErrorMessage());
                                loggerText = String.format("error:aiCode: %s", aiCode);
                                logger.info(loggerText);
                                trnJsonObjRequest.setModuleNo(2L);
                                trnJsonObjRequest.setRequestTime(new Date());
                                trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                                trnJsonObjRequest.setAccessToken(accessToken);
                                trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                                loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                                logger.info(loggerText);
                                trnJsonObjResponse.setResponseTime(new Date());
                                trnJsonObjResponse.setTokenId(tokenId);
                                trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                                trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                                trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                                trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                                trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                                trnJson.saveJsonResponse(trnJsonObjResponse);
                                return ResponseEntity
                                        .status(message.get(0)
                                        .getStatusCode()).body(res);
                            }
                        }
                        logger.info(">>>allowCDITokenDuplicate<<<");
                        String CCRATokenBody = deCodeCCRAToken(accessToken);
                        JSONObject jsonObjCCRA = new JSONObject(CCRATokenBody);
                        String aiCCRA = jsonObjCCRA.get("ai_code").toString();
                        loggerText = String.format("AI_CODE_CCRA>>> %s", jsonObjCCRA.get("ai_code"));
                        logger.info(loggerText);
                        String userId = jsonObjCCRA.get("client_id").toString();
                        loggerText = String.format("Username>>> %s", userId);
                        logger.info(loggerText);
                        String aiCDI = jsonObjCDI.get("ai_code").toString();
                        loggerText = String.format("AI_CODE_CDI>>> %s", jsonObjCDI.get("ai_code"));
                        logger.info(loggerText);
                        String companyCDI = jsonObjCDI.get("company_id").toString();
                        loggerText = String.format("companyCDI>>> %s", jsonObjCDI.get("company_id"));
                        logger.info(loggerText);
                        String jti = jsonObjCDI.get("jti").toString();
                        loggerText = String.format("jti>>> %s", jti);
                        logger.info(loggerText);
                        Date expCDI = new Date(Long.valueOf(jsonObjCDI.get("exp").toString()) * 1000);
                        loggerText = String.format("expCDI>>> %s", jsonObjCDI.get("exp"));
                        logger.info(loggerText);
                        loggerText = String.format("expCDI>>> %s", expCDI);
                        logger.info(loggerText);
                        Date expCCRA = new Date(Long.valueOf(jsonObjCCRA.get("exp").toString()) * 1000);
                        loggerText = String.format("expCCRA>>> %s", jsonObjCCRA.get("exp"));
                        logger.info(loggerText);
                        loggerText = String.format("expCCRA>>> %s", expCCRA);
                        logger.info(loggerText);

                        Boolean isValidSignature = false;
                        if (!expCDI.before(new Date())) {
                            isValidSignature = verifyCdiToken(cdiToken);

                        }//CDI Token expire
                        else if (expCDI.before(new Date())) {
                            List<MessageError> message = messageErrorService.getMessageByCode("00042");
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            loggerText = String.format("error:aiCode: %s", aiCode);
                            logger.info(loggerText);
                            trnJsonObjRequest.setModuleNo(2L);
                            trnJsonObjRequest.setRequestTime(new Date());
                            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                            trnJsonObjRequest.setAccessToken(accessToken);
                            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                            loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                            logger.info(loggerText);
                            trnJsonObjResponse.setResponseTime(new Date());
                            trnJsonObjResponse.setTokenId(tokenId);
                            trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                            trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                            trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                        }
                        String aiOut = aiCode;
                        String companyOut = companyID;
                        if (!aiCCRA.equals(aiOut)) {
                            List<MessageError> message = messageErrorService.getMessageByCode("00047");
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            trnJsonObjRequest.setModuleNo(2L);
                            trnJsonObjRequest.setRequestTime(new Date());
                            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                            trnJsonObjRequest.setAccessToken(accessToken);
                            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                            trnJsonObjResponse.setResponseTime(new Date());
                            trnJsonObjResponse.setTokenId(tokenId);
                            trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                            trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                            trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                        } else if (!aiCDI.equals(aiOut)) {
                            List<MessageError> message = messageErrorService.getMessageByCode("00046");
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            loggerText = String.format("error:aiCode: %s", aiCode);
                            logger.info(loggerText);
                            trnJsonObjRequest.setModuleNo(2L);
                            trnJsonObjRequest.setRequestTime(new Date());
                            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                            trnJsonObjRequest.setAccessToken(accessToken);
                            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                            loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                            logger.info(loggerText);
                            trnJsonObjResponse.setResponseTime(new Date());
                            trnJsonObjResponse.setTokenId(tokenId);
                            trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                            trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                            trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                        } else if (!companyCDI.equals(companyOut)) {
                            List<MessageError> message = messageErrorService.getMessageByCode("00045");
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            loggerText = String.format("error:aiCode: %s", aiCode);
                            logger.info(loggerText);
                            trnJsonObjRequest.setModuleNo(2L);
                            trnJsonObjRequest.setRequestTime(new Date());
                            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                            trnJsonObjRequest.setAccessToken(accessToken);
                            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                            loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                            logger.info(loggerText);
                            trnJsonObjResponse.setResponseTime(new Date());
                            trnJsonObjResponse.setTokenId(tokenId);
                            trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                            trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                            trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            return ResponseEntity.status(401).body(res);
                        } else if (expCCRA.before(new Date())) {
                            List<MessageError> message = messageErrorService.getMessageByCode("00040");
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            loggerText = String.format("error:aiCode: %s", aiCode);
                            logger.info(loggerText);
                            trnJsonObjRequest.setModuleNo(2L);
                            trnJsonObjRequest.setRequestTime(new Date());
                            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                            trnJsonObjRequest.setAccessToken(accessToken);
                            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                            loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                            logger.info(loggerText);
                            trnJsonObjResponse.setResponseTime(new Date());
                            trnJsonObjResponse.setTokenId(tokenId);
                            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                        } else if (!isValidSignature) {
                            List<MessageError> message = messageErrorService.getMessageByCode("00043");
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            trnJsonObjRequest.setModuleNo(2L);
                            trnJsonObjRequest.setRequestTime(new Date());
                            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                            trnJsonObjRequest.setAccessToken(accessToken);
                            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                            loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                            logger.info(loggerText);
                            trnJsonObjResponse.setResponseTime(new Date());
                            trnJsonObjResponse.setTokenId(tokenId);
                            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                        } else {
                            cdiTokenObj.setAiCode(aiCode);
                            cdiTokenObj.setCdiToken(cdiToken);
                            cdiTokenObj.setPayload(CDITokenBody);
                            cdiTokenObj.setJti(jti);
                            cdiTokenObj.setExpireTime(expCDI);
                            cdiTokenObj.setCreateTime(new Date());
                            cdiTokenObj.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                            cdiTokenObj.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                            cdiTokenObj.setTranTime(new Date());
                            cdiTokenService.saveCdiToken(cdiTokenObj);
                            cdiTokenFound = cdiTokenFound(cdiToken, tokenApi.get(0).getuID().toString());
                            if (!cdiTokenFound.isEmpty()) {
                                logger.info("tokencdiid: " + cdiTokenFound.get(0).getTokenCdiId());
                                tokenCdiId = cdiTokenFound.get(0).getTokenCdiId();
                                trnJsonObjRequest.setTokenCdiId(tokenCdiId);
                            }
                            try {
                                AesEcbEncryptDecrypt.setKey(userList.get(0).getSecretKey());
                                userId = new AesEcbEncryptDecrypt().decrypt(userId);
                                loggerText = String.format("userList: %s", userList.get(0));
                                logger.info(loggerText);

                                if (userId.equals(userList.get(0).getUserID())) {
                                    logger.info(">>userId.equals(userList.get(0).getUserID())<<");
                                    msgErr = beforeGetReport.process(userList.get(0), requestModelTemp, accessToken);
                                    loggerText = String.format("msgErr:status: %s", msgErr.getStatusCode());
                                    logger.info(loggerText);
                                    if (msgErr.getErrorCode() != null) {
                                        res.setCode(msgErr.getErrorCode());
                                        res.setMessage(msgErr.getErrorMessage());
                                        logger.info(">>>Validate before fail<<<");
                                        trnJsonObjRequest.setModuleNo(2L);
                                        trnJsonObjRequest.setAccessToken(accessToken);
                                        trnJsonObjRequest.setRequestTime(new Date());
                                        trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                                        trnJsonObjRequest.setAccessToken(accessToken);
                                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                                        loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                                        logger.info(loggerText);
                                        trnJsonObjResponse.setResponseTime(new Date());
                                        trnJsonObjResponse.setTokenId(tokenId);
                                        trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                                        trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                                        trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                                        trnJsonObjResponse.setErrorCode(msgErr.getErrorCode());
                                        trnJsonObjResponse.setStatusCode(Long.parseLong(msgErr.getStatusCode().toString()));
                                        trnJson.saveJsonResponse(trnJsonObjResponse);
                                        return ResponseEntity.status(msgErr.getStatusCode()).body(res);
                                    } else {
                                        res.setCode("200");
                                        res.setMessage("Return Report Successful.");
                                        trnJsonObjRequest.setModuleNo(2L);
                                        trnJsonObjRequest.setAccessToken(accessToken);
                                        trnJsonObjRequest.setRequestTime(new Date());
                                        trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                                        trnJsonObjRequest.setAccessToken(accessToken);
                                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                                        loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                                        logger.info(loggerText);
                                        trnJsonObjResponse.setResponseTime(new Date());
                                        trnJsonObjResponse.setTokenId(tokenId);
                                        trnJsonObjResponse.setStatusCode(200L);
                                        trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                                        trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                                        trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                                        requestModelTemp.setUserID(userId);
                                        requestModelTemp.setMatchTranId(Long.parseLong(msgErr.getTransactionId().toString()));
                                        trnJson.saveJsonResponse(trnJsonObjResponse);
                                    }
                                } else {
                                    logger.info(">>!userId.equals(userList.get(0).getUserID())<<");
                                    List<MessageError> message = messageErrorService.getMessageByCode("00041");
                                    res.setCode(message.get(0).getErrorCode());
                                    res.setMessage(message.get(0).getErrorMessage());
                                    trnJsonObjRequest.setModuleNo(2L);
                                    trnJsonObjRequest.setRequestTime(new Date());
                                    trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                                    trnJsonObjRequest.setAccessToken(accessToken);
                                    trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                                    trnJsonObjResponse.setResponseTime(new Date());
                                    trnJsonObjResponse.setTokenId(tokenId);
                                    trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                                    trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                                    trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                                    trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                                    trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                                    trnJson.saveJsonResponse(trnJsonObjResponse);
                                    return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                                }
                            } catch (NumberFormatException e) {
                                loggerText = String.format("Exception:last: %s", e);
                                logger.error(loggerText);
                                logger.error(e.getMessage(),e);
                                List<MessageError> message = messageErrorService.getMessageByCode(error_00051);
                                res = new ResponseModel();
                                res.setCode(message.get(0).getErrorCode());
                                res.setMessage(message.get(0).getErrorMessage());
                                trnJsonObjRequest.setModuleNo(2L);
                                trnJsonObjRequest.setRequestTime(new Date());
                                trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                                trnJsonObjRequest.setAccessToken(accessToken);
                                trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                                trnJsonObjResponse.setResponseTime(new Date());
                                trnJsonObjResponse.setTokenId(tokenId);
                                trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                                trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                                trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                                trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                                trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                                trnJson.saveJsonResponse(trnJsonObjResponse);
                                return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                            }
                        }
                    } else {
                        logger.info(">>>>User not found<<<<");
                        List<MessageError> message = messageErrorService.getMessageByCode("00041");
                        res.setCode(message.get(0).getErrorCode());
                        res.setMessage(message.get(0).getErrorMessage());
                        trnJsonObjRequest.setModuleNo(2L);
                        trnJsonObjRequest.setRequestTime(new Date());
                        trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                        trnJsonObjRequest.setAccessToken(accessToken);
                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                        trnJsonObjResponse.setResponseTime(new Date());
                        trnJsonObjResponse.setTokenId(tokenId);
                        trnJsonObjResponse.setaId(Long.parseLong(tokenApi.get(0).getaID().toString()));
                        trnJsonObjResponse.setuId(Long.parseLong(tokenApi.get(0).getuID().toString()));
                        trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                        trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                        trnJson.saveJsonResponse(trnJsonObjResponse);
                        return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
                    }
                }
                Map<String, Object> bundle = reportService.creatReport(requestModelTemp, trnJsonObjResponse);

                if (!cdiTokenFound.isEmpty() && bundle.get("expenseId") != null) {
                    tokenCdiId = cdiTokenFound.get(0).getTokenCdiId();
                    trnJsonObjResponse.setTokenCdiId(tokenCdiId);
                    trnJsonObjResponse.setExpenseId((Long) bundle.get("expenseId"));
                    trnJson.saveJsonResponse(trnJsonObjResponse);
                }
                return ResponseEntityFactory.ok(bundle.get("report"));

            } catch (Exception e) {
                loggerText = String.format("Error:all: %s", e);
                logger.error(loggerText);
                List<MessageError> message = messageErrorService.getMessageByCode(error_00051);
                res = new ResponseModel();
                res.setCode(message.get(0).getErrorCode());
                res.setMessage(message.get(0).getErrorMessage());
                trnJsonObjRequest.setModuleNo(2L);
                trnJsonObjRequest.setRequestTime(new Date());
                trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
                trnJsonObjRequest.setAccessToken(accessToken);
                trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                trnJsonObjResponse.setResponseTime(new Date());
                trnJsonObjResponse.setTokenId(tokenId);
                trnJsonObjResponse.setaId(Long.parseLong(tokenApi.get(0).getaID().toString()));
                trnJsonObjResponse.setuId(Long.parseLong(tokenApi.get(0).getuID().toString()));
                trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                trnJson.saveJsonResponse(trnJsonObjResponse);
                loggerText = String.format("res: %s", res.toString());
                logger.error(loggerText);
                return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
            }
        } else {
            logger.info(">>>Cdi token error<<<");
            logger.info("tokenApi::" + tokenApi.get(0));
            List<MessageError> message = messageErrorService.getMessageByCode("00043");
            res = new ResponseModel();
            res.setCode(message.get(0).getErrorCode());
            res.setMessage(message.get(0).getErrorMessage());
            trnJsonObjRequest.setModuleNo(2L);
            trnJsonObjRequest.setRequestTime(new Date());
            trnJsonObjRequest.setJsonRequest(requestModelTemp.toJsonStringReport());
            trnJsonObjRequest.setAccessToken(accessToken);
            trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
            trnJsonObjResponse.setResponseTime(new Date());
            trnJsonObjResponse.setTokenId(Long.parseLong(tokenApi.get(0).getTokenId().toString()));
            trnJsonObjResponse.setaId(Long.parseLong(tokenApi.get(0).getaID().toString()));
            trnJsonObjResponse.setuId(Long.parseLong(tokenApi.get(0).getuID().toString()));
            trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
            trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
            trnJson.saveJsonResponse(trnJsonObjResponse);
            loggerText = String.format("res: %s", res.toString());
            logger.error(loggerText);
            return ResponseEntity.status(message.get(0).getStatusCode()).body(res);
        }
    }

    @RequestMapping(value = "/get_ccra_report/html", method = {RequestMethod.POST}, produces = "application/zip")
    public void getCCRAReportHtml(HttpServletResponse httpResponse, @RequestBody ReportAPIRequest requestJson) throws JSONException, Exception {
        TrnJson trnJsonObjRequest = new TrnJson();
        TrnJson trnJsonObjResponse = new TrnJson();
        String ccraToken = "";
        try {
            HttpServletRequest requestHttp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            loggerText = String.format("IP:: %s", requestHttp.getRemoteAddr());
            logger.info(loggerText);
            logger.info(">>>>>getCCRAReport<<<");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            ResponseModel res = new ResponseModel();
            loggerText = String.format("requestJson:: %s", requestJson.toString());
            logger.info(loggerText);
            if (requestJson.getCdiToken() == null) {
                List<MessageError> message = messageErrorService.getMessageByCode(error_00051);
                res.setCode(message.get(0).getErrorCode());
                res.setMessage(message.get(0).getErrorMessage());
                httpResponse.setStatus(message.get(0).getStatusCode());
                saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                reportService.zipErrorResponseNclean(httpResponse, res);
            } else {
                String cdiToken = requestJson.getCdiToken();
                loggerText = String.format("CDI_TOKEN : %s", cdiToken);
                logger.info(loggerText);
                String authorization = request.getHeader("Authorization");
                loggerText = String.format("authorization:: %s", authorization);
                logger.info(loggerText);

                if (authorization != null && authorization.toLowerCase().startsWith("bearer")) {
                    ccraToken = authorization.split("Bearer ")[1];
                    loggerText = String.format("ccraToken:: %s", ccraToken);
                    logger.info(loggerText);
                }
                List<TokenAPI> tokenApi = beforeGetReport.getTokenIdFromCCRAToken(ccraToken);
                if (tokenApi.isEmpty()) {
                    logger.info(">>>tokenApi.isEmpty()<<<");
                    List<MessageError> message = messageErrorService.getMessageByCode("00041");
                    res.setCode(message.get(0).getErrorCode());
                    res.setMessage(message.get(0).getErrorMessage());
                    httpResponse.setStatus(message.get(0).getStatusCode());
                    saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                    reportService.zipErrorResponseNclean(httpResponse, res);
                } else {
                    logger.info(">>>!tokenApi.isEmpty()<<<");
                    List<CdiToken> cdiTokenList = cdiTokenFound(cdiToken, tokenApi.get(0).getuID().toString());
                    loggerText = String.format("cdiTokenList>>> %s", cdiTokenList);
                    logger.info(loggerText);
                    if (!cdiTokenList.isEmpty()) {
                        Date cdiExpiryTime = cdiTokenList.get(0).getExpireTime();
                        String CDITokenBody = deCodeCDIToken(cdiToken);
                        if (!CDITokenBody.equals("Error")) {
                            JSONObject jsonObjCDI = new JSONObject(CDITokenBody);
                            Long tokenId = Long.parseLong(tokenApi.get(0).getTokenId().toString());
                            Date expiryTime = tokenApi.get(0).getExpireTime();
                            String CCRATokenBody = deCodeCCRAToken(ccraToken);
                            JSONObject jsonObjCCRA = new JSONObject(CCRATokenBody);
                            String aiCCRA = jsonObjCCRA.get("ai_code").toString();
                            loggerText = String.format("AI_CODE_CCRA>>> %s", aiCCRA);
                            logger.info(loggerText);
                            String userId = jsonObjCCRA.get("client_id").toString();
                            loggerText = String.format("Username>>> %s", userId);
                            logger.info(loggerText);
                            String aiCDI = jsonObjCDI.get("ai_code").toString();
                            loggerText = String.format("AI_CODE_CDI>>> %s", aiCDI);
                            logger.info(loggerText);
                            String companyCDI = jsonObjCDI.get("company_id").toString();
                            loggerText = String.format("companyCDI>>> %s", companyCDI);
                            logger.info(loggerText);
                            String jti = jsonObjCDI.get("jti").toString();
                            loggerText = String.format("jti>>> %s", jti);
                            logger.info(loggerText);
                            Date expCDI = new Date(Long.valueOf(jsonObjCDI.get("exp").toString()) * 1000);
                            loggerText = String.format("expCDI>>> %s", jsonObjCDI.get("exp"));
                            logger.info(loggerText);
                            loggerText = String.format("expCDI>>> %s", expCDI);
                            logger.info(loggerText);
                            Date expCCRA = new Date(Long.valueOf(jsonObjCCRA.get("exp").toString()) * 1000);
                            loggerText = String.format("expiryTime>>> %s", expiryTime);
                            logger.info(loggerText);
                            loggerText = String.format("expCCRA>>> %s", expCCRA);
                            logger.info(loggerText);
                            if (cdiExpiryTime.before(new Date())) {
                                List<MessageError> message = messageErrorService.getMessageByCode("00042");
                                res.setCode(message.get(0).getErrorCode());
                                res.setMessage(message.get(0).getErrorMessage());
                                httpResponse.setStatus(message.get(0).getStatusCode());
                                saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                                reportService.zipErrorResponseNclean(httpResponse, res);
                            }//CCRA ACCESS Token expire
                            else if (expiryTime.before(new Date())) {
                                List<MessageError> message = messageErrorService.getMessageByCode("00040");
                                res.setCode(message.get(0).getErrorCode());
                                res.setMessage(message.get(0).getErrorMessage());
                                httpResponse.setStatus(message.get(0).getStatusCode());
                                saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                                reportService.zipErrorResponseNclean(httpResponse, res);
                            } else {
                                try {
                                    List<TrnJson> trnJsonObj = beforeGetReport.getDataFromTokenCDIIDandUIDandModule(cdiTokenList.get(0).getTokenCdiId(), tokenApi.get(0).getuID(), "2");
                                    if (!trnJsonObj.isEmpty()) {
                                        if (trnJsonObj.get(0).getExpenseId() == null) {
                                            logger.info("trnJsonObj.get(0).getExpenseId()>>>" + trnJsonObj.get(0).getExpenseId());
                                            List<MessageError> message = messageErrorService.getMessageByCode(trnJsonObj.get(0).getErrorCode());
                                            res.setCode(message.get(0).getErrorCode());
                                            res.setMessage(message.get(0).getErrorMessage());
                                            httpResponse.setStatus(message.get(0).getStatusCode());
                                            saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                                            reportService.zipErrorResponseNclean(httpResponse, res);
                                        } else {
                                            Long expenseId = trnJsonObj.get(0).getExpenseId();
                                            loggerText = String.format("ExpenseId: %s", expenseId);
                                            logger.info(loggerText);
                                            List<ViewApiUser> userList = userService.getUserByAICode(aiCDI);
                                            msgErr = beforeGetReport.processHtml(cdiTokenList.get(0).getTokenCdiId(), userId, tokenApi);
                                            loggerText = String.format("msgErr:%s", msgErr.toString());
                                            logger.info(loggerText);
                                            if (msgErr.getErrorCode() != null) {
                                                logger.info("msgErr.getErrorCode()::" + msgErr.getErrorCode());
                                                logger.info("msgErr.getErrorMessage()::" + msgErr.getErrorMessage());
                                                res.setCode(msgErr.getErrorCode());
                                                res.setMessage(msgErr.getErrorMessage());
                                                httpResponse.setStatus(msgErr.getStatusCode());
                                                List<MessageError> message = messageErrorService.getMessageByCode(msgErr.getErrorCode());
                                                saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                                                logger.info(">>> create report html zip n clean 1 <<<<");
                                                reportService.zipErrorResponseNclean(httpResponse, res);
                                            } else {
                                                res.setCode("200");
                                                res.setMessage("Return Report Successful.");
                                                requestJson.setUserID(userList.get(0).getUserID());
                                                requestJson.setCompanyID(companyCDI);
                                                trnJsonObjRequest.setModuleNo(3L);
                                                trnJsonObjRequest.setAccessToken(ccraToken);
                                                trnJsonObjRequest.setRequestTime(new Date());
                                                trnJsonObjRequest.setJsonRequest(requestJson.toJsonStringReport());
                                                trnJsonObjRequest.setExpenseId(expenseId);
                                                trnJsonObjRequest.setTokenCdiId(cdiTokenList.get(0).getTokenCdiId());
                                                trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                                                trnJsonObjResponse.setResponseTime(new Date());
                                                trnJsonObjResponse.setTokenId(tokenId);
                                                trnJsonObjResponse.setStatusCode(200L);
                                                trnJsonObjResponse.setaId(Long.parseLong(userList.get(0).getaID().toString()));
                                                trnJsonObjResponse.setuId(Long.parseLong(userList.get(0).getuID().toString()));
                                                trnJsonObjResponse.setGroupId(Long.parseLong(userList.get(0).getGroupID()));
                                                trnJson.saveJsonResponse(trnJsonObjResponse);

                                                logger.info(">>> genererate report html zip n clean 2 <<<<");
                                                reportService.generateReportHtml(httpResponse, requestJson, expenseId);
                                                logger.info(">>> genererate report html zip n clean end 2 <<<<");

                                            }
                                        }
                                    } else {
                                        List<MessageError> message = messageErrorService.getMessageByCode("00048");
                                        res.setCode(message.get(0).getErrorCode());
                                        res.setMessage(message.get(0).getErrorMessage());
                                        httpResponse.setStatus(message.get(0).getStatusCode());
                                        saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                                        reportService.zipErrorResponseNclean(httpResponse, res);
                                    }
                                } catch (Exception e) {
                                    loggerText = String.format("Exception:last: %s", e);
                                    logger.error(loggerText);
                                    List<MessageError> message = messageErrorService.getMessageByCode(error_00051);
                                    res = new ResponseModel();
                                    res.setCode(message.get(0).getErrorCode());
                                    res.setMessage(message.get(0).getErrorMessage());
                                    httpResponse.setStatus(message.get(0).getStatusCode());
                                    saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                                    trnJson.saveJsonResponse(trnJsonObjResponse);

                                    reportService.zipErrorResponseNclean(httpResponse, res);
                                }
                            }
                        } else {
                            logger.info(">>>Cdi token error<<<");
                            logger.error(loggerText);
                            List<MessageError> message = messageErrorService.getMessageByCode("00043");
                            res = new ResponseModel();
                            res.setCode(message.get(0).getErrorCode());
                            res.setMessage(message.get(0).getErrorMessage());
                            httpResponse.setStatus(message.get(0).getStatusCode());
                            saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                            trnJson.saveJsonResponse(trnJsonObjResponse);
                            reportService.zipErrorResponseNclean(httpResponse, res);
                        }
                    } else if (cdiTokenList.isEmpty()) {
                        List<MessageError> message = messageErrorService.getMessageByCode("00048");
                        res.setCode(message.get(0).getErrorCode());
                        res.setMessage(message.get(0).getErrorMessage());
                        httpResponse.setStatus(message.get(0).getStatusCode());
                        saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
                        reportService.zipErrorResponseNclean(httpResponse, res);
                    }
                }
            }
        } catch (Exception e) {
            loggerText = String.format("Error:all: %s", e);
            logger.error(loggerText);
            List<MessageError> message = messageErrorService.getMessageByCode(error_00041);
            ResponseModel res = new ResponseModel();
            res.setCode(message.get(0).getErrorCode());
            res.setMessage(message.get(0).getErrorMessage());
            httpResponse.setStatus(message.get(0).getStatusCode());
            saveTrnJsonErrorHtmlLog(trnJsonObjRequest, trnJsonObjResponse, message, ccraToken, requestJson, 3L);
            reportService.zipErrorResponseNclean(httpResponse, res);
        }
    }

    public void saveTrnJsonErrorHtmlLog(TrnJson trnJsonObjRequest, TrnJson trnJsonObjResponse, List<MessageError> message, String ccraToken, ReportAPIRequest requestJson, Long moduleNo) {
        trnJsonObjRequest.setModuleNo(moduleNo);
        trnJsonObjRequest.setRequestTime(new Date());
        trnJsonObjRequest.setJsonRequest(requestJson.toJsonStringReport());
        trnJsonObjRequest.setAccessToken(ccraToken);
        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
        trnJsonObjResponse.setResponseTime(new Date());
        trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
        trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
        trnJson.saveJsonResponse(trnJsonObjResponse);
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
        loggerText = String.format("JWT Header : %s", header);
        logger.info(loggerText);

        logger.info("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        loggerText = String.format("JWT Body : %s", body);
        logger.info(loggerText);
        loggerText = String.format("JWT Signature : %s", base64EncodedSignature);
        logger.info(loggerText);
        return body;
    }

    public String deCodeCDIToken(String cdiToken) {
        try {
            Base64 base64Url = new Base64(true);
            String jwtToken = cdiToken;
            logger.info("------------ Decode JWT cdiToken ------------");
            String[] split_string = jwtToken.split("\\.");
            String base64EncodedHeader = split_string[0];
            String base64EncodedBody = split_string[1];
            String base64EncodedSignature = split_string[2];

            logger.info("~~~~~~~~~ JWT Header cdiToken ~~~~~~~");

            String header = new String(base64Url.decode(base64EncodedHeader));
            loggerText = String.format("JWT Header : %s", header);
            logger.info(loggerText);

            logger.info("~~~~~~~~~ JWT Body cdiToken~~~~~~~");
            String body = new String(base64Url.decode(base64EncodedBody));
            loggerText = String.format("JWT Body : %s", body);
            logger.info(loggerText);
            loggerText = String.format("JWT Signature : %s", base64EncodedSignature);
            logger.info(loggerText);
            return body;
        } catch (Exception e) {
            loggerText = String.format("Error:deCodeCDIToken: %s", e);
            logger.error(loggerText);
            return "Error";
        }
    }

   public Boolean verifyToken(String token) throws InvalidKeySpecException {

        try {

//            String publicKey = "ZsKDkxQUGNFebdG6645Vj6bZvOOt6Heh";
            List<CtrlKey> keyInfo = ctrlKey.getKey();
            loggerText = String.format("keyInfo:: %s", keyInfo);
            logger.info(loggerText);
            String publicKey = keyInfo.get(0).getCtrlValue();
            loggerText = String.format("publicKey:: %s", publicKey);
            logger.info(loggerText);
            Algorithm algorithm = Algorithm.HMAC256(publicKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException | IllegalArgumentException ex) {
            loggerText = String.format("Error: %s", ex);
            logger.error(loggerText);
            return false;
        }

    }

    public List<CdiToken> cdiTokenFound(String cdiToken, String userId) {
        List<CdiToken> cdiTokenFound = cdiTokenService.findCdiToken(cdiToken, Long.parseLong(userId));
        return cdiTokenFound;
    }

    private Boolean verifyCdiToken(String cdiToken) {
        Boolean isValidSignature = false;
        try {
            isValidSignature = verifyToken(cdiToken);
        } catch (InvalidKeySpecException ex) {
            loggerText = String.format("Exception:InvalidKeySpecException: %s", ex);
            logger.error(loggerText);
        }
        loggerText = String.format("isValidSignature>>> %s", isValidSignature);
        logger.info(loggerText);
        return isValidSignature;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
