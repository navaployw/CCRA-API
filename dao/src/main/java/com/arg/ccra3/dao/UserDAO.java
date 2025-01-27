
package com.arg.ccra3.dao;

import ch.qos.logback.classic.Logger;
import static com.arg.cb2.inquiry.PreferLanguage.*;
import com.arg.ccra3.dao.repo.UserAPIRepo;
import com.arg.ccra3.model.User;
import java.sql.ResultSet;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


public class UserDAO {
    
    private final JdbcTemplate jdbcTemplate;
    
    private final Logger logger = (Logger) LoggerFactory.getLogger(UserDAO.class);
    
    public UserDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public User getUserFromUserID(String userID){
        User user = new User();
        user.setUserID(userID);
        
        final String SQL = "select * from spm_user u " +
            "inner join SPM_GROUP (nolock) g on g.GROUPID = u.GROUPID " +
            "left join base_country bc on bc.id = g.COUNTRY " +
            "where u.USERID = ?";
        
        jdbcTemplate.query(
            SQL,
            (ResultSet rst) -> {
                do{
                    user.setuID(rst.getInt("UID"));
                    user.setAddressEng1(rst.getString("ADDRESS_1"));
                    user.setAddressEng2(rst.getString("ADDRESS_2"));
                    user.setAddressEng3(rst.getString("ADDRESS_3"));
                    user.setCountry(rst.getString("DESCRIPTION_EN"));
                    user.setProvinceEnglish(rst.getString("PROVINCE"));
                    user.setCityEnglish(rst.getString("CITY"));
                    user.setAreaCode(rst.getString("POST_CODE"));
                    user.setPreferLanguage(rst.getString("PREFERREDLANGUAGE").equals("L") ? CHINESE : ENGLISH);
                    if(!rst.next()){
                        break;                    
                    }
                }while (rst.next());
            },
            userID
        );
        
        return user;
    }
}
