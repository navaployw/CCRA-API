package com.arg.ccra3.online.service;

import com.arg.ccra3.models.security.ViewApiUser;
import com.arg.ccra3.dao.repo.ViewApiUserRepositories;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViewApiUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<ViewApiUser> getUserByAICode(String aiCode)  {
        String sql = "SELECT * FROM VIEW_API_USER WHERE AICODE = ?";
        List<ViewApiUser> results = jdbcTemplate.query(sql,
            BeanPropertyRowMapper.newInstance(ViewApiUser.class),aiCode);
        return results; 

    }
    
    public List<ViewApiUser> getUserByAICodeAndUserId(String aiCode,String userId)  {
        String sql = "SELECT * FROM VIEW_API_USER WHERE AICODE = ? and USERID = ?";
        Object[] params = new Object[]{aiCode, userId};
        List<ViewApiUser> results = jdbcTemplate.query(sql,
            BeanPropertyRowMapper.newInstance(ViewApiUser.class),params);
        return results; 

    }
    
    public List<ViewApiUser> getUserByAICodePasswordUserId(String aiCode,String password,String userId)  {
        String sql = "SELECT * FROM VIEW_API_USER WHERE AICODE = ? and USERID = ? and PASSWORD = ?";
        Object[] params = new Object[]{aiCode, userId, password};
         List<ViewApiUser> results = jdbcTemplate.query(sql,
            BeanPropertyRowMapper.newInstance(ViewApiUser.class),params);
        return results; 

    }       
    
    public List<ViewApiUser> getAllUser()  {
        String sql = "SELECT * FROM VIEW_API_USER WHERE FLAG_ACTIVE = true";
        List<ViewApiUser> results = jdbcTemplate.query(sql,
            BeanPropertyRowMapper.newInstance(ViewApiUser.class));
        return results; 

    }      
    
    public List<ViewApiUser> getUserByAIDUserID(Long aID,String userId)  {
        String sql = "SELECT * FROM VIEW_API_USER WHERE AID = ? and USERID = ?";
        Object[] params = new Object[]{aID, userId};
        List<ViewApiUser> results = jdbcTemplate.query(sql,
            BeanPropertyRowMapper.newInstance(ViewApiUser.class),params);
        return results; 

    }
}
