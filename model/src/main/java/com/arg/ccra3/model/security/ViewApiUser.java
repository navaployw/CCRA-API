package com.arg.ccra3.model.security;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "VIEW_API_USER")
public class ViewApiUser implements Serializable {
    @Id
    @Column(name = "AID")
    private Integer aID;
    
    @Column(name = "UID")
    private Integer uID;
    
    @Column(name = "USERID")
    private String userID;
    
    @Column(name = "GROUPID")
    private String groupID;
    
    @Column(name = "GROUPAIID")
    private String groupAIID;
    
    @Column(name = "PASSWORD")
    private String password;
    
    @Column(name = "AICODE")
    private String aiCode;
    
    @Column(name = "SECRETKEY")
    private String secretKey;
    
    @Column(name = "SECRET_START")
    private Instant secretStart;
    
    @Column(name = "SECRET_END")
    private Instant secretEnd;
    
    @Column(name = "FLAG_ACTIVE")
    private Boolean flagActive;

    public Integer getaID() {
        return aID;
    }

    public void setaID(Integer aID) {
        this.aID = aID;
    }

    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer uID) {
        this.uID = uID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAiCode() {
        return aiCode;
    }

    public void setAiCode(String aiCode) {
        this.aiCode = aiCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Instant getSecretStart() {
        return secretStart;
    }

    public void setSecretStart(Instant secretStart) {
        this.secretStart = secretStart;
    }

    public Instant getSecretEnd() {
        return secretEnd;
    }

    public void setSecretEnd(Instant secretEnd) {
        this.secretEnd = secretEnd;
    }

    public Boolean getFlagActive() {
        return flagActive;
    }

    public void setFlagActive(Boolean flagActive) {
        this.flagActive = flagActive;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupAIID() {
        return groupAIID;
    }

    public void setGroupAIID(String groupAIID) {
        this.groupAIID = groupAIID;
    }

    @Override
    public String toString() {
        return '{' + "aID=" + aID + ", uID=" + uID + ", userID=" + userID + ", groupID=" + groupID + ", groupAIID=" + groupAIID + ", password=" + password + ", aiCode=" + aiCode + ", secretKey=" + secretKey + ", secretStart=" + secretStart + ", secretEnd=" + secretEnd + ", flagActive=" + flagActive + '}';
    }


    
    
}
