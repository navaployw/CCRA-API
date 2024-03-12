package com.arg.ccra3.models.api;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "API_SPM_TRANSACTION")
@EntityListeners(AuditingEntityListener.class)
public class TransactionAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTIONID", nullable = false, insertable = false, updatable = false)
    private Long transactionId;
    @Column(name = "SESSIONID", nullable = false)
    private Long sessionId;
    @Column(name = "UID", nullable = false)
    private Long uID;
    @Column(name = "GROUPAIID")
    private Long groupAIId;
    @Column(name = "GROUPID")
    private Long groupId;
    @Column(name = "OBJECTID", nullable = false)
    private Integer objectId;
    @Column(name = "DATAID")
    private Integer dataId = null;
    @Column(name = "CHANNEL", nullable = false)
    private String channel = "A";
    @Column(name = "AMOUNTRESULT", nullable = false)
    private Integer amountResult;
    @Column(name = "CREATEDBY")
    private Long createdBy;

    @CreatedDate
    @Column(name = "CREATEDDATE")
    private Date createdDate;

    @Column(name = "UPDATEDBY")
    private Long updatedBy;
    
    @LastModifiedDate
    @Column(name = "UPDATEDDATE")
    private Date updatedDate;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getuID() {
        return uID;
    }

    public void setuID(Long uID) {
        this.uID = uID;
    }

    public Long getGroupAIId() {
        return groupAIId;
    }

    public void setGroupAIId(Long groupAIId) {
        this.groupAIId = groupAIId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getAmountResult() {
        return amountResult;
    }

    public void setAmountResult(Integer amountResult) {
        this.amountResult = amountResult;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    
}
