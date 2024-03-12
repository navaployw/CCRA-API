/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.online.service;

import com.arg.ccra3.dao.ExpenseAPIDAO;
import com.arg.ccra3.dao.ReportCreatorDAO;
import com.arg.ccra3.dao.security.util.SymmetricCipher;
import com.arg.ccra3.models.TrnJson;
import com.arg.ccra3.models.api.ExpenseAPI;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class HttpTraceFilterRepositoryManager extends OncePerRequestFilter {

    @Autowired
    TrnJsonService trnJson;
    @Autowired
    private ApiCtrlService apiCtrlService;
    @Autowired
    ExpenseAPIDAO expenseDAO;
    @Autowired
    ReportCreatorDAO reportCreatorDao;
    
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {

        String ctrlValue = apiCtrlService.getCtrlValueByCtrlType("encryptFlag");
        String loggerText = "";
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(
                response);
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(
                request);
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            loggerText = String.format("aa.trnJsonID>>> %s", trnJson.getSaveObj());
            logger.info(loggerText);
            if (trnJson.getSaveObj() != null) {
                TrnJson trnJsonObjResponse = trnJson.getSaveObj();
                loggerText = String.format("TrnJsonObjResponse:: %s", trnJsonObjResponse);
                logger.info(loggerText);
                loggerText = String.format("statusCode:: %s", Long.parseLong(Integer.toString(response.getStatus())));
                logger.info(loggerText);
                loggerText = String.format("getResponseBody(wrappedResponse).toString().length():: %s", getResponseBody(wrappedResponse).toString().length());
                logger.info(loggerText);
                StringBuffer responseBody = getResponseBody(wrappedResponse);
                if (responseBody != null) {
                    if (trnJsonObjResponse.getModuleNo() == 3) {
                        trnJsonObjResponse.setJsonResponse("");
                    } else {
                        if (ctrlValue.equals("Y")) {
                            SymmetricCipher cy = SymmetricCipher.getInstance();
                            trnJsonObjResponse.setJsonResponse(cy.encrypt(getResponseBody(wrappedResponse).toString()));
                        } else {
                            trnJsonObjResponse.setJsonResponse(getResponseBody(wrappedResponse).toString());
                        }
                    }
                    trnJsonObjResponse.setResponseTime(new Date());
                    trnJsonObjResponse.setStatusCode(Long.parseLong(Integer.toString(response.getStatus())));
                    trnJson.saveJsonResponse(trnJsonObjResponse);
                    if(trnJsonObjResponse.getModuleNo()==2){
                        logger.info(">>>ModuleNo::2<<<");
                        List<ExpenseAPI>expenseList = expenseDAO.getdataByExpenseId(trnJsonObjResponse.getExpenseId());
                        if(!expenseList.isEmpty()){
                            expenseList.get(0).setResponsetime(trnJsonObjResponse.getResponseTime());
                            Long expenseId = expenseDAO.updateResponseTime(expenseList.get(0));
                            reportCreatorDao.updateSuspectedAbnormal(Integer.parseInt(expenseList.get(0).getGroupaiid().toString()),       
                            Integer.parseInt(expenseList.get(0).getCbuid().toString()),expenseId,trnJsonObjResponse.getResponseTime());
                        }
                    }
                    
                }
            }
        } catch (Exception ex) {
            loggerText = String.format("Error %s", ex);
            logger.error(loggerText);
        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }

    protected StringBuffer getResponseBody(
            ContentCachingResponseWrapper wrappedResponse) {
        try {
            if (wrappedResponse.getContentSize() <= 0) {
                return null;
            }
            byte[] srcBytes = wrappedResponse.getContentAsByteArray();
            Charset charset = Charset.forName(wrappedResponse.getCharacterEncoding());
            CharsetDecoder decoder = charset.newDecoder();
            ByteBuffer srcBuffer = ByteBuffer.wrap(srcBytes);
            CharBuffer resBuffer = decoder.decode(srcBuffer);
            return new StringBuffer(resBuffer);
        } catch (CharacterCodingException e) {
            logger.error("Error" + trnJson.getSaveObj());
            return null;
        }
    }

}
