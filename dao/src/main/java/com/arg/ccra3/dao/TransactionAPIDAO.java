package com.arg.ccra3.dao;

import com.arg.cb2.inquiry.data.ReportCreatorData;
import static com.arg.ccra3.common.InquiryConstants.*;
import com.arg.ccra3.dao.repo.TransactionAPIRepo;
import com.arg.ccra3.model.api.TransactionAPI;

public class TransactionAPIDAO {
    
    private final TransactionAPIRepo tranRepo;
    
    public TransactionAPIDAO(TransactionAPIRepo tranRepo){
        this.tranRepo = tranRepo;
    }
    
    public long saveTransactionAPIReport(long uid, long ugid, long uaiid, int objId, long tokenId){
        TransactionAPI tran = new TransactionAPI();
        tran.setSessionId(tokenId);
        tran.setuID(uid);
        tran.setGroupId(ugid);
        tran.setGroupAIId(uaiid);
        tran.setChannel("A");
        tran.setAmountResult(0);
        tran.setCreatedBy(uid);
        tran.setUpdatedBy(uid);
        
        switch(objId){
            case OBJECT_TYPE.BASIC_REPORT -> {tran.setObjectId(OBJECT_TYPE.BASIC_REPORT_API);}
            case OBJECT_TYPE.CHINESE_REPORT -> {tran.setObjectId(OBJECT_TYPE.CHINESE_REPORT_API);}
        }
        
        return tranRepo.save(tran).getTransactionId();
    }
}
