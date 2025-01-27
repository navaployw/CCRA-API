/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.model.security;

/**
 *
 * @author navaployw
 */
public class SpmTransaction {
    
    Integer transactionId;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "SpmTransaction{" + "transactionId=" + transactionId + '}';
    }
    
    
    
}
