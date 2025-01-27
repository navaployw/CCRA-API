package com.arg.ccra3.dao.test.sqlString;

import com.arg.cb2.inquiry.ErrorMessages;
import com.arg.ccra3.dao.util.ReportResource;
import com.arg.ccra3.dao.util.OIStatement;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("sqlString")
@SpringBootTest
public class sqlStringTest {

    @Test
    void testErrorMsg(){
        assertEquals(ErrorMessages.getString("ICE-A00004"), "lol");
    }
    
    @ParameterizedTest
    @MethodSource("com.arg.ccra3.dao.test.sqlString.sqlStringTest#getOIStatementTestParam")
    public void OIStatementTest(String property, String[] params, String expectedSql){
        final String sql = OIStatement.getString(property, params);
        assertEquals(sql, expectedSql);
    }

    @ParameterizedTest
    @MethodSource("com.arg.ccra3.dao.test.sqlString.sqlStringTest#getReportResourceTesttParam")
    public void ReportResourceTest(String property, String[] params, String expectedSql){
        final String sql = ReportResource.getString(property, params);
        assertEquals(sql, expectedSql);
    }

    static List<Arguments> getOIStatementTestParam(){
        return List.of(
            Arguments.of("AlertLogicManager.update.amoutResult",
                new String[] { "6", "7" },
                "UPDATE spm_transaction SET amountresult = 6 WHERE transactionid = 7"
            ),
            Arguments.of("AlertLogicManager.update.companyName",
                new String[] { "companyName", "6" },
                "UPDATE mal_productdeliver SET companyname = 'companyName' WHERE transactionid =6"
            ),
            Arguments.of("AlertLogicManager.update.companyNameExpense",
                new String[] { "companyName", "6", "7" },
                "UPDATE mal_productdeliver SET companyname = 'companyName', expenseid = 7 WHERE transactionid =6"
            ),
            Arguments.of("AlertLogicManager.update.MonitoringExpense",
                new String[] { "6" },
                "UPDATE spm_expense SET disabled = 0 WHERE expenseid = 6"
            ),
            Arguments.of("AlertLogicManager.update.productUsage",
                new String[] { "1", "2", "3" },
                "UPDATE spm_expense SET productusage = 1, cbuid = 3 WHERE expenseid=2"
            ),
            Arguments.of("AlertLogicManager.update.readflag",
                new String[] { "6", "7", "8" },
                "update prod_login_alert set read_flag =1, updatedby = 6, updateddate = getdate() where groupid = 7 and login_alert_type = 8"
            )
        );
    }
    
    static List<Arguments> getReportResourceTesttParam(){
        return List.of(
            Arguments.of("search.name.english",
                new String[] { "10", "name" },
                "select top 10 master_name_english.duns_no, master_name_english.english_name company_name_en, master_dunstable.company_name_lo company_name_lo, name_type, district, master_name_english.cbuid, master_dunstable.disputestatus, master_dunstable.disputedate, master_dunstable.disputereason, master_dunstable.disputeby from master_name_english with (nolock), master_dunstable with (nolock) where master_name_english.duns_no = master_dunstable.duns_no and master_name_english.english_name >= 'name' order by english_name asc"
            ),
            Arguments.of("search.name.local",
                new String[] { "10", "name" },
                "select top 10 master_name_local.duns_no, master_name_local.local_name company_name_lo, master_dunstable.company_name_eng company_name_en, name_type, district, master_name_local.cbuid, master_dunstable.disputestatus, master_dunstable.disputedate, master_dunstable.disputereason, master_dunstable.disputeby from master_name_local with (nolock), master_dunstable with (nolock) where master_dunstable.duns_no = master_name_local.duns_no and local_name >= N'name' order by local_name asc"
            )
        );
    }
}
