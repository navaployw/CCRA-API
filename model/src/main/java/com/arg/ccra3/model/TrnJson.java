/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author navaployw
 */

@Entity
@Table(name = "API_TRN_JSON")
public class TrnJson implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRNAPIID")
    Long trnApiId;
    @Column(name = "AID")
    Long aId;
    @Column(name = "UID")
    Long uId;
    @Column(name = "MODULE")
    Long moduleNo; //1 For get CCRA Token
    @Column(name = "ACCESS_TOKEN")
    String accessToken;
    @Column(name = "JSON_REQUEST")
    String jsonRequest;
    @Column(name = "JSON_RESPONSE")
    String jsonResponse;
    @Column(name = "REQUEST_TIME")
    Date requestTime;
    @Column(name = "RESPONSE_TIME")
    Date responseTime;
    @Column(name = "STATUS_CODE")
    Long statusCode;
    @Column(name = "ERROR_CODE")
    String errorCode;
    @Column(name = "GROUPID")
    Long groupId;
    @Column(name = "TOKENID")
    Long tokenId;
    @Column(name = "TOKENCDIID")
    Long tokenCdiId;
    @Column(name = "EXPENSEID")
    Long expenseId;

    public Long getTrnApiId() {
        return trnApiId;
    }

    public void setTrnApiId(Long trnApiId) {
        this.trnApiId = trnApiId;
    }

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public Long getuId() {
        return uId;
    }

    public void setuId(Long uId) {
        this.uId = uId;
    }

    public Long getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(Long module) {
        this.moduleNo = module;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(String jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public Long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Long statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getTokenCdiId() {
        return tokenCdiId;
    }

    public void setTokenCdiId(Long tokenCdiId) {
        this.tokenCdiId = tokenCdiId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public String toString() {
        return "TrnJson{" + "trnApiId=" + trnApiId + ", aId=" + aId + ", uId=" + uId + ", moduleNo=" + moduleNo + ", accessToken=" + accessToken + ", jsonRequest=" + jsonRequest + ", jsonResponse=" + jsonResponse + ", requestTime=" + requestTime + ", responseTime=" + responseTime + ", statusCode=" + statusCode + ", errorCode=" + errorCode + ", groupId=" + groupId + ", tokenId=" + tokenId + ", tokenCdiId=" + tokenCdiId + ", expenseId=" + expenseId + '}';
    }

    public TrnJson() {
    }
    
    
    
    
    

}
