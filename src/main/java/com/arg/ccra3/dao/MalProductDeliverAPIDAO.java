package com.arg.ccra3.dao;

import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.data.ReportCreatorData;
import static com.arg.ccra3.common.InquiryConstants.*;
import com.arg.ccra3.dao.repo.MalProductDeliverAPIRepo;
import com.arg.ccra3.models.User;
import com.arg.ccra3.models.api.MalProductDeliverAPI;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class MalProductDeliverAPIDAO {
    
    private MalProductDeliverAPIRepo malRepo;
    
    public MalProductDeliverAPIDAO(MalProductDeliverAPIRepo malRepo){
        this.malRepo = malRepo;
    }
    
    public void saveMalProdAPI(User user, ReportCreatorData data, Map<String, Object> header, String within7days){
        MalProductDeliverAPI mal = new MalProductDeliverAPI();
        mal.setCreatedDate(new Date());
        
        mal.setTransactionId(data.getTransID());
        mal.setExpenseId(data.getExpenseID());
        mal.setRowNumber(0l);
        mal.setRequestType("A");
        mal.setDunsNo(user.getComapnyID());
        mal.setReasonCode((Integer)header.get("reasonCode"));
        mal.setAirefcode1((String)header.get("aiRefCode1"));
        mal.setAirefcode2((String)header.get("aiRefCode2"));
        mal.setAirefcode3((String)header.get("aiRefCode3"));        
        mal.setCompanyName((String)header.get("companyName"));
        mal.setMinorversion((int)header.get("minorVersion"));
        mal.setDeleted(0);
        mal.setReadFlag(false);
        mal.setPreferredLanguage(user.getPreferLanguage() == PreferLanguage.ENGLISH ? "E" : "L");
        mal.setuID(data.getUID());
        mal.setGroupId(user.getuID());
        mal.setGroupAIId(user.getuID());
        mal.setMatchKey(data.getMatchKey());
        mal.setWithin7days("Y".equals(within7days));
        
        switch(data.getObjectID()){
            case OBJECT_TYPE.BASIC_REPORT -> {mal.setObjectId(OBJECT_TYPE.BASIC_REPORT_API);}
            case OBJECT_TYPE.CHINESE_REPORT -> {mal.setObjectId(OBJECT_TYPE.CHINESE_REPORT_API);}
             default-> {
              {mal.setObjectId(OBJECT_TYPE.CHINESE_REPORT_API);}
        }
        }
        
        malRepo.save(mal);
    }
}
