package com.arg.ccra3.models.security;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "SPM_TRANSACTION")
public class Transaction{
    @Id
    @Column(name = "TRANSACTIONID", nullable = false, updatable = false, insertable = false)
    private Long transactionID = null;
    @Transient
    private Date createdDate = null;
    @Transient
    private Date updatedDate = null;

    @Column(name = "CREATEDBY", nullable = false)
    private Integer createdBy = null;

    @Column(name = "DATAID", nullable = false)
    private Integer dataID = null;

    @Column(name = "GROUPAIID", nullable = false)
    private Integer groupAIID = null;

    @Column(name = "GROUPID", nullable = false)
    private Integer groupID = null;

    @Column(name = "OBJECTID", nullable = false)
    private Integer objectID = null;

    @Column(name = "SESSIONID", nullable = false)
    private Integer sessionID = null;

    @Column(name = "UID", nullable = false)
    private Integer uID = null;

    @Column(name = "UPDATEDBY", nullable = false)
    private Integer updatedBy = null;
    @Transient
    private Long expenseID = null;

    @Column(name = "CHANNEL", nullable = false)
    private String channel = null;

    @Column(name = "AMOUNTRESULT", nullable = false)
    private int amountResult = 0;

    public Date getcreatedDate() {
        return createdDate;
    }

    public void setcreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getupdatedDate() {
        return updatedDate;
    }

    public void setupdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getDataID() {
        return dataID;
    }

    public void setDataID(Integer dataID) {
        this.dataID = dataID;
    }

    public Integer getGroupAIID() {
        return groupAIID;
    }

    public void setGroupAIID(Integer groupAIID) {
        this.groupAIID = groupAIID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getSessionID() {
        return sessionID;
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(Long expenseID) {
        this.expenseID = expenseID;
    }

    public Long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAmountResult() {
        return amountResult;
    }

    public void setAmountResult(int amountResult) {
        this.amountResult = amountResult;
    }
    
    public void setupDate(){
        Date now = new Date(System.currentTimeMillis());
        createdDate = now;
        updatedDate = now;
    }

}
