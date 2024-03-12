/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.online.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ch.qos.logback.classic.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.arg.ccra3.models.MessageError;
import com.arg.ccra3.models.ResponseConfig;
import com.arg.ccra3.models.ResponseModel;
import com.arg.ccra3.models.TrnJson;
import com.arg.ccra3.online.service.MessageErrorService;
import com.arg.ccra3.online.service.ReportService;
import com.arg.ccra3.online.service.TrnJsonService;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Component("ResourceExceptionEntryPoint")
public class ResourceExceptionEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    TrnJsonService trnJson;
    @Autowired
    private MessageErrorService messageErrorService;
    @Autowired
    @Lazy
    private ReportService reportService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private String loggerText = "";
    String userId;
    String password;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException, ServletException {
        logger.info(">>>Resource authen error<<<<");
        logger.info(request.getContextPath());
        logger.info(request.getRequestURI());
        ResponseModel res = new ResponseModel();
        final ObjectMapper mapper = new ObjectMapper();
        TrnJson trnJsonObjRequest = new TrnJson();
        TrnJson trnJsonObjResponse;
        String CCRATokenBody = deCodeCCRAToken();
        String authorization = request.getHeader("Authorization");
        String ccraToken = "";
        if (authorization.toLowerCase().startsWith("bearer")) {
            ccraToken = authorization.split("Bearer ")[1];
        }
        if (request.getRequestURI().equals("/ccraapiauth/get_ccra_report/html")) {
            try {
                
                String body = "";
                if (!CCRATokenBody.equals("")) {
                    JSONObject jsonObjCCRA = new JSONObject(CCRATokenBody);
                    Date expCCRA = new Date(Long.valueOf(jsonObjCCRA.get("exp").toString()) * 1000);
                    loggerText = String.format("expCCRA>>> %s", expCCRA);
                    logger.info(loggerText);
                    if (expCCRA.before(new Date())) {
                        List<MessageError> message = messageErrorService.getMessageByCode("00040");
                        res.setCode(message.get(0).getErrorCode());
                        res.setMessage(message.get(0).getErrorMessage());
                        response.setStatus(message.get(0).getStatusCode());
                        trnJsonObjRequest.setModuleNo(3L);
                        trnJsonObjRequest.setRequestTime(new Date());
                        trnJsonObjRequest.setJsonRequest(body);
                        trnJsonObjRequest.setAccessToken(ccraToken);
                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                        loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                        logger.info(loggerText);
                        trnJsonObjResponse.setResponseTime(new Date());
                        trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                        trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));

                        String jsonBody = mapper.writeValueAsString(res);
                        trnJsonObjResponse.setJsonResponse(jsonBody);
                        trnJson.saveJsonResponse(trnJsonObjResponse);

                        reportService.zipErrorResponseNclean(response, res);
                    } else {
                        List<MessageError> message = messageErrorService.getMessageByCode("00041");
                        res.setCode(message.get(0).getErrorCode());
                        res.setMessage(message.get(0).getErrorMessage());
                        response.setStatus(message.get(0).getStatusCode());
                        trnJsonObjRequest.setModuleNo(3L);
                        trnJsonObjRequest.setRequestTime(new Date());
                        trnJsonObjRequest.setJsonRequest(body);
                        trnJsonObjRequest.setAccessToken(ccraToken);
                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                        loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                        logger.info(loggerText);
                        trnJsonObjResponse.setResponseTime(new Date());
                        trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                        trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));

                        String jsonBody = mapper.writeValueAsString(res);
                        trnJsonObjResponse.setJsonResponse(jsonBody);
                        trnJson.saveJsonResponse(trnJsonObjResponse);

                        reportService.zipErrorResponseNclean(response, res);
                    }
                } else {
                    List<MessageError> message = messageErrorService.getMessageByCode("00041");
                    res.setCode(message.get(0).getErrorCode());
                    res.setMessage(message.get(0).getErrorMessage());
                    response.setStatus(message.get(0).getStatusCode());
                    trnJsonObjRequest.setModuleNo(3L);
                    trnJsonObjRequest.setRequestTime(new Date());
                    trnJsonObjRequest.setJsonRequest(body);
                    trnJsonObjRequest.setAccessToken(ccraToken);
                    trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                    trnJsonObjResponse.setResponseTime(new Date());
                    trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                    trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));

                    String jsonBody = mapper.writeValueAsString(res);
                    trnJsonObjResponse.setJsonResponse(jsonBody);
                    trnJson.saveJsonResponse(trnJsonObjResponse);

                    reportService.zipErrorResponseNclean(response, res);
                }

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String body = request.getReader().lines().collect(Collectors.joining());
            try {
                if (!CCRATokenBody.equals("")) {
                    JSONObject jsonObjCCRA = new JSONObject(CCRATokenBody);
                    Date expCCRA = new Date(Long.valueOf(jsonObjCCRA.get("exp").toString()) * 1000);
                    loggerText = String.format("expCCRA>>> %s", expCCRA);
                    logger.info(loggerText);
                    if (expCCRA.before(new Date())) {
                        List<MessageError> message = messageErrorService.getMessageByCode("00040");
                        ResponseConfig resConfig = new ResponseConfig();
                        resConfig.setError_code(message.get(0).getErrorCode());
                        resConfig.setError_message(message.get(0).getErrorMessage());
                        final Map<String, Object> mapBodyException = new LinkedHashMap<>();
                        mapBodyException.put("error_code", message.get(0).getErrorCode());
                        mapBodyException.put("error_message", message.get(0).getErrorMessage());
                        response.setContentType("application/json");
                        response.setStatus(message.get(0).getStatusCode());
                        trnJsonObjRequest.setModuleNo(2L);
                        trnJsonObjRequest.setRequestTime(new Date());
                        trnJsonObjRequest.setJsonRequest(body);
                        trnJsonObjRequest.setAccessToken(ccraToken);
                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                        loggerText = String.format("trnJsonObjResponse:: %s", trnJsonObjResponse);
                        logger.info(loggerText);
                        trnJsonObjResponse.setResponseTime(new Date());
                        trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                        trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));
                        trnJsonObjResponse.setJsonResponse(new ObjectMapper().writeValueAsString(mapBodyException));

                        String jsonBody = mapper.writeValueAsString(mapBodyException);
                        trnJsonObjResponse.setJsonResponse(jsonBody);
                        trnJson.saveJsonResponse(trnJsonObjResponse);

                        response.getWriter().write(jsonBody);
                    } else {
                        List<MessageError> message = messageErrorService.getMessageByCode("00041");

                        ResponseConfig resConfig = new ResponseConfig();
                        resConfig.setError_code(message.get(0).getErrorCode());
                        resConfig.setError_message(message.get(0).getErrorMessage());
                        final Map<String, Object> mapBodyException = new LinkedHashMap<>();
                        mapBodyException.put("error_code", message.get(0).getErrorCode());
                        mapBodyException.put("error_message", message.get(0).getErrorMessage());
                        response.setContentType("application/json");
                        response.setStatus(message.get(0).getStatusCode());
                        trnJsonObjRequest.setModuleNo(2L);
                        trnJsonObjRequest.setRequestTime(new Date());
                        trnJsonObjRequest.setJsonRequest(body);
                        trnJsonObjRequest.setAccessToken(ccraToken);
                        trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                        trnJsonObjResponse.setResponseTime(new Date());
                        trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                        trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));

                        String jsonBody = mapper.writeValueAsString(mapBodyException);
                        trnJsonObjResponse.setJsonResponse(jsonBody);
                        trnJson.saveJsonResponse(trnJsonObjResponse);

                        response.getWriter().write(jsonBody);
                    }
                } else {
                    List<MessageError> message = messageErrorService.getMessageByCode("00041");

                    ResponseConfig resConfig = new ResponseConfig();
                    resConfig.setError_code(message.get(0).getErrorCode());
                    resConfig.setError_message(message.get(0).getErrorMessage());
                    final Map<String, Object> mapBodyException = new LinkedHashMap<>();
                    mapBodyException.put("error_code", message.get(0).getErrorCode());
                    mapBodyException.put("error_message", message.get(0).getErrorMessage());
                    response.setContentType("application/json");
                    response.setStatus(message.get(0).getStatusCode());
                    trnJsonObjRequest.setModuleNo(2L);
                    trnJsonObjRequest.setRequestTime(new Date());
                    trnJsonObjRequest.setJsonRequest(body);
                    trnJsonObjRequest.setAccessToken(ccraToken);
                    trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                    trnJsonObjResponse.setResponseTime(new Date());
                    trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                    trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));

                    String jsonBody = mapper.writeValueAsString(mapBodyException);
                    trnJsonObjResponse.setJsonResponse(jsonBody);
                    trnJson.saveJsonResponse(trnJsonObjResponse);

                    response.getWriter().write(jsonBody);
                }
            } catch (IOException | NumberFormatException | JSONException e) {
                logger.error(e.getMessage());
                List<MessageError> message = messageErrorService.getMessageByCode("00041");

                ResponseConfig resConfig = new ResponseConfig();
                resConfig.setError_code(message.get(0).getErrorCode());
                resConfig.setError_message(message.get(0).getErrorMessage());
                final Map<String, Object> mapBodyException = new LinkedHashMap<>();
                mapBodyException.put("error_code", message.get(0).getErrorCode());
                mapBodyException.put("error_message", message.get(0).getErrorMessage());
                response.setContentType("application/json");
                response.setStatus(message.get(0).getStatusCode());
                trnJsonObjRequest.setModuleNo(2L);
                trnJsonObjRequest.setRequestTime(new Date());
                trnJsonObjRequest.setJsonRequest(body);
                trnJsonObjRequest.setAccessToken(ccraToken);
                trnJsonObjResponse = trnJson.saveJsonRrequest(trnJsonObjRequest);
                trnJsonObjResponse.setResponseTime(new Date());
                trnJsonObjResponse.setErrorCode(message.get(0).getErrorCode());
                trnJsonObjResponse.setStatusCode(Long.parseLong(message.get(0).getStatusCode().toString()));

                String jsonBody = mapper.writeValueAsString(mapBodyException);
                trnJsonObjResponse.setJsonResponse(jsonBody);
                trnJson.saveJsonResponse(trnJsonObjResponse);

                response.getWriter().write(jsonBody);
            }
        }
    }

    public String deCodeCCRAToken() {
        String body = "";
        try {
            Base64 base64Url = new Base64(true);
            String jwtToken = "";
            logger.info("------------ Decode JWT ------------");
            loggerText = String.format("JWT Token : %s", jwtToken);
            logger.info(loggerText);
            String[] split_string = jwtToken.split("\\.");
            String base64EncodedHeader = split_string[0];
            String base64EncodedBody = split_string[1];
            String base64EncodedSignature = split_string[2];

            logger.info("~~~~~~~~~ JWT Header ~~~~~~~");

            String header = new String(base64Url.decode(base64EncodedHeader));
            loggerText = String.format("JWT Header : %s", header);
            logger.info(loggerText);

            logger.info("~~~~~~~~~ JWT Body ~~~~~~~");
            body = new String(base64Url.decode(base64EncodedBody));
            loggerText = String.format("JWT Body : %s", body);
            logger.info(loggerText);
            loggerText = String.format("JWT Signature : %s", base64EncodedSignature);
            logger.info(loggerText);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return body;
    }

    public static String getBearerTokenHeader() {
        return null;
    }

}
