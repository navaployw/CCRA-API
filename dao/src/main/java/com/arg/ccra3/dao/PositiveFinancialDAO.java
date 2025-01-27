
package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.InquiryUtilities.*;
import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.cb2.inquiry.data.SPositiveFinancialOptOutFlagGroup;
import com.arg.cb2.inquiry.data.SPositiveFinancialWithCancel;
import com.arg.cb2.inquiry.data.SPositiveFinancialWithoutCancel;
import com.arg.cb2.inquiry.data.SPositiveFinancials;
import com.arg.util.GenericException;
import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.Comparator;
import static java.util.Comparator.comparing;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;


public class PositiveFinancialDAO {
    
    private final JdbcTemplate jdbcTemplate;

    public PositiveFinancialDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    static final String REPAID = "1";
    static final String FACILITIES_CANCEL = "3";

    private static final String COLUMN_CONTIGENT_SHARED_3RD_PARTY = getString(
                            "report.table.prod_credit.columns.contigentshared3rdparty");

    private static final String COLUMN_GROUP_ID = getString("report.table.prod_credit.columns.groupid");
    private static final String COLUMN_PERIOD = getString(
                            "report.table.prod_credit.columns.period");
    private static final String COLUMN_REPORTED_PERIOD = getString(
                            "report.table.prod_credit.columns.reportedperiod");
    private static final String COLUMN_REVOC_FACILITY_LIMIT = getString("report.table.prod_credit.columns.revocationfacilitylimit");
    private static final String COLUMN_REVOC_SHARED_3RD_PARTY = getString(
                            "report.table.prod_credit.columns.revocationshared3rdparty");
    private static final String COLUMN_NON_REVOC_FACILITY_LIMIT = getString("report.table.prod_credit.columns.nonrevocationfacilitylimit");
    private static final String COLUMN_NON_REVOC_SHARED_3RD_PARTY = getString("report.table.prod_credit.columns.nonrevocationshared3rdparty");
    private static final String COLUMN_CONTIGENT_FACILITY_LIMIT = getString(
                            "report.table.prod_credit.columns.contigentfacilitylimit");
    private static final String COLUMN_HP_LEASING_FACILITY_LIMIT = getString(
                        "report.table.prod_credit.columns.hpleasingfacilitylimit");
    private static final String COLUMN_HP_LEASING_SHARED_3RD_PARTY = getString(
                            "report.table.prod_credit.columns.hpleasingshared3rdparty");
    private static final String COLUMN_CONBINED_FACILITY_LIMIT = getString(
                            "report.table.prod_credit.columns.combinedfacilitylimit");
    private static final String COLUMN_CONBINED_SHARED_3RD_PARTY = getString(
                            "report.table.prod_credit.columns.combinedshared3rdparty");
    private static final String COLUMN_COMBINED_LIMIT_DETAILS = getString(
                            "report.table.prod_credit.columns.combinedlimitdetails");
    private static final String COLUMN_TANGIBLE_SECURITY = getString(
                            "report.table.prod_credit.columns.tangiblesecurity");
    private static final String COLUMN_DISPUTED_STATUS = getString(
                            "report.table.prod_credit.columns.disputedstatus");
    private static final String COLUMN_CREDIT_DISPUTED_STATUS = getString(
                            "report.table.prod_credit.columns.creditdisputedstatus");
    private static final String COLUMN_FLAGDATE = getString(
                                "report.table.prod_credit.columns.flagdate");
    private static final String COLUMN_DISPUTEED_DATE = getString(
                                "report.table.prod_credit.columns.disputeddate");
    private static final String COLUMN_FACILIT_CANCEL_STATUS = getString(
                            "report.table.prod_credit.columns.facilitycancelstatus");
    class ComparatorSort implements Comparator<SPositiveFinancialWithCancel>
    {
         public int compare(SPositiveFinancialWithCancel s1,SPositiveFinancialWithCancel s2)
         {
             BigDecimal bs1 = BigDecimal.ZERO;
             BigDecimal bs2 = BigDecimal.ZERO;

             bs1 = bs1.add(s1.getCombinedFacilityLimit() == null ? ZERO:s1.getCombinedFacilityLimit());
             bs1 = bs1.add(s1.getHPLeasingFacilityLimit() == null ? ZERO:s1.getHPLeasingFacilityLimit());
             bs1 = bs1.add(s1.getRevolvingFacilityLimit()== null ? ZERO:s1.getRevolvingFacilityLimit());
             bs1 = bs1.add(s1.getNonRevolvingFacilityLimit() == null ? ZERO:s1.getNonRevolvingFacilityLimit());
             bs1 = bs1.add(s1.getContigentFacilityLimit() == null ? ZERO:s1.getContigentFacilityLimit());

             bs2 = bs2.add(s2.getCombinedFacilityLimit() == null ? ZERO:s2.getCombinedFacilityLimit());
             bs2 = bs2.add(s2.getHPLeasingFacilityLimit() == null ? ZERO:s2.getHPLeasingFacilityLimit());
             bs2 = bs2.add(s2.getRevolvingFacilityLimit() == null ? ZERO:s2.getRevolvingFacilityLimit());
             bs2 = bs2.add(s2.getNonRevolvingFacilityLimit() == null ? ZERO:s2.getNonRevolvingFacilityLimit());
             bs2 = bs2.add(s2.getContigentFacilityLimit() == null ? ZERO:s2.getContigentFacilityLimit());
             if(bs1.compareTo(bs2) == 0)
             {
                   Integer period1 = Integer.parseInt(s1.getPeriod());
                   Integer period2 = Integer.parseInt(s2.getPeriod());
                   if(period1 < period2)
                   {
                        return 1;
                   }
                   else if(period1 > period2)
                   {
                        return -1;
                   }
                   else
                   {

                       String ai1 = s1.getSubmittedBy();
                       String ai2 = s2.getSubmittedBy();
                       if(ai1.indexOf("AI#") != -1 && ai2.indexOf("AI#") != -1)
                       {
                           int aisub1 = Integer.parseInt(ai1.substring(3));
                           int aisub2 = Integer.parseInt(ai2.substring(3));
                           if(aisub1 < aisub2)
                           {
                               return -1;
                           }
                           if(aisub1 > aisub2)
                           {
                               return 1;
                           }
                           else
                           {
                               return 0;
                           }
                       }
                       else
                       {
                           if(s1.getSubmittedBy().compareToIgnoreCase(s2.getSubmittedBy()) < 0)
                           {
                               return -1;
                           }
                           if(s1.getSubmittedBy().compareToIgnoreCase(s2.getSubmittedBy()) > 0)
                           {
                               return 1;
                           }
                           else
                           {
                               return 0;
                           }
                       }
                   }
             }
             else
             {
                 return bs2.compareTo(bs1);
             }
         }
    }

    private boolean checkWriteOff(int cbuid, int groupid, String currPeriod)
        throws Exception
    {
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
            getString("reportcreator.preparedstatement.credit.checkwriteoff"),
            new Object[]{cbuid, groupid, backToPeriod(currPeriod, 2), currPeriod}
        );
        
        if(results != null){
            for(var rst : results){
                String str_temp = (String) rst.get(getString(
                    "report.table.prod_credit.columns.facilitycancelstatus"));

                if (FACILITIES_CANCEL.equals(str_temp))
                    break;

                BigDecimal bd_temp = (BigDecimal)rst.get(getString(
                    "report.table.prod_credit.columns.writeoffamount"));

                if (null != bd_temp && bd_temp.compareTo(ZERO) > 0)
                    return true;
            }
        }
        return false;
    }


    public SPositiveFinancialWithCancel[] getPositiveCreditWithCancel(
        AIOrderDAO aiDao, Integer uGroupID, String docLang, Integer CBUID, Integer years)
        throws Exception
    {
        final SPositiveFinancialOptOutFlagGroup optOutFlag = getPositiveOptOutFlag(CBUID);
        final List<SPositiveFinancialWithCancel> result = new LinkedList<>();
        
        String sql = getString("reportcreator.preparedstatement.credit.positivecreditwithcancel");
        
        String period = getPeriod(years);
        Object[] params = new Object[]{period, period, CBUID, period};
        
        RowCallbackHandler rowHandler = (ResultSet rst) -> {
            do{
                SPositiveFinancialWithCancel cancel = new SPositiveFinancialWithCancel();

                int gid = rst.getInt(COLUMN_GROUP_ID);
                cancel.setGroupID(gid);
                cancel.setCBUID(CBUID);
                cancel.setSubmittedBy(aiDao.getAIName(uGroupID,
                        gid, docLang ,CBUID));

                String currPeriod = rst.getString(COLUMN_PERIOD);
                cancel.setPeriod(currPeriod);
                cancel.setPreviousPeriod(rst.getString(COLUMN_REPORTED_PERIOD));

                cancel.setRevolvingFacilityLimit(rst.getBigDecimal(
                        COLUMN_REVOC_FACILITY_LIMIT));
                String str_temp = rst.getString(COLUMN_REVOC_SHARED_3RD_PARTY);
                cancel.setRevolvingShared3rdParty((!((null == str_temp)
                        || ("0".equals(str_temp)))));

                cancel.setNonRevolvingFacilityLimit(rst.getBigDecimal(
                        COLUMN_NON_REVOC_FACILITY_LIMIT));
                str_temp = rst.getString(COLUMN_NON_REVOC_SHARED_3RD_PARTY);
                cancel.setNonRevolvingShared3rdParty((!(null == str_temp) && !("0".equals(str_temp))));

                cancel.setContigentFacilityLimit(rst.getBigDecimal(COLUMN_CONTIGENT_FACILITY_LIMIT
                        ));
                str_temp = rst.getString(COLUMN_CONTIGENT_SHARED_3RD_PARTY);
                cancel.setContigentShared3rdParty((!((null == str_temp)
                        || ("0".equals(str_temp)))));

                cancel.setHPLeasingFacilityLimit(rst.getBigDecimal(COLUMN_HP_LEASING_FACILITY_LIMIT
                        ));
                str_temp = rst.getString(COLUMN_HP_LEASING_SHARED_3RD_PARTY);
                cancel.setHPLeasingShared3rdParty((!((null == str_temp)
                        || ("0".equals(str_temp)))));

                cancel.setCombinedFacilityLimit(rst.getBigDecimal(COLUMN_CONBINED_FACILITY_LIMIT
                        ));
                str_temp = rst.getString(COLUMN_CONBINED_SHARED_3RD_PARTY);
                cancel.setCombinedShared3rdParty((!((null == str_temp)
                        || ("0".equals(str_temp)))));
                cancel.setCombinedLimitDetails(rst.getString(COLUMN_COMBINED_LIMIT_DETAILS));

                cancel.setTangibleSecurity(rst.getString(COLUMN_TANGIBLE_SECURITY));

                str_temp = rst.getString(COLUMN_DISPUTED_STATUS);
                boolean isDispute = ("1".equals(str_temp));

                str_temp = rst.getString(COLUMN_CREDIT_DISPUTED_STATUS);
                isDispute = isDispute || ("1".equals(str_temp));

                cancel.setDisputedStatus(isDispute);

                if ("1".equals(str_temp))
                    cancel.setDisputedDate(rst.getDate(COLUMN_FLAGDATE));
                else
                    cancel.setDisputedDate(rst.getDate(COLUMN_DISPUTEED_DATE));

                str_temp = rst.getString(COLUMN_FACILIT_CANCEL_STATUS);
                cancel.setFacilityCancelledDetailsPeriod(currPeriod);
                cancel.setFacilityCancelledDetailsStatus(str_temp);
                cancel.setWriteOff(rst.getBoolean("is_write_off"));

                cancel.setRevolvingOptOutFlag(optOutFlag.isRevolvingOptOut(currPeriod, String.valueOf(gid)));
                cancel.setNonRevolvingOptOutFlag(optOutFlag.isNonRevolvingOptOut(currPeriod, String.valueOf(gid)));
                cancel.setContigentOptOutFlag(optOutFlag.isContingentOptOut(currPeriod, String.valueOf(gid)));
                cancel.setHPLeasingOptOutFlag(optOutFlag.isHpLeasingOptOut(currPeriod, String.valueOf(gid)));
                cancel.setCombinedOptOutFlag(optOutFlag.isCombinedOptOut(currPeriod, String.valueOf(gid)));

                result.add(cancel);
                Collections.sort(result, new ComparatorSort());
            }while (rst.next());
        };
        
        jdbcTemplate.query(sql, rowHandler, params);        
        return result.toArray(SPositiveFinancialWithCancel[]::new);
    }



    private SPositiveFinancialWithoutCancel[] getPositiveCreditWithOutCancel(
        AIOrderDAO aiDao, Integer uGroupID, String docLang, Integer CBUID,Integer years)
        throws Exception
    {
        final List<SPositiveFinancialWithoutCancel> result = new LinkedList<>();
        final SPositiveFinancialOptOutFlagGroup optOutFlag = getPositiveOptOutFlag(CBUID);
        
        String period = getPeriod(years);
        Object[] params = new Object[]{CBUID, period};
        String sql = getString("reportcreator.preparedstatement.credit.positivecreditwithoutcancel");
        
        RowCallbackHandler rowHandler = (ResultSet rst) -> {
            do{
                SPositiveFinancialWithoutCancel cancel = new SPositiveFinancialWithoutCancel();

                int gid = rst.getInt(getString(
                            "report.table.prod_credit.columns.groupid"));

                cancel.setCBUID(CBUID);
                cancel.setSubmittedBy(aiDao.getAIName(uGroupID,gid, docLang ,CBUID));
                cancel.setPeriod(rst.getString(getString(
                            "report.table.prod_credit.columns.period")));

                cancel.setRevolvingFacilityLimit(rst.getBigDecimal(
                        getString(
                            "report.table.prod_credit.columns.revocationfacilitylimit")));
                String str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.revocationshared3rdparty"));
                cancel.setRevolvingShared3rdParty(((null == str_temp)
                        || ("0".equals(str_temp))) ? false : true);

                cancel.setNonRevolvingFacilityLimit(rst.getBigDecimal(
                        getString(
                            "report.table.prod_credit.columns.nonrevocationfacilitylimit")));
                str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.nonrevocationshared3rdparty"));
                cancel.setNonRevolvingShared3rdParty(((null == str_temp)
                        || ("0".equals(str_temp))) ? false : true);

                cancel.setContigentFacilityLimit(rst.getBigDecimal(
                        getString(
                            "report.table.prod_credit.columns.contigentfacilitylimit")));
                str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.contigentshared3rdparty"));
                cancel.setContigentShared3rdParty(((null == str_temp)
                        || ("0".equals(str_temp))) ? false : true);

                cancel.setHPLeasingFacilityLimit(rst.getBigDecimal(
                        getString(
                            "report.table.prod_credit.columns.hpleasingfacilitylimit")));
                str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.hpleasingshared3rdparty"));
                cancel.setHPLeasingShared3rdParty(((null == str_temp)
                        || ("0".equals(str_temp))) ? false : true);

                cancel.setCombinedFacilityLimit(rst.getBigDecimal(
                        getString(
                            "report.table.prod_credit.columns.combinedfacilitylimit")));
                str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.combinedshared3rdparty"));
                cancel.setCombinedShared3rdParty(((null == str_temp)
                        || ("0".equals(str_temp))) ? false : true);
                cancel.setCombinedLimitDetails(rst.getString(
                        getString(
                            "report.table.prod_credit.columns.combinedlimitdetails")));

                cancel.setTangibleSecurity(rst.getString(
                        getString(
                            "report.table.prod_credit.columns.tangiblesecurity")));

                str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.disputedstatus"));
                
                boolean isDispute = ("1".equals(str_temp));
                str_temp = rst.getString(getString(
                            "report.table.prod_credit.columns.creditdisputedstatus"));
                isDispute = isDispute || ("1".equals(str_temp));
                cancel.setDisputedStatus(isDispute);

                //If there is dispute at AI level then use dispute date at AI level
                if ("1".equals(str_temp))                
                    cancel.setDisputedDate(rst.getDate(
                        getString("report.table.prod_credit.columns.flagdate"))
                    );
                
                else if (isDispute)
                    cancel.setDisputedDate(rst.getDate(
                        getString("report.table.prod_credit.columns.disputeddate"))
                    );

                cancel.setRevolvingOptOutFlag(optOutFlag.isRevolvingOptOut(cancel.getPeriod(), String.valueOf(gid)));
                cancel.setNonRevolvingOptOutFlag(optOutFlag.isNonRevolvingOptOut(cancel.getPeriod(), String.valueOf(gid)));
                cancel.setContigentOptOutFlag(optOutFlag.isContingentOptOut(cancel.getPeriod(), String.valueOf(gid)));
                cancel.setHPLeasingOptOutFlag(optOutFlag.isHpLeasingOptOut(cancel.getPeriod(), String.valueOf(gid)));
                cancel.setCombinedOptOutFlag(optOutFlag.isCombinedOptOut(cancel.getPeriod(), String.valueOf(gid)));

                if(cancel.getRevolvingFacilityLimit() != null 
                    && cancel.getRevolvingFacilityLimit().equals(ZERO) && cancel.getRevolvingOptOutFlag())
                        cancel.setRevolvingFacilityLimit(null);                    
                
                if(cancel.getNonRevolvingFacilityLimit() != null 
                    && cancel.getNonRevolvingFacilityLimit().equals(ZERO) && cancel.getNonRevolvingOptOutFlag())
                        cancel.setNonRevolvingFacilityLimit(null);                    
                
                if(cancel.getContigentFacilityLimit() != null
                    && cancel.getContigentFacilityLimit().equals(ZERO) && cancel.getContigentOptOutFlag())
                        cancel.setContigentFacilityLimit(null);
                                    
                if(cancel.getHPLeasingFacilityLimit() != null 
                    && cancel.getHPLeasingFacilityLimit().equals(ZERO) && cancel.getHPLeasingOptOutFlag())
                        cancel.setHPLeasingFacilityLimit(null);                    
                
                result.add(cancel);
            }while (rst.next());
        };

        jdbcTemplate.query(sql, rowHandler, params);
        return result.toArray(SPositiveFinancialWithoutCancel[]::new);
    }


    public SPositiveFinancials getPositiveFinancials(Integer userGroupID,
        AIOrderDAO aiDao, Integer CBUID, String docLang, Integer years)
        throws Exception
    {
        if (null == CBUID)
            throw new GenericException("not have CBUID");

        SPositiveFinancials pFinancial = new SPositiveFinancials(docLang);
        {
            SPositiveFinancialWithoutCancel[] pWOCancels =
                this.getPositiveCreditWithOutCancel(aiDao, userGroupID, docLang,
                    CBUID, years);
            for (var pWOCancel : pWOCancels)
                pFinancial.addPositiveWithoutCancel(pWOCancel);
        }        
        {
            SPositiveFinancialWithCancel[] pWCancels =
            this.getPositiveCreditWithCancel(aiDao, userGroupID, docLang,
                CBUID,years);
            for(var pWCancel : pWCancels)
                pFinancial.addPositiveWithCancel(pWCancel);
        }
        return pFinancial;
    }

    public SPositiveFinancialOptOutFlagGroup getPositiveOptOutFlag(Integer CBUID)
        throws Exception
    {
        final SPositiveFinancialOptOutFlagGroup result = new SPositiveFinancialOptOutFlagGroup();
        
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.credit.positivecreditoptoutflag"),
            (ResultSet rst) -> {
                do{
                    String periodData = rst.getString(getString(
                        "report.table.oms_detail_access.period"));
                    String groupID = rst.getString(getString(
                        "report.table.oms_detail_access.groupid"));
                    String loanType = rst.getString(getString(
                        "report.table.oms_detail_access.loantype"));
                    result.addOptOutFlag(periodData, groupID, loanType);
                }while (rst.next());
            },
            new Object[]{CBUID, getPeriod(2)}
        );       

        return result;
    }
}
