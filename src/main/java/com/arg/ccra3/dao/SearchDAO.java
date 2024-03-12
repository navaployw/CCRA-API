package com.arg.ccra3.dao;

import com.arg.cb2.inquiry.data.ReportCreatorData;
import com.arg.ccra3.dao.util.InquiryUtilities;
import com.arg.ccra3.models.inquiry.data.SearchByNameData;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.ccra3.dao.util.ValidateReportRule;
import com.arg.ccra3.models.inquiry.data.Demographic;
import com.arg.ccra3.models.inquiry.data.SearchByIdData;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

public class SearchDAO {
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger(SearchDAO.class);

    public SearchDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasOrderedReportNew(int CBUID, int groupAIID, int objectID,
        int duration, int durationUnit)
       
    {
        Calendar backDate =
            InquiryUtilities.getDateBackFromCurrent(duration, durationUnit);

        String sql = getString(
            "search.hasorderreport",
            new String[]{CBUID+"", groupAIID+"", objectID+"", new java.sql.Date(backDate.getTimeInMillis()).toString()}
        );

        return jdbcTemplate.queryForList(sql).size() == 1;//select top 1
    }

    public List<SearchByNameData> searchByEnglishName(String name, int page_size){
        String sql = getString(
            "search.name.english",
            new String[] { Integer.toString(page_size), name }
        );

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SearchByNameData.class));
    }

    public List<SearchByNameData> searchByLocalName(String name, int page_size){
        String sql = getString(
            "search.name.local",
            new String[] { Integer.toString(page_size), name }
        );

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SearchByNameData.class));
    }

    public List<SearchByIdData> searchByHKBRC(String hkbrc){
        String sql = getString("search.id.brc", hkbrc);

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SearchByIdData.class));
    }
    public List<SearchByIdData> searchByHKCI(String hkci) {
        String sql = getString("search.id.ci", hkci);

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SearchByIdData.class));
    }
    public List<SearchByIdData> searchByOtherReg(String otherReg, String placeReg, int limit) {
        String sql = getString(
            "search.id.other-with-placereg",
            new String[] { Integer.toString(limit), otherReg, placeReg }
        );

        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(SearchByIdData.class));
    }
    
    public Map<String, Object> getLookupInfoOfUser(String userID, String companyID){
        Map<String, Object> ret = new HashMap<>();
        setViewApiUserOfReport(ret, userID);
        setCompanyInfoOfReport(ret, Long.parseLong(companyID));
        return ret;
    }
    
    private void setViewApiUserOfReport(Map<String, Object> ret, String userID){
        String sql = "select UID, GROUPAIID, GROUPID from VIEW_API_USER (nolock) where USERID = ?";
        RowCallbackHandler rowHandler = (ResultSet rs) -> {
            do {         
                ret.put("UID", rs.getInt("UID"));
                ret.put("AIID", rs.getInt("GROUPAIID"));
                ret.put("GroupID", rs.getInt("GROUPID"));
                
            }while (rs.next());
        };
        
        jdbcTemplate.query(sql, rowHandler, userID);
    }
    
    private void setCompanyInfoOfReport(Map<String, Object> ret, Long companyId){
        String sql = "exec get_customerByDunsNo null, null, ?";
        RowCallbackHandler rowHandler = (ResultSet rs) -> {
            do {        
                logger.debug("rs--------------------------------------------: " + rs);
                ret.put("CBUID", rs.getInt("CBUID"));
                ret.put("CompanyName", rs.getString("COMPANY_NAME_ENG"));
              
            }while (rs.next());
        };        
        jdbcTemplate.query(sql, rowHandler, new BigDecimal(companyId) );
    }
}
