/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "API_MESSAGE_ERR")
public class MessageError {
    @Id
    @Column(name = "STATUS_CODE")
    Integer statusCode;
    @Column(name ="ERROR_CODE")
    String errorCode;
    @Column(name ="ERROR_MESSAGE")
    String errorMessage;
    @Transient
    Integer transactionId;
    @Transient
    Integer tokenId;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public String toString() {
        return "MessageError{" + "statusCode=" + statusCode + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", transactionId=" + transactionId + ", tokenId=" + tokenId + '}';
    }
    
    
    
}
