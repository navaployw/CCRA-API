/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.online.service;



import com.arg.ccra3.model.TrnJson;
import com.arg.ccra3.dao.repo.TrnJsonRepository;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author navaployw
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TrnJsonService {
    @Autowired
    private TrnJsonRepository trnJsonRepository;
    TrnJson objSave;
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private String loggerText = "";
    
    public TrnJson saveJsonRrequest(TrnJson data) {

        trnJsonRepository.save(data);

        return data;
    }

     public TrnJson saveJsonResponse(TrnJson data) {
        objSave = data;
        loggerText = String.format("JSONResponse: %s", data.getJsonResponse());
        logger.info(loggerText);
        loggerText = String.format("ResponseTime: %s", data.getResponseTime());
        logger.info(loggerText);
        loggerText = String.format("StatusCode: %s", data.getStatusCode());
        logger.info(loggerText);
        loggerText = String.format("ErrorCode: %s", data.getErrorCode());
        logger.info(loggerText);
        loggerText = String.format("TrnApiId: %s", data.getTrnApiId());
        logger.info(loggerText);

        trnJsonRepository.save(data);
        return data;
    }
     public TrnJson getSaveObj(){

         return objSave;
     } 
}
