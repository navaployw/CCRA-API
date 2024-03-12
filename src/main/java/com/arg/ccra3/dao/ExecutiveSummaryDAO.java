
package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.InquiryUtilities.*;
import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.ccra3.dao.util.RevocationSQLUtil;
import static com.arg.ccra3.dao.security.util.ServerConfiguration.getLocale;
import com.arg.cb2.inquiry.data.SDispute;
import com.arg.cb2.inquiry.data.SEvidanceOfCourt;
import com.arg.cb2.inquiry.data.SExecutiveSummary;
import com.arg.cb2.inquiry.data.SNoticeOfConsent;
import com.arg.cb2.inquiry.data.SResubmitted;
import com.arg.cb2.inquiry.data.SUnloaded;
import com.arg.cb2.inquiry.data.SUpdated;
import com.arg.cb2.inquiry.util.HistoryRevocationData;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class ExecutiveSummaryDAO {
    private static final Logger logger = LogManager.getLogger(ExecutiveSummaryDAO.class);
    private static final String DYNAMIC_COLUMNS_TOTAL = "report.dynamic.columns.total";
    final Locale locale = getLocale();
    
    private final JdbcTemplate jdbcTemplate;
    
    public ExecutiveSummaryDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public SExecutiveSummary getExecutiveSummary(Integer userGroupID,
        AIOrderDAO aiDao, Integer CBUID, String docLang, Integer years){

        SExecutiveSummary summary = new SExecutiveSummary(docLang);
        SingleRowCreditInfo info = getCreditInfo(CBUID,years);
        
        if (null != info)
        {
            if(info.getTotalAIsReportNumber() == null)
                summary.setAIsReportNumber(0);
            else
                summary.setAIsReportNumber(info.getTotalAIsReportNumber());
            
            summary.setFirstReportDate(info.getFirstReportDate());            
            summary.setAIsReportPeriod(info.getTotalAIsReportPeriod());
            summary.setRecoveredAmount(info.getRecoveredAmount());
            summary.setRecoveredPeriod(info.getRecoveredPeriod());
            summary.setTotalPastDueAmount60Days(info.getTotalPastDueAmount60Days());
            summary.setTotalPastDueAmount60DaysPeriod(info.getTotalPastDueAmount60DaysPeriod());
            
            logger.info(" summary.getNoticeOfRevocationOfConsent(): " + summary.getNoticeOfRevocationOfConsent());
            logger.info(" summary.getEvidanceOfCourt(): " + summary.getEvidanceOfCourt());
            logger.info(" summary.getDispute(): " + summary.getDispute());
            logger.info(" summary.getResubmitted(): " + summary.getResubmitted());
            logger.info(" summary.getUnloaded(): " + summary.getUnloaded());
            logger.info(" summary.getUpdated(): " + summary.getUpdated());
        }
        
        SNoticeOfConsent[] consents = this.getRevocationOfConsent(aiDao, userGroupID, docLang, CBUID, years);
        for (SNoticeOfConsent it : consents)
            summary.addNoticeOfRevocationOfConsent(it);

        SEvidanceOfCourt[] courts = this.getEvidanceOfCourt(CBUID);
        for (SEvidanceOfCourt it : courts)
            summary.addEvidanceOfCourt(it);

        SDispute[] disputes = this.getDispute(aiDao, userGroupID, docLang, CBUID, years);
        for (SDispute it : disputes)
            summary.addDispute(it);

        SResubmitted[] resubmitted = this.getResubmitted(aiDao, userGroupID, docLang, CBUID, years);
        for (SResubmitted it : resubmitted)
            summary.addResubmitted(it);

        SUnloaded[] unloaded = this.getUnloaded(aiDao, userGroupID, docLang, CBUID, years);
        for (SUnloaded it : unloaded)
            summary.addUnloaded(it);

        SUpdated[] updated = this.getUpdated(aiDao, userGroupID, docLang, CBUID, years);
        for (SUpdated sUpdated : updated)
            summary.addUpdated(sUpdated);
        
        return summary;
    }
    
    private SNoticeOfConsent[] getRevocationOfConsent(AIOrderDAO aiDao,
        Integer uGroupID, String docLang, Integer cbuid, int years)
    {
        Calendar cal = Calendar.getInstance(locale);
        cal.add(Calendar.YEAR, -2);

        List<HistoryRevocationData> revocationList =
            RevocationSQLUtil.selectHistoryRevocation(jdbcTemplate, cbuid, new Date(cal.getTimeInMillis()),years);

        List<SNoticeOfConsent> results = new ArrayList<>();
        for (HistoryRevocationData it : revocationList){
            int gid = Integer.parseInt(it.getGroupID());
            results.add(new SNoticeOfConsent(cbuid,
                    aiDao.getAIName(uGroupID, gid, docLang ,cbuid),
                    it.getRevocNoticedDate(),
                    it.getRevocEffectDate()
            ));
        }
        
        return results.toArray(SNoticeOfConsent[]::new);
    }
    
    private SEvidanceOfCourt[] getEvidanceOfCourt(int cbuid)
    {
        Calendar cal = Calendar.getInstance(locale);
        cal.add(Calendar.YEAR, -2);
        
        final List<SEvidanceOfCourt> results = new LinkedList<>();
        
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.executivesummary.court"),
            (ResultSet rst) -> {
                do {
                    results.add(new SEvidanceOfCourt(
                        rst.getInt(getString(DYNAMIC_COLUMNS_TOTAL)),
                        rst.getDate(getString("report.dynamic.columns.date"))
                    ));
                }while (rst.next());
            },
            new Object[]{cbuid, new java.sql.Date(cal.getTimeInMillis()) }
        );
        
        return results.toArray(SEvidanceOfCourt[]::new);
    }
    
    private SDispute[] getDispute(
        AIOrderDAO aiDao, Integer uGroupID,
        String docLang, Integer cbuid, int years
    ) 
    {
        final List<SDispute> results = new LinkedList<>();
        
        String period = getPeriod(years);
        
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.executivesummary.dispute"),
            (ResultSet rst) -> {
                do {
                    int gid = rst.getInt(getString("report.table.prod_credit.columns.groupid"));
                    String creditDisputedStatus = rst.getString(getString("report.table.prod_credit.columns.creditdisputedstatus"));

                    Date date = "1".equals(creditDisputedStatus) ? 
                            rst.getDate(getString("report.table.prod_credit.columns.flagdate")) : 
                            rst.getDate(getString("report.table.prod_credit.columns.disputeddate"));

                    results.add(new SDispute(
                        cbuid,
                        aiDao.getAIName(uGroupID, gid,docLang , cbuid),
                        date
                    ));
                }while (rst.next());
            },
            new Object[]{cbuid, period, period}
        );
        return results.toArray(SDispute[]::new);
    }
    
    private SResubmitted[] getResubmitted(AIOrderDAO aiDao,
        Integer uGroupID, String docLang, Integer cbuid, int years)
        
    {
        final List<SResubmitted> results = new LinkedList<>();
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.executivesummary.resubmitted"),
            (ResultSet rst) -> {
                do{
                    int gid = rst.getInt(getString("report.table.prod_unload.columns.groupid"));            
                    results.add(new SResubmitted(cbuid,
                        aiDao.getAIName(uGroupID, gid, docLang , cbuid),
                        rst.getString(getString("report.table.prod_unload.columns.period")),
                        rst.getDate(getString("report.table.prod_unload.columns.date"))
                    ));
                }while (rst.next());
            },
            new Object[]{cbuid, getPeriod(years) }
        );
        return results.toArray(SResubmitted[]::new);
    }
    
    private SUnloaded[] getUnloaded(AIOrderDAO aiDao, Integer uGroupID,
        String docLang, Integer cbuid, int years)
        
    {
        List<SUnloaded> results = new LinkedList<>();
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.executivesummary.unloaded"),
            (ResultSet rst) -> {
                do{
                    int gid = (int)rst.getInt(getString("report.table.prod_unload.columns.groupid"));
                    results.add(new SUnloaded(cbuid,
                        aiDao.getAIName(uGroupID, gid, docLang , cbuid),
                        rst.getString(getString("report.table.prod_unload.columns.period")),
                        rst.getDate(getString("report.table.prod_unload.columns.date"))
                    ));
                }while (rst.next());
            },
            new Object[]{cbuid, getPeriod(years) }
        );
        return results.toArray(SUnloaded[]::new);
    }
    
    private SUpdated[] getUpdated(AIOrderDAO aiDao, Integer uGroupID,
        String docLang, Integer cbuid, int years)
        
    {
        final List<SUpdated> results = new LinkedList<>();
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.executivesummary.updated"),
            (ResultSet rst) -> {
                do{
                    int gid = rst.getInt(getString("report.table.update.columns.groupid"));
                    results.add(new SUpdated(cbuid,
                        aiDao.getAIName(uGroupID, gid, docLang ,cbuid),
                        rst.getString(getString("report.table.update.columns.period")),
                        rst.getDate(getString("report.table.update.columns.date"))
                    ));
                }while (rst.next());
            },
            new Object[]{cbuid, getPeriod(years) }
        );
        return results.toArray(SUpdated[]::new);
    }

    
    
    
    
    public SingleRowCreditInfo getCreditInfo(int cbuid, int years) 
    {
        
        String period = getPeriod(years);
        final SingleRowCreditInfo info = new SingleRowCreditInfo();
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.executivesummary.single-row-information"),
            (ResultSet rst) -> {
                do{
                    info.setCBUID(rst.getInt(1));
                    info.setFirstReportDate(rst.getString(2));
                    info.setTotalAIsReportPeriod(rst.getString(3));
                    info.setTotalAIsReportNumber(rst.getInt(4));
                    info.setTotalPastDueAmount60Days(rst.getBigDecimal(5));
                    info.setTotalPastDueAmount60DaysPeriod(rst.getString(6));
                    info.setRecoveredAmount(rst.getBigDecimal(7));
                    info.setRecoveredPeriod(rst.getString(8));
                   
                }while(rst.next());
            },
            new Object[]{cbuid, period, cbuid, period, cbuid, period }
        );
        
        return info;
    }
  
    
    
    public TotalAmountPastDue60Days getTotalAmountPastDue60(int cbuid) 
    {
        Map<String, Object> rst = jdbcTemplate.queryForMap(
            getString("reportcreator.preparedstatement.executivesummary.totalpastdue60days"),
            new Object[]{ cbuid, getPeriod(2) }
        );
        
        TotalAmountPastDue60Days totalAmount = null;
        if (!rst.isEmpty())
            totalAmount = new TotalAmountPastDue60Days(
                (String) rst.get(getString("report.table.prod_demographic_flag.columns.period")),
                (BigDecimal)rst.get(getString(DYNAMIC_COLUMNS_TOTAL))
            );
        return totalAmount;
    }
    public TotalRecoverAmount getTotalRecoverAmount(int cbuid)
        
    {
        Map<String, Object> rst = jdbcTemplate.queryForMap(
            getString("reportcreator.preparedstatement.executivesummary.totalrecoveredamount"),
            new Object[]{ cbuid, getPeriod(2) }
        );
        
        TotalRecoverAmount totalAmount = null;
        if (!rst.isEmpty())
            totalAmount = new TotalRecoverAmount(
                (String)rst.get(getString("report.table.prod_demographic_flag.columns.period")),
                (BigDecimal)rst.get(getString(DYNAMIC_COLUMNS_TOTAL))
            );
        return totalAmount;
    }
    

    

    class TotalAIsReportData{
        String period;
        int totalAIs = 0;

        public TotalAIsReportData(String period, int totalAIs)
        {
            this.period = period;
            this.totalAIs = totalAIs;
        }
        public String getPeriod()
        {
            return period;
        }
        public int getTotalAIs()
        {
            return totalAIs;
        }
    }
    class TotalAmountPastDue60Days{
        BigDecimal amount = null;
        String period = null;

        public TotalAmountPastDue60Days(String period, BigDecimal amount)
        {
            this.period = period;
            this.amount = amount;
        }
        public BigDecimal getAmount()
        {
            return amount;
        }
        public String getPeriod()
        {
            return period;
        }
    }
    class TotalRecoverAmount{
        BigDecimal amount = null;
        String period = null;

        public TotalRecoverAmount(String period, BigDecimal amount)
        {
            this.period = period;
            this.amount = amount;
        }
        public BigDecimal getAmount()
        {
            return amount;
        }
        public String getPeriod()
        {
            return period;
        }
    }

    private class SingleRowCreditInfo
    {
        private String firstReportDate;
        private Integer CBUID;
        private String totalAIsReportPeriod;
        private Integer totalAIsReportNumber;
        private BigDecimal totalPastDueAmount60Days;
        private String totalPastDueAmount60DaysPeriod;
        private BigDecimal recoveredAmount;
        private String recoveredPeriod;
        public String getFirstReportDate()
        {
            return firstReportDate;
        }

        public void setFirstReportDate(String firstReportDate)
        {
            this.firstReportDate = firstReportDate;
        }

        public Integer getCBUID()
        {
            return CBUID;
        }

        public void setCBUID(Integer CBUID)
        {
            this.CBUID = CBUID;
        }

        public String getTotalAIsReportPeriod()
        {
            return totalAIsReportPeriod;
        }

        public void setTotalAIsReportPeriod(String totalAIsReportPeriod)
        {
            this.totalAIsReportPeriod = totalAIsReportPeriod;
        }

        public Integer getTotalAIsReportNumber()
        {
            return totalAIsReportNumber;
        }

        public void setTotalAIsReportNumber(Integer totalAIsReportNumber)
        {
            this.totalAIsReportNumber = totalAIsReportNumber;
        }

        public BigDecimal getTotalPastDueAmount60Days()
        {
            return totalPastDueAmount60Days;
        }

        public void setTotalPastDueAmount60Days(BigDecimal totalPastDueAmount60Days)
        {
            this.totalPastDueAmount60Days = totalPastDueAmount60Days;
        }

        public String getTotalPastDueAmount60DaysPeriod()
        {
            return totalPastDueAmount60DaysPeriod;
        }

        public void setTotalPastDueAmount60DaysPeriod(
                String totalPastDueAmount60DaysPeriod)
        {
            this.totalPastDueAmount60DaysPeriod = totalPastDueAmount60DaysPeriod;
        }

        public BigDecimal getRecoveredAmount()
        {
            return recoveredAmount;
        }

        public void setRecoveredAmount(BigDecimal recoveredAmount)
        {
            this.recoveredAmount = recoveredAmount;
        }

        public String getRecoveredPeriod()
        {
            return recoveredPeriod;
        }

        public void setRecoveredPeriod(String recoveredPeriod)
        {
            this.recoveredPeriod = recoveredPeriod;
        }

        public String toString()
        {
            return "SingleRowCreditInfo{" +
                    "firstReportDate='" + firstReportDate + '\'' +
                    ", CBUID=" + CBUID +
                    ", totalAIsReportPeriod='" + totalAIsReportPeriod + '\'' +
                    ", totalAIsReportNumber=" + totalAIsReportNumber +
                    ", totalPastDueAmount60Days=" + totalPastDueAmount60Days +
                    ", totalPastDueAmount60DaysPeriod='" + totalPastDueAmount60DaysPeriod + '\'' +
                    ", recoveredAmount=" + recoveredAmount +
                    ", recoveredPeriod='" + recoveredPeriod + '\'' +
                    '}';
        }
    }

}
