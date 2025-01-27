package com.arg.ccra3.dao;

import com.arg.cb2.inquiry.data.ReportCreatorData;
import com.arg.ccra3.dao.util.InquiryUtilities;
import com.arg.ccra3.model.inquiry.data.SearchByNameData;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.ccra3.dao.util.ValidateReportRule;
import com.arg.ccra3.model.inquiry.data.Demographic;
import com.arg.ccra3.model.inquiry.data.SearchByIdData;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

public class SearchDAO {
    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger(SearchDAO.class);
    //https://mkyong.com/spring/spring-jdbctemplate-querying-examples/
    //https://www.tabnine.com/code/java/methods/org.springframework.jdbc.core.JdbcTemplate/getDataSource
    //https://spring.io/guides/gs/relational-data-access/

    public SearchDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean hasOrderedReportNew(int CBUID, int groupAIID, int objectID,
        int duration, int durationUnit)
        throws Exception
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

    /*public Long createTransactionDeliver(Transaction trans, MailProductDeliver productData) throws Exception
    {
        if (trans == null || productData == null)
            throw new IllegalArgumentException("All parameters could not be null");

        Long transId = null;

        try{
            trans = transactionRepo.save(trans);
            transId = trans.getTransactionID();

            productData.setTransactionID(transId);
            mailProDeliRepo.save(productData);
        }
        catch (Throwable e){
            throw new GenericException("ICE-R00000", e.getMessage());
        }

        return transId;
    }

    public void updateSearchData(
        long transactionId,
        String airef1, String airef2, String airef3,
        int reasoncode,
        String nameSearch
    )throws Exception {

        MailProductDeliver local = null;

        try {
            local = mailProDeliRepo.findById(transactionId).get();
        }
        catch (Exception e){
            //log.warn(e.getMessage());
        }

        if (local != null){
            local.setAIRefCode1(airef1);
            local.setAIRefCode2(airef2);
            local.setAIRefCode3(airef3);
            local.setReasonCode(reasoncode);
            local.setNameSearch(nameSearch);
            mailProDeliRepo.save(local);
        }
    }*/

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

    /*public boolean hasLocalLanguageData(int cbuid)
        throws Exception
    {
        try
        {
            {
                Object shareHolders = jdbcTemplate.queryForObject(
                    getString("search.haslocalrecord.shareholdernamelo"),
                    Object.class,
                    cbuid
                );
                if(shareHolders != null)
                    return true;
            }

            {
                //check amount past due In 5  Years
                ValidateReportRule validateReportRule = new ValidateReportRule();
                int amountPastDueInYears = validateReportRule.findAmountPastDueInYears(cbuid,jdbcTemplate);

                List<Demographic> demographics = jdbcTemplate.query(
                    getString("search.haslocalrecord.demographic"),
                    BeanPropertyRowMapper.newInstance(Demographic.class),
                    new Object[]{ cbuid, InquiryUtilities.getPeriod(amountPastDueInYears) }
                );

                for(Demographic demographic : demographics)
                    if(demographic.checkLocalStringNotEmpty())
                        return true;
            }

            {
                Calendar pastManyYear = Calendar.getInstance(Locale.ENGLISH);
                pastManyYear.add(Calendar.YEAR, -2);
                Date pastManyYearTime = new Date(pastManyYear.getTimeInMillis());

                List<Map<String, Object>> plaintiff_defendants = jdbcTemplate.queryForList(
                    getString("search.haslocalrecord.plaintiff-defendant"),
                    new Object[]{ cbuid, pastManyYearTime }
                );

                if(plaintiff_defendants.isEmpty())
                    return false;

                final String[] cols = new String[]{"GIVEN_NAME_NAT", "SURNAME_NAT", "PLAINTIFF_1_NAT", "ACTIONNO"};
                for(Map<String, Object> it : plaintiff_defendants){
                    for(String col : cols){
                        String tmpField = it.get(col).toString();
                        if (tmpField != null && tmpField.trim().length() > 0)
                            return true;
                    }
                }
                for(Map<String, Object> pd : plaintiff_defendants){
                    List<Map<String, Object>> other_defendants = jdbcTemplate.queryForList(
                        getString("search.haslocalrecord.other-defendant"),
                        new Object[]{ pd.get("ACTIONNO"), pastManyYearTime}
                    );
                    for(Map<String, Object> od : other_defendants){
                        int first2 = 0;
                        for(String col : cols){
                            String tmpField = od.get(col).toString();
                            if (tmpField != null && tmpField.trim().length() > 0)
                                return true;

                            if(++first2 == 2)break;//check only cols[0] and cols[1]
                        }
                    }
                }
            }
        }
        catch (Exception e){ 
            throw e;
        }
        
        return false;
    }*/
    
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
                return;
            }while (rs.next());
        };
        
        jdbcTemplate.query(sql, rowHandler, userID);
    }
    
    private void setCompanyInfoOfReport(Map<String, Object> ret, Long companyId){
        String sql = "exec get_customerByDunsNo null, null, ?";
        RowCallbackHandler rowHandler = (ResultSet rs) -> {
            do {
                /*CBUID	numeric
                COMPANY_NAME_ENG varchar
                COMPANY_NAME_LO	varchar*/        
                logger.debug("rs--------------------------------------------: " + rs);
                ret.put("CBUID", rs.getInt("CBUID"));
                ret.put("CompanyName", rs.getString("COMPANY_NAME_ENG"));
                break;
            }while (rs.next());
        };        
        jdbcTemplate.query(sql, rowHandler, companyId);
    }
}
