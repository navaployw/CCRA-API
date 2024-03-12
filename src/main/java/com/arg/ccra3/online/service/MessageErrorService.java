/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.online.service;


import com.arg.ccra3.models.MessageError;
import com.arg.ccra3.dao.repo.MessageErrorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class MessageErrorService {
    @Autowired
    private MessageErrorRepository messageRepo;
    
    public List<MessageError> getMessageByCode(String errorCode){

        return messageRepo.findByerrorCode(errorCode);
    }
}
