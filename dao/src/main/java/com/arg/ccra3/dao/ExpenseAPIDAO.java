package com.arg.ccra3.dao;

import com.arg.cb2.inquiry.data.ReportCreatorData;
import static com.arg.ccra3.common.InquiryConstants.*;
import com.arg.ccra3.dao.repo.ExpenseAPIRepo;
import com.arg.ccra3.model.api.ExpenseAPI;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.apache.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;


public class ExpenseAPIDAO {
    
    private static final org.apache.log4j.Logger logger = LogManager.getLogger(ExpenseAPIDAO.class);
    
    private final ExpenseAPIRepo expenseRepo;
    private final JdbcTemplate jdbcTemplate;
    
    public ExpenseAPIDAO(JdbcTemplate jdbcTemplate, ExpenseAPIRepo expenseRepo){
        this.jdbcTemplate = jdbcTemplate;
        this.expenseRepo = expenseRepo;
    }
    
    public List<ExpenseAPI> getdataByExpenseId(Long expenseId){
        logger.info(">>>>getExpenseDataByExpenseId<<<<");
        List<ExpenseAPI> expenseList =expenseRepo.findByexpenseid(expenseId); 
        return expenseList;
    }
    public Long saveReportExpense(Integer uid, Integer ugid, Integer uaiid, Integer cbuid, int objID, Long tokenID,Date requestTime,Date responseTime, AIOrderDAO aiDao){
        LocalDateTime today = LocalDateTime.now();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        final String convertedRequestTime = today.getYear() + 
            (month < 10 ? "0" : "") + month + 
            (day < 10 ? "0" : "") + day;

        ExpenseAPI expense = new ExpenseAPI();
        expense.setUid(uid.longValue());
        expense.setSessionID(tokenID);
        expense.setGroupid(ugid.longValue());
        expense.setGroupaiid(uaiid.longValue());
        expense.setCbuid(cbuid.longValue());
        expense.setFlagexpense("C");
        expense.setFlagcharge(true);
        expense.setDisabled(false);
        expense.setRequesttime(requestTime);
        expense.setResponsetime(responseTime);
        expense.setCreatedby(uid.longValue());
        expense.setUpdatedby(uid.longValue());
        expense.setConverted_requesttime(convertedRequestTime);
        
        Integer userAIID = uaiid;
        expense.setSubmission_flag(aiDao.getSubmissionFlag(userAIID));
        expense.setRevoc_effect_date(aiDao.getRevocEffectDate(userAIID));
        expense.setAcc_mnger_code(aiDao.getAccMngerCode(userAIID));
        expense.setCustomer_no(aiDao.getCustomerCode(userAIID));
        expense.setBrc_no(aiDao.getHKBRCNO(userAIID));
        expense.setCi_no(aiDao.getHKCINO(userAIID));
        expense.setLoc_branch_id(aiDao.getLocBranchID(userAIID));
        expense.setOther_reg_no(aiDao.getOtherRegNO(userAIID));
        expense.setPlace_of_reg(aiDao.getPlaceOfReg(userAIID));
        
        expense.setFlagresult(false);
        
        switch(objID){
            case OBJECT_TYPE.BASIC_REPORT -> {expense.setProductusage(OBJECT_TYPE.BASIC_REPORT_API);}
            case OBJECT_TYPE.CHINESE_REPORT -> {expense.setProductusage(OBJECT_TYPE.CHINESE_REPORT_API);}
        }
        
        jdbcTemplate.query(
            "select COSTOFOBJECT from API_SPM_OBJECT (nolock) where OBJECTID = ?",
            (ResultSet rst) -> {
                do{
                    expense.setUnitcharge(rst.getLong(1));
                }while (rst.next());
            },
            expense.getProductusage()
        );
        
        synchronized(this){
            jdbcTemplate.query(
                "select convert(varchar, getdate(), 112), (select top 1 report_ref_no from api_spm_expense where converted_requesttime = convert(varchar, getdate(), 112) order by expenseid desc) as report_ref_no",
                (ResultSet rst) -> {
                    do{
                        String refNo = rst.getString(2);//A202207151 or null
                        int runningNum = 1;
                        if(refNo != null)
                            runningNum = Integer.parseInt(refNo.substring(9)) + 1;
                        expense.setReport_ref_no("A" + convertedRequestTime + runningNum);
                    }while (rst.next());
                }
            );
            logger.info("expense: " + expense);
            return expenseRepo.save(expense).getExpenseid();
        }
    }
    
    public Long updateResponseTime(ExpenseAPI expense){
        return expenseRepo.save(expense).getExpenseid();
    }
}
