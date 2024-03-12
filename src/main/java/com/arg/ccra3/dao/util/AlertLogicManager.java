package com.arg.ccra3.dao.util;

import org.springframework.jdbc.core.JdbcTemplate;

import static com.arg.ccra3.dao.util.SQLParseUtil.*;
import static com.arg.ccra3.dao.util.OIStatement.*;
import com.arg.util.GenericException;

public class AlertLogicManager {

    private AlertLogicManager() {
    }
    
    public static boolean updateAmountResult(
        final JdbcTemplate jdbcTemplate,
        final String amount,
        final String transationid)
    throws GenericException 
    {
        String statement = getString(
            "AlertLogicManager.update.amoutResult",
            new String[] { amount, transationid }
        );

        return jdbcTemplate.update(statement) == 1;
    }


    public static boolean updateMalProduct(
        final JdbcTemplate jdbcTemplate,
        final String companyname,
        final String transationid)
    
    {
        String companyName = getSQLString(companyname);
        String statement = getString(
            "AlertLogicManager.update.companyName",
            new String[] { companyName, transationid }
        );

        return jdbcTemplate.update(statement) == 1;
    }


    public static boolean updateMalProduct(
        final JdbcTemplate jdbcTemplate,
        final String companyname,
        final String transationid,
        final String expenseID)
    
    {
        String companyName = getSQLString(companyname);
        String statement = getString(
            "AlertLogicManager.update.companyNameExpense",
            new String[] { companyName, transationid, expenseID }
        );

        return jdbcTemplate.update(statement) == 1;
    }


    public static boolean updateSpmExpense(
        final JdbcTemplate jdbcTemplate,
        final String expenseID)
    
    {
        String statement = getString(
            "AlertLogicManager.update.MonitoringExpense",
            new String[]{ expenseID }
        );

        return jdbcTemplate.update(statement) == 1;
    }


    public static boolean updateObjIDExpense(
        final JdbcTemplate jdbcTemplate,
        final String objId,
        final String expenseId, final String cbuid)
   
    {
        String statement = getString(
            "AlertLogicManager.update.productUsage",
            new String[]{ objId, expenseId, cbuid }
        );

        return jdbcTemplate.update(statement) == 1;
    }


    public static boolean updateReadFlag(
        final JdbcTemplate jdbcTemplate,
        final String uid,
        final String aiid,
        final String productType)
    
    {
        String statement = getString(
            "AlertLogicManager.update.readflag",
            new String[]{ uid, aiid, productType }
        );

        return jdbcTemplate.update(statement) == 1;
    }
}
