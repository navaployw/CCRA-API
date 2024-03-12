
package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.ReportResource.*;
import static com.arg.ccra3.dao.security.util.ServerConfiguration.getLocale;
import com.arg.cb2.inquiry.data.SCourtLO;
import com.arg.cb2.inquiry.data.SCourts;
import com.arg.cb2.inquiry.data.SDefendantLO;
import com.arg.ccra3.models.TrnJson;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;


public class CourtDAO {
    final Locale locale = getLocale();
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LogManager.getLogger(TrnJson.class);
    public CourtDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    private SDefendantLO[] findOtherDefendant(String actionNO ,String matchKey, String fileNO)
    {
        List<SDefendantLO> result = new LinkedList<>();

        Calendar cal = Calendar.getInstance(locale);
        cal.add(Calendar.YEAR, -2);
        
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.court.otherdefendant"),
            (ResultSet rst) -> {
                do{
                    result.add(new SDefendantLO(
                        rst.getString(getString("report.table.prod_defendant.columns.surnameeng")),
                        rst.getString(getString("report.table.prod_defendant.columns.surnamenat")),
                        rst.getString(getString("report.table.prod_defendant.columns.givennameeng")),
                        rst.getString(getString("report.table.prod_defendant.columns.givennamenat"))
                    ));
                }while (rst.next());
            },
            new Object[]{actionNO, matchKey, fileNO, new java.sql.Date(cal.getTimeInMillis()) }
        );

        return result.toArray(SDefendantLO[]::new);
    }

    
    public SCourts getCourtList(Integer userGroupID, AIOrderDAO aiDao,
        Integer CBUID, String docLang)
    {
        logger.info(userGroupID);
        logger.info(aiDao);

        SCourts courts = new SCourts(docLang);

        Calendar cal = Calendar.getInstance(locale);
        cal.add(Calendar.YEAR, -2);
        
        jdbcTemplate.query(
            getString("reportcreator.preparedstatement.court"),
            (ResultSet rst) -> {
                do{
                    SCourtLO court = new SCourtLO();
                    court.addDefendant(new SDefendantLO(
                        rst.getString(getString("report.table.prod_defendant.columns.surnameeng")),
                        rst.getString(getString("report.table.prod_defendant.columns.surnamenat")),
                        rst.getString(getString("report.table.prod_defendant.columns.givennameeng")),
                        rst.getString(getString("report.table.prod_defendant.columns.givennamenat"))
                    ));
                    court.setCBUID(CBUID);
                    court.setActionDesc(rst.getString(getString("report.table.prod_plaintiff.columns.actiondesctexteng")));
                    court.setActionDescLO(rst.getString(getString("report.table.prod_plaintiff.columns.actiondesctextchn")));
                    court.setAmount(rst.getBigDecimal(getString("report.table.prod_plaintiff.columns.amount")));
                    court.setCause(rst.getString(getString("report.table.prod_plaintiff.columns.causeofactiontexteng")));
                    court.setCauseLO(rst.getString(getString("report.table.prod_plaintiff.columns.causeofactiontextchn")));
                    court.setCurrencyCode(rst.getString(getString("report.table.prod_plaintiff.columns.currencycode")));
                    court.setDate(rst.getDate(getString("report.table.prod_plaintiff.columns.actiondate")));                    
                    court.setPlaintiff(rst.getString(getString("report.table.prod_plaintiff.columns.plaintiff1eng")));
                    court.setPlaintiffLO(rst.getString(getString("report.table.prod_plaintiff.columns.plaintiff1nat")));
                    
                    String fileNO = rst.getString(getString("report.table.prod_plaintiff.columns.filenumber"));
                    String actionNO = rst.getString(getString("report.table.prod_plaintiff.columns.actionno"));
                    String matchKey = rst.getString(getString("report.table.prod_defendant.columns.matchkey"));
                    
                    court.setFileNO(fileNO);
                    SDefendantLO[] others = this.findOtherDefendant(actionNO , matchKey, fileNO);
                    for (SDefendantLO other : others )
                        court.addDefendant(other);

                    courts.addCourt(court);
                }while (rst.next());
            },
            new Object[]{CBUID, new java.sql.Date(cal.getTimeInMillis()) }
        );

        return courts;
    }
}
