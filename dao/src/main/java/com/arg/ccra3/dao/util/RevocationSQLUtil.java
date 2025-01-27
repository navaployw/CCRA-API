
package com.arg.ccra3.dao.util;

import com.arg.cb2.inquiry.util.HistoryRevocationData;
import com.arg.cb2.inquiry.util.RevocationStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
public class RevocationSQLUtil {
    
    private RevocationSQLUtil(){}
    

    protected static String getPeriod(int year){
        return InquiryUtilities.getPeriod(year);
    }
    
    public static List<HistoryRevocationData> selectHistoryRevocation(JdbcTemplate jdbcTemplate, int cbuid,
        java.sql.Date effectDate,int years)
    {        
        String period = getPeriod(years);            
        final List<HistoryRevocationData> ret = new LinkedList<>();
        jdbcTemplate.query(
            RevocationStatement.getString("select.history.revocation.of.consent"),
            (ResultSet rs) -> {
                do{
                    ret.add(new HistoryRevocationData(
                        0, rs.getString("groupid"),
                        rs.getString("group_name_en"), rs.getString("group_name_lo"), 
                        rs.getDate("revoc_noticed_date"), rs.getDate("revoc_effect_date"),
                        null, null
                    ));
                }while (rs.next());
            },
            new Object[]{period, cbuid, effectDate, cbuid, effectDate, period}
        );
        return ret;
    }
}
