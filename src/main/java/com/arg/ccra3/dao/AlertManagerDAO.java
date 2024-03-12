package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.cb2.inquiry.data.AlertRegisterData;
import com.arg.ccra3.dao.util.ReportResource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.jdbc.core.JdbcTemplate;

public class AlertManagerDAO {
    
    private final JdbcTemplate jdbcTemplate;
    
    public AlertManagerDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<String> listRegisterAlert(AlertRegisterData data) {
        String[] params = new String[] {
            data.getCbuid().toString(),
            data.getGroupAiId().toString(),
            data.getProductType().toString(),
            data.getUId().toString()
        };
        String sql = getString( "alert.select.register-alert", params);
        
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    public boolean isDisabledAutoRenew(AlertRegisterData data) {
        String[] params = new String[] {
            data.getCbuid().toString(), 
            data.getUId().toString(), 
            data.getProductType().toString()
        };
        String sql = getString( "alert.select.check-disable-autorenew", params);
        
        return jdbcTemplate.queryForObject(sql, Object.class) != null;
    }
    
    public void reActivatePreviousSubscription(AlertRegisterData data)
    {
        SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

        String sql =
            ReportResource.getString("alert.update.reactivate-previous-subscription",
                new String[] {
                    "'" + dateFormat.format(new Date()) + "'"
                        , data.getCbuid().toString()
                        , data.getUId().toString()
                        , data.getProductType().toString() });

        jdbcTemplate.update(sql);
        
    }
}
