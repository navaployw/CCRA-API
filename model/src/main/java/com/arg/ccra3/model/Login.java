/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.model;

/**
 *
 * @author navaployw
 */
public class Login {
    
    private Boolean result;
    private String message;
    private Integer userId;
    private Integer groupId;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "Login{" + "result=" + result + ", message=" + message + ", userId=" + userId + ", groupId=" + groupId + '}';
    }

  

 

   
    
    
    
}
