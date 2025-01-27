package com.arg.ccra3.dao.util;

import org.springframework.jdbc.core.JdbcTemplate;

import static com.arg.ccra3.dao.util.SQLParseUtil.*;
import static com.arg.ccra3.dao.util.OIStatement.*;

public class AlertLogicManager {
    public static boolean updateAmountResult(
        final JdbcTemplate jdbcTemplate,
        final String amount,
        final String transationid)
    throws Exception 
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
    throws Exception
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
    throws Exception
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
    throws Exception
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
    throws Exception
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
    throws Exception
    {
        String statement = getString(
            "AlertLogicManager.update.readflag",
            new String[]{ uid, aiid, productType }
        );

        return jdbcTemplate.update(statement) == 1;
    }
}
