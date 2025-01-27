
package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.InquiryUtilities.*;
import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.cb2.inquiry.data.SNegativeFinancial;
import com.arg.cb2.inquiry.data.SNegativeFinancials;
import com.arg.cb2.spm.core.SPMConstants;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;


public class NegativeFinancialDAO {
    
    private final JdbcTemplate jdbcTemplate;

    public NegativeFinancialDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public SNegativeFinancials getNegativeFinancial(Integer userGroupID,
        Integer CBUID, String docLang)
    throws Exception
    {
        if (SPMConstants.GroupContants.CCRA_ID == userGroupID)        
            return getNegativeFinancialNONAI(userGroupID, CBUID, docLang);        

        return getNegativeFinancialAI(userGroupID, CBUID, docLang);
    }

    private SNegativeFinancials getNegativeFinancialNONAI(Integer userGroupID, Integer CBUID, String docLang)
        throws Exception
    {
        SNegativeFinancials neg = new SNegativeFinancials(docLang);

        SNegativeFinancial[] negNYears = this.getNegativeFinancialNYeas(CBUID, 1, userGroupID);
        for(var it : negNYears)
            neg.addNegativePast12Months(it);
        
        negNYears = this.getNegativeFinancialNYeas(CBUID, 2,userGroupID);
        for(var it : negNYears)
            neg.addNegativePast24Months(it);

        negNYears = this.getNegativeFinancialNYeas(CBUID, 5,userGroupID);
        for(var it : negNYears)
            neg.addNegativePast60Months(it);

        return neg;
    }

    private SNegativeFinancials getNegativeFinancialAI(Integer userGroupID,
        Integer CBUID, String docLang)
        throws Exception
    {
        SNegativeFinancials neg = new SNegativeFinancials(docLang);

        SNegativeFinancial[] negNYears = this.getNegativeFinancialNYeas(CBUID, 1,userGroupID);
        for(var it : negNYears)
            neg.addNegativePast12Months(it);

        negNYears = this.getNegativeFinancialNYeas(CBUID, 2,userGroupID);
        for(var it : negNYears)
            neg.addNegativePast24Months(it);

        negNYears = this.getNegativeFinancialNYeas(CBUID, 5,userGroupID);
        for(var it : negNYears)
            neg.addNegativePast60Months(it);

        return neg;
    }

    
    private SNegativeFinancial[] getNegativeFinancialNYeas(Integer CBUID,
        int year,int groupAIID)
        throws Exception
    {
        String startPeriod, endPeriod;
        switch (year) {
            case 2 -> {
                startPeriod = getPeriod(2, 2);
                endPeriod = getPeriod(0, 1);
            }
            case 1 -> {
                startPeriod = getPeriod(1, 1);
                endPeriod = getPeriod(0, 0);
            }
            default -> {
                startPeriod = getPeriod(year,2);
                endPeriod = getPeriod(0,1);
            }
        }

        List<SNegativeFinancial> result = new LinkedList<>();
        
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.negative-financial"),
            (ResultSet rst) -> {
                do {
                    result.add(new SNegativeFinancial(CBUID,
                    rst.getString(getString(
                            "report.table.negative-financial.columns.period")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.groupid")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.maximumamountpastdue60days")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.maximumdayspastdue")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.totalaispastdue")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.totalwriteoff")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.totalaiswriteoff")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.totalrecovery")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.totalaisrecovery")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.totalrevolvingpastdue")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.maximumdaysrevolving")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.totalnonrevolvingpastdue")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.maximumdaysnonrevolving")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.totalcontigentpastdue")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.maximumdayscontigent")),
                    rst.getBigDecimal(getString(
                            "report.table.negative-financial.columns.totalhpleasingpastdue")),
                    rst.getInt(getString(
                            "report.table.negative-financial.columns.maximumdayshpleasing")),
                    (groupAIID == 1)?rst.getString(getString(
                            "report.table.negative-financial.columns.reportBy")):null));
                }while (rst.next());
            },
            new Object[]{CBUID, startPeriod, endPeriod }
        );

        return result.toArray(SNegativeFinancial[]::new);
    }
}
