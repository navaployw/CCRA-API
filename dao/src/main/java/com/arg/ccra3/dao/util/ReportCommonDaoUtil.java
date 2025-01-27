
package com.arg.ccra3.dao.util;

import com.arg.cb2.DeadlockAwareTask;
import com.arg.cb2.DeadlockUtil;
import com.arg.cb2.inquiry.BaseCreditType;
import com.arg.cb2.inquiry.BaseReasonCode;
import com.arg.cb2.inquiry.BaseReasonCodes;
import com.arg.cb2.inquiry.BaseTangibleSecurity;
import com.arg.cb2.inquiry.CreditType;
import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.ReasonCode;
import com.arg.cb2.inquiry.TangibleSecurity;
import com.arg.ccra3.dao.ReportCreatorDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.LogManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class ReportCommonDaoUtil {
    
    private static final org.apache.log4j.Logger logger = LogManager.getLogger(ReportCommonDaoUtil.class);
            
    public static BaseTangibleSecurity getTangibleSecurity(JdbcTemplate jdbcTemplate)
    {
        final String SQL =
            "SELECT id, description_en, description_lo "
            + "FROM base_securitytype WITH (NOLOCK) ORDER BY description_en";

        final BaseTangibleSecurity baseTangibleSecurity = new BaseTangibleSecurity();
        
        jdbcTemplate.query(
            SQL,
            (ResultSet rst) -> {
                do{
                    String ID;
                    try {
                        ID = rst.getString("ID");
                        baseTangibleSecurity.addTangibleSecurity(ID,
                        new TangibleSecurity(ID, rst.getString("DESCRIPTION_EN"),
                            rst.getString("DESCRIPTION_LO")));
                    } catch (SQLException ex) {
                        logger.error(ex.getMessage());
                    }
                }while (rst.next());
            }
        );

        return baseTangibleSecurity;
    }
    
    public static String getReasonCode(JdbcTemplate jdbcTemplate, Integer id, PreferLanguage userpreLang)
    {
        final String SQL =
            "SELECT ID, DESCRIPTION_EN, DESCRIPTION_LO FROM base_reason where ID = ?";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(
            SQL,
            id
        );
        
        if(results.isEmpty())
            return null;
        
        Map<String, Object> code = results.get(0);
        return (String) (PreferLanguage.ENGLISH.equals(userpreLang) ? 
            code.get("DESCRIPTION_EN") : code.get("DESCRIPTION_LO"));
    }
    
    public static BaseCreditType getCreditType(JdbcTemplate jdbcTemplate)
    {
        final String SQL =
            "SELECT id, description_en, description_lo "
            + "FROM base_credittype WITH (NOLOCK) ORDER BY description_en";

        BaseCreditType baseCreditType = new BaseCreditType();
        
        jdbcTemplate.query(
            SQL,
            (ResultSet rst) -> {
                do{
                    try {
                        String ID = rst.getString("ID");

                        baseCreditType.addCreditType(ID,
                            new CreditType(ID, rst.getString("DESCRIPTION_EN"),
                                rst.getString("DESCRIPTION_LO")));
                    } catch (SQLException ex) {
                        Logger.getLogger(ReportCreatorDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }while (rst.next());
            }
        );

        return baseCreditType;
    }
    
    public static BaseReasonCodes getBaseReasonCodes(JdbcTemplate jdbcTemplate)
        throws Exception
    {
        final String SQL =
            "SELECT id, type, description_en, description_lo "
            + "FROM base_reason WITH (NOLOCK)";

        BaseReasonCodes baseReasonCode = new BaseReasonCodes();
        
        jdbcTemplate.query(
            SQL,
            (ResultSet rst) -> {
                do{
                    String reasonType = rst.getString("TYPE");
                    String reasonCode = rst.getString("ID");

                    baseReasonCode.addReasonCode(reasonType, reasonCode,
                        new ReasonCode(reasonCode, rst.getString("DESCRIPTION_EN"),
                            rst.getString("DESCRIPTION_LO")));
                }while (rst.next());
            }
        );
        
        return baseReasonCode;
    }
    
    public BaseReasonCode getReasonCode(JdbcTemplate jdbcTemplate, String reasonType)
    {
        BaseReasonCode bReasonCode = new BaseReasonCode();

        String SQL =
            "SELECT id, type, description_en, description_lo "
            + "FROM base_reason WITH (NOLOCK) WHERE type = ? ORDER BY id ASC";
        
        jdbcTemplate.query(
            SQL,
            (ResultSet rst) -> {
                do{
                    String ID = rst.getString("ID");

                    bReasonCode.addReasonCode(ID,
                        new ReasonCode(ID, rst.getString("DESCRIPTION_EN"),
                            rst.getString("DESCRIPTION_LO")));
                }while (rst.next());
            },
            reasonType
        );
        
        return bReasonCode;
    }
}
