package com.arg.ccra3.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setuID(rs.getInt("UID"));
        user.setUserID(rs.getString("USERID"));
        
        return user;
    }
}
