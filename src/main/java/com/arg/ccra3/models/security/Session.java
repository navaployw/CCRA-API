
package com.arg.ccra3.models.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SPM_SESSION")
public class Session {
    
    @Id
    @Column(name = "SESSIONID", nullable = false, updatable = false, insertable = false)
    private Long sessionID;
    
    @Column(name = "UID", nullable = false, updatable = false, insertable = false)
    private Long uID;
    
    @Column(name = "LOGINDATE", nullable = false, updatable = false, insertable = false)
    private Long loginDate;
    
    @Column(name = "LOGOUTDATE", nullable = false, updatable = false, insertable = false)
    private Long logoutDate;
    
    @Column(name = "IPADDRESS", nullable = false, updatable = false, insertable = false)
    private Long ipAddress;
    
    @Column(name = "CHANNELID", nullable = false, updatable = false, insertable = false)
    private Long channelID;
    
    @Column(name = "lastActiveTime", nullable = false, updatable = false, insertable = false)
    private Long LACTIVETIME;
    
    @Column(name = "LOGINATTEMPT", nullable = false, updatable = false, insertable = false)
    private Long loginAttempt;
    
    @Column(name = "LOGINFLAG", nullable = false, updatable = false, insertable = false)
    private Long loginFlag;

    public Long getSessionID() {
        return sessionID;
    }

    public void setSessionID(Long sessionID) {
        this.sessionID = sessionID;
    }

    public Long getuID() {
        return uID;
    }

    public void setuID(Long uID) {
        this.uID = uID;
    }

    public Long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Long loginDate) {
        this.loginDate = loginDate;
    }

    public Long getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Long logoutDate) {
        this.logoutDate = logoutDate;
    }

    public Long getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(Long ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getChannelID() {
        return channelID;
    }

    public void setChannelID(Long channelID) {
        this.channelID = channelID;
    }

    public Long getLACTIVETIME() {
        return LACTIVETIME;
    }

    public void setLACTIVETIME(Long LACTIVETIME) {
        this.LACTIVETIME = LACTIVETIME;
    }

    public Long getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(Long loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    public Long getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(Long loginFlag) {
        this.loginFlag = loginFlag;
    }
    
    
}
