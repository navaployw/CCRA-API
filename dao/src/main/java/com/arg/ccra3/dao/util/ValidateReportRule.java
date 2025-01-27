
package com.arg.ccra3.dao.util;

import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

public class ValidateReportRule {
    JdbcTemplate jdbcTemplate;
    
    public ValidateReportRule(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public int findAmountPastDueInYears(Integer CBUID)
        throws Exception
    {
        int year5 = 5;
        String period5 = InquiryUtilities.getPeriod(year5);
        
        Map<String, Object> row = jdbcTemplate.queryForMap(
            ReportResource.getString("reportcreator.preparedstatement.amountPastDueInYears"),
            new Object[]{CBUID, period5}
        );
        
        if(row != null && !row.isEmpty())
            return Integer.parseInt(row.get("years").toString());
        
        return 0;
    }
}
