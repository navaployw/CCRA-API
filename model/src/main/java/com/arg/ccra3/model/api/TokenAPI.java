package com.arg.ccra3.model.api;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "API_TOKEN")
public class TokenAPI {
    @Id
    @Column(name = "TOKENID", nullable = false, insertable = false, updatable = false)
    private Integer tokenId;
    @Column(name = "AID", nullable = false, insertable = false, updatable = false)
    private Integer aID;
    @Column(name = "UID", nullable = false, insertable = false, updatable = false)
    private Integer uID;
    @Column(name = "ACCESS_TOKEN", nullable = false, insertable = false, updatable = false)
    private String accessToken;
    @Column(name = "IAT", nullable = false, insertable = false, updatable = false)
    private Integer iat;
    @Column(name = "EXP", nullable = false, insertable = false, updatable = false)
    private Integer exp;
    @Column(name = "CREATE_TIME", nullable = false, insertable = false, updatable = false)
    private Date createTime;
    @Column(name = "EXPIRE_TIME", nullable = false, insertable = false, updatable = false)
    private Date expireTime;
    @Column(name = "AICODE", nullable = false, insertable = false, updatable = false)
    private String aiCode;

    public Integer getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }

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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getIat() {
        return iat;
    }

    public void setIat(Integer iat) {
        this.iat = iat;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
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

    public String getAiCode() {
        return aiCode;
    }

    public void setAiCode(String aiCode) {
        this.aiCode = aiCode;
    }

    @Override
    public String toString() {
        return "TokenAPI{" + "tokenId=" + tokenId + ", aID=" + aID + ", uID=" + uID + ", accessToken=" + accessToken + ", iat=" + iat + ", exp=" + exp + ", createTime=" + createTime + ", expireTime=" + expireTime + ", aiCode=" + aiCode + '}';
    }


}
