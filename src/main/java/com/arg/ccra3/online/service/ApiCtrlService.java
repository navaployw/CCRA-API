/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.online.service;


import com.arg.ccra3.dao.repo.ApiCtrlRepository;
import com.arg.ccra3.models.ApiCtrl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ApiCtrlService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ApiCtrlRepository apiCtrlRepository;

    
    private String loggerText = "";

    public Boolean allowCDITokenDuplicate(){
        List<ApiCtrl> allowCDITokenDup = apiCtrlRepository.findByCtrlType("CDIDup");
        if(!allowCDITokenDup.isEmpty()){
            logger.info("!allowCDITokenDup.isEmpty()");
            loggerText = String.format("allowCDITokenDup.get(0).getCtrlValue()::: %s",allowCDITokenDup.get(0).getCtrlValue());
            logger.info(loggerText);
            if(allowCDITokenDup.get(0).getCtrlValue().trim().equals("Y")){
                logger.info("Return true");
                return true;
            }
        }

        return false;
    }
    
     public String getFileDBConfigPath(){
        List<ApiCtrl> resultList = apiCtrlRepository.findByCtrlType("DBConfigPath");
        if(!resultList.isEmpty()&&!resultList.get(0).getCtrlValue().trim().equals("")){
            return resultList.get(0).getCtrlValue();
        }

        return "";
    }
     
    public String getCtrlValueByCtrlType(String value){
        List<ApiCtrl> resultList = apiCtrlRepository.findByCtrlType(value);
        if(!resultList.isEmpty()&&!resultList.get(0).getCtrlValue().trim().equals("")){
            return resultList.get(0).getCtrlValue();
        }

        return "";
    } 
     
}
