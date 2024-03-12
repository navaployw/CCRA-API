package com.arg.ccra3.models.api;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "API_PRODUCTDELIVER")
@EntityListeners(AuditingEntityListener.class)
public class MalProductDeliverAPI {
    @Id
    @Column(name = "TRANSACTIONID", nullable = false)
    private Long transactionId;
    @Column(name = "EXPENSEID")
    private Long expenseId;
    @Column(name = "BTHPRODUCTDELIVERID")
    private Long bthProductDeliverId;
    @Column(name = "ROWNUMBER", nullable = false)
    private Long rowNumber;
    @Column(name = "REQUESTTYPE", nullable = false)
    private String requestType;
    @Column(name = "DUNS_NO")
    private Long dunsNo;
    @Column(name = "REASONCODE")
    private Integer reasonCode;
    @Column(name = "AIREFCODE1")
    private String airefcode1;
    @Column(name = "AIREFCODE2")
    private String airefcode2;
    @Column(name = "AIREFCODE3")
    private String airefcode3;
    @Column(name = "OBJECTID")
    private Integer objectId;
    @Column(name = "DELETED", nullable = false)
    private Integer deleted;
    @Column(name = "ERR_CODE")
    private String errCode;
    @Column(name = "ERR_DESC")
    private String errDesc;
    @CreatedDate
    @Column(name = "CREATEDDATE", nullable = false)
    private Date createdDate;
    @Column(name = "COMPANYNAME")
    private String companyName;
    @Column(name = "PREFERREDLANGUAGE", nullable = false)
    private String preferredLanguage;
    @Column(name = "UID", nullable = false)
    private Integer uID;
    @Column(name = "GROUPAIID", nullable = false)
    private Integer groupAIId;
    @Column(name = "GROUPID", nullable = false)
    private Integer groupId;
    @Column(name = "MATCHKEY")
    private Long matchKey;
    @Column(name = "READ_FLAG", nullable = false)
    private Boolean readFlag;
    @Column(name = "DELETED_DATE")
    private Date deletedDate;
    @Column(name = "VERSION")
    private Integer version = 2;
    @Column(name = "WITHIN7DAYS")
    private Boolean within7days;
    @Column(name = "MINORVERSION", nullable = false)
    private Integer minorversion;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public Long getBthProductDeliverId() {
        return bthProductDeliverId;
    }

    public void setBthProductDeliverId(Long bthProductDeliverId) {
        this.bthProductDeliverId = bthProductDeliverId;
    }

    public Long getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Long rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Long getDunsNo() {
        return dunsNo;
    }

    public void setDunsNo(Long dunsNo) {
        this.dunsNo = dunsNo;
    }

    public Integer getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(Integer reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getAirefcode1() {
        return airefcode1;
    }

    public void setAirefcode1(String airefcode1) {
        this.airefcode1 = airefcode1;
    }

    public String getAirefcode2() {
        return airefcode2;
    }

    public void setAirefcode2(String airefcode2) {
        this.airefcode2 = airefcode2;
    }

    public String getAirefcode3() {
        return airefcode3;
    }

    public void setAirefcode3(String airefcode3) {
        this.airefcode3 = airefcode3;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public Integer getGroupAIId() {
        return groupAIId;
    }

    public void setGroupAIId(Integer groupAIId) {
        this.groupAIId = groupAIId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Long getMatchKey() {
        return matchKey;
    }

    public void setMatchKey(Long matchKey) {
        this.matchKey = matchKey;
    }

    public Boolean getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Boolean readFlag) {
        this.readFlag = readFlag;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getWithin7days() {
        return within7days;
    }

    public void setWithin7days(Boolean within7days) {
        this.within7days = within7days;
    }

    public Integer getMinorversion() {
        return minorversion;
    }

    public void setMinorversion(Integer minorversion) {
        this.minorversion = minorversion;
    }

        
}
