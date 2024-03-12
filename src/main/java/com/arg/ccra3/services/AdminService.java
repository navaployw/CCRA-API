/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.arg.ccra3.dao.UserDAO;
import com.arg.ccra3.models.User;

@Service
public class AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplateAPI;

    // @Autowired
    // private JWTService jwtService;

    // private final Logger logger = (Logger) LoggerFactory.getLogger(AdminService.class);
    // private String logText ="";
    // private final String USER_NAME ="username";
    // private final String NOPERMISSION ="No Permission";
    // private final String USERID ="userId";

    public User findByUid(Long uid){
        UserDAO adminLoginDAO = new UserDAO(jdbcTemplateAPI);
        return adminLoginDAO.findByUid(uid);
    }
    
}
