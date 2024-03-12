/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
@Table(name = "API_TOKEN_CDI")
public class CdiToken implements Serializable {
    private static final Logger logger = LogManager.getLogger(CdiToken.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKENCDIID")
    private Long tokenCdiId;
    @Column(name = "AID")
    private Long aId;
    @Column(name = "UID")
    private Long uId;
    @Column(name = "CDI_TOKEN")
    private String cdiToken;
    @Column(name = "JTI")
    private String jti;
    @Column(name = "PAYLOAD")
    private String payload;
    @Column(name = "AICODE")
    private String aiCode;
    @Column(name = "CREATE_TIME")
    private Date createTime;
    @Column(name = "EXPIRE_TIME")
    private Date expireTime;
    @Column(name = "TRAN_TIME")
    private Date tranTime;

    public CdiToken() {
        logger.info("CdiToken");
    }

    public Long getTokenCdiId() {
        return tokenCdiId;
    }

    public void setTokenCdiId(Long tokenCdiId) {
        this.tokenCdiId = tokenCdiId;
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

    public String getCdiToken() {
        return cdiToken;
    }

    public void setCdiToken(String cdiToken) {
        this.cdiToken = cdiToken;
    }

    public String getJti() {
        return jti;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getAiCode() {
        return aiCode;
    }

    public void setAiCode(String aiCode) {
        this.aiCode = aiCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getTranTime() {
        return tranTime;
    }

    public void setTranTime(Date tranTime) {
        this.tranTime = tranTime;
    }

    @Override
    public String toString() {
        return "CdiToken{" + "tokenCdiId=" + tokenCdiId + ", aId=" + aId + ", uId=" + uId + ", cdiToken=" + cdiToken + ", jti=" + jti + ", payload=" + payload + ", aiCode=" + aiCode + ", createTime=" + createTime + ", expireTime=" + expireTime + ", tranTime=" + tranTime + '}';
    }

    
    
}
