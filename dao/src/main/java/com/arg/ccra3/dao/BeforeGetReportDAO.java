/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.dao;

import ch.qos.logback.classic.Logger;
import com.arg.ccra3.model.MessageError;
import com.arg.ccra3.model.TrnJson;
import com.arg.ccra3.model.api.TokenAPI;
import com.arg.ccra3.model.api.UserAPI;
import com.arg.ccra3.model.security.ChkThreshold;
import com.arg.ccra3.model.security.Company;
import com.arg.ccra3.model.security.SpmTransaction;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 *
 * @author navaployw
 */
public class BeforeGetReportDAO {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = (Logger) LoggerFactory.getLogger(BeforeGetReportDAO.class);
    private String loggerText = "";
    public BeforeGetReportDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
 
    public List<ChkThreshold> checkThreshold(BigDecimal uid) {
        logger.info("checkThreshold>>>");
        String sql = "EXEC check_threshold null,null,?";
        List<ChkThreshold> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ChkThreshold.class), uid);
        loggerText = String.format("resultsChkThreshold>>>>> %s", results);
        logger.info(loggerText);
        //1notpass 0pass
        return results;
    }

    public List<Company> getCompanyByDunsNo(BigDecimal company) {
        logger.info("getCompanyByDunsNo>>>");
        String sql = "exec get_customerByDunsNo null, null, ?";
        List<Company> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Company.class), company);
        loggerText = String.format("resultsCompany>>>>> %s", results);
        logger.info(loggerText);
        return results;
    }

    public List<ChkThreshold> checkOrder7Day(BigDecimal cbuId, BigDecimal groupAiId, Integer reportType) {
        logger.info("checkOrder7Day>>>");
        String sql = "exec Check_reOrderIn7Days null, null,?,?,?";
        Object[] params = new Object[]{groupAiId, cbuId, reportType};
        List<ChkThreshold> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ChkThreshold.class), params);
        loggerText = String.format("resultsCheckOrder7Day>>>>> %s", results);
        logger.info(loggerText);
        //1notpass 0pass
        return results;
    }

    public List<ChkThreshold> checkOrderChinese(BigDecimal cbuId, BigDecimal groupAiId, Integer reportType) {
        logger.info("checkOrderChinese>>>");
        String sql = "exec check_canOrderChinese null, null,?,?,?";
        Object[] params = new Object[]{groupAiId, cbuId, reportType};
        List<ChkThreshold> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(ChkThreshold.class), params);
        loggerText = String.format("checkOrderChinese>>>>> %s", results);
        logger.info(loggerText);
        //1notpass 0pass
        return results;
    }

    public List<MessageError> getErrorMessageByErrorCodeNative(String code) {
        logger.info("getErrorMessageByErrorCodeNative>>>");
        String sql = "select * from API_MESSAGE_ERR where ERROR_CODE = ?";
        List<MessageError> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(MessageError.class), code);
        loggerText = String.format("getErrorMessageByErrorCodeNative>>>>> %s", results);
        logger.info(loggerText);
        return results;

    }

    public List<TokenAPI> getTokenIdFromCCRAToken(String token) {
        logger.info("getTokenIdFromCCRAToken>>>");
        String sql = "select * from API_TOKEN where ACCESS_TOKEN = ?";
        List<TokenAPI> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TokenAPI.class), token);
        loggerText = String.format("getTokenIdFromCCRAToken>>>>> %s", results);
        logger.info(loggerText);
        return results;

    }
    
    
    public List<TrnJson> getDataFromTokenCDIID(Long tokenCDIID){
        String sql = "select top(1) * from API_TRN_JSON where TOKENCDIID = ? order by TOKENCDIID desc";
        List<TrnJson> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TrnJson.class), tokenCDIID);
        return results;
    }
    
    public List<TrnJson> getDataFromTokenCDIIDandUIDandModule(Long tokenCDIID,Integer uID,String module){
        String sql = "select * from API_TRN_JSON where TOKENCDIID = ? and UID= ? and MODULE= ?";
        Object[] params = new Object[]{tokenCDIID, uID, module};
        List<TrnJson> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(TrnJson.class), params);
        return results;
    }

    public List<SpmTransaction> insertApiSpmTransaction(Integer sessionId, BigDecimal uid, BigDecimal groupAiId, BigDecimal groupId,Integer expense) {
        logger.info("insertApiSpmTransaction>>>");
        String sql = "INSERT INTO API_SPM_TRANSACTION VALUES(?,?,?,?,202,null,'A',?,?,getDate(),?,getDate()); SELECT * FROM API_SPM_TRANSACTION WHERE TRANSACTIONID = SCOPE_IDENTITY()";
        Object[] params = new Object[]{sessionId, uid, groupAiId, groupId,expense, uid, uid};
        List<SpmTransaction> results = this.jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SpmTransaction.class), params);
        
        return results;
    }

    public void insertApiProductDeliver (Integer transactionId, String dunsNo,Integer reasonCode,String aiRefCode1,String aiRefCode2,String aiRefCode3,
        Integer objectId,BigDecimal uId,BigDecimal groupAiId,BigDecimal groupId) {
        logger.info("insertApiProductDeliver>>>");
        String sql = "INSERT INTO API_PRODUCTDELIVER OUTPUT Inserted.TRANSACTIONID VALUES(?,NULL,NULL,0,'A',?,?,?,?,?,?,0,NULL,NULL,getDate(),NULL,'E',?,?,?,?,0,NULL,1,1,0)";
        Object[] params = new Object[]{transactionId, dunsNo, reasonCode, aiRefCode1, aiRefCode2, aiRefCode3,objectId,uId,groupAiId,groupId,transactionId};
        jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SpmTransaction.class), params);
    }
    
        
    public String updateTresholdApiUser(Integer aId){
        try{
        String sql = "update API_USER set THRESHOLD_WEEK_COUNT = isnull(THRESHOLD_WEEK_COUNT,0)+1 where  AID = %s";
        String query = String.format(sql,aId);
        jdbcTemplate.execute(query);
        }catch(DataAccessException e){
            loggerText = String.format("Error:updateTreshold: %s",e);
            logger.error(loggerText);
            return "Fail";
        }
        return "Success";
    }

}
