
package com.arg.ccra3.models.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

@Entity
@Table(name = "SPM_USER")
public class UserSPM {

    @Id
    @Column(name = "UID", nullable = false, insertable = false, updatable = false)
    private Long uID;

    @Column(name = "GROUPID", nullable = false, insertable = false, updatable = false)
    private String groupID;

    @Column(name = "GROUPAIID", nullable = false, insertable = false, updatable = false)
    private String groupAIID;

    @Column(name = "GROUPCCRAID", nullable = false, insertable = false, updatable = false)
    private String groupCCRAID;

    @Column(name = "TITLE", nullable = false, insertable = false, updatable = false)
    private String title;

    @Column(name = "USER_NAME_LO", nullable = false, insertable = false, updatable = false)
    private String userNameLocal;

    @Column(name = "USER_NAME_EN", nullable = false, insertable = false, updatable = false)
    private String userNameEnglish;

    @Column(name = "MANAGERID", nullable = false, insertable = false, updatable = false)
    private String managerID;

    @Column(name = "USERID", nullable = false, insertable = false, updatable = false)
    private String userID;

    @Column(name = "PASSWORD", nullable = false, insertable = false, updatable = false)
    private String password;

    @Column(name = "ADMINFLAG", nullable = false, insertable = false, updatable = false)
    private String adminFlag;

    @Column(name = "DESCRIPTION", nullable = false, insertable = false, updatable = false)
    private String description;

    @Column(name = "PREFERREDLANGUAGE", nullable = false, insertable = false, updatable = false)
    private String preferredLanguage;

    @Column(name = "MAXCONNECTION", nullable = false, insertable = false, updatable = false)
    private String maxConnection;

    @Column(name = "LOGONFAIL", nullable = false, insertable = false, updatable = false)
    private String loginFail;

    @Column(name = "SESSIONTIMEOUT", nullable = false, insertable = false, updatable = false)
    private String sessionTimeout;

    @Column(name = "CHANGEPASSWORDNEXTLOGON", nullable = false, insertable = false, updatable = false)
    private String changePasswordNextLogin;

    @Column(name = "CANNOTCHANGEPASSWORD", nullable = false, insertable = false, updatable = false)
    private String cannotChangePassword;

    @Column(name = "PASSWORDNEVEREXPIRED", nullable = false, insertable = false, updatable = false)
    private String passwordNeverExpired;

    @Column(name = "PASSWORDEXPIREDPERIOD", nullable = false, insertable = false, updatable = false)
    private String passwordExpiredPeriod;

    @Column(name = "PASSWORDEXPIREDDATE", nullable = false, insertable = false, updatable = false)
    private String passwordExpiredDate;

    @Column(name = "EXPIREDATE", nullable = false, insertable = false, updatable = false)
    private String expiredDate;

    @Column(name = "LOGIN_PERMIT_MON", nullable = false, insertable = false, updatable = false)
    private String loginPermittedMon;

    @Column(name = "LOGIN_PERMIT_TUE", nullable = false, insertable = false, updatable = false)
    private String loginPermittedTue;

    @Column(name = "LOGIN_PERMIT_WED", nullable = false, insertable = false, updatable = false)
    private String loginPermittedWed;

    @Column(name = "LOGIN_PERMIT_THU", nullable = false, insertable = false, updatable = false)
    private String loginPermittedThu;

    @Column(name = "LOGIN_PERMIT_FRI", nullable = false, insertable = false, updatable = false)
    private String loginPermittedFri;

    @Column(name = "LOGIN_PERMIT_SAT", nullable = false, insertable = false, updatable = false)
    private String loginPermittedSat;

    @Column(name = "LOGIN_PERMIT_SUN", nullable = false, insertable = false, updatable = false)
    private String loginPermittedSun;

    @Column(name = "DELETED", nullable = false, insertable = false, updatable = false)
    private String deleted;

    @Column(name = "DISABLED", nullable = false, insertable = false, updatable = false)
    private String disabled;

    @Column(name = "DISABLEDREASON", nullable = false, insertable = false, updatable = false)
    private String disabledReason;

    @Column(name = "SHAREMAILBOX", nullable = false, insertable = false, updatable = false)
    private String shareMailBox;

    @Column(name = "EMAIL", nullable = false, insertable = false, updatable = false)
    private String email;

    @Column(name = "CREATEDBY", nullable = false, insertable = false, updatable = false)
    private String createdBy;

    @Column(name = "CREATEDDATE", nullable = false, insertable = false, updatable = false)
    private String createdDate;

    @Column(name = "UPDATEDBY", nullable = false, insertable = false, updatable = false)
    private String updatedBy;

    @Column(name = "UPDATEDDATE", nullable = false, insertable = false, updatable = false)
    private String updatedDate;

    public Long getuID() {
        return uID;
    }

    public void setuID(Long uID) {
        this.uID = uID;
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

    public String getGroupCCRAID() {
        return groupCCRAID;
    }

    public void setGroupCCRAID(String groupCCRAID) {
        this.groupCCRAID = groupCCRAID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserNameLocal() {
        return userNameLocal;
    }

    public void setUserNameLocal(String userNameLocal) {
        this.userNameLocal = userNameLocal;
    }

    public String getUserNameEnglish() {
        return userNameEnglish;
    }

    public void setUserNameEnglish(String userNameEnglish) {
        this.userNameEnglish = userNameEnglish;
    }

    public String getManagerID() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID = managerID;
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

    public String getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(String adminFlag) {
        this.adminFlag = adminFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(String maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getLoginFail() {
        return loginFail;
    }

    public void setLoginFail(String loginFail) {
        this.loginFail = loginFail;
    }

    public String getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(String sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getChangePasswordNextLogin() {
        return changePasswordNextLogin;
    }

    public void setChangePasswordNextLogin(String changePasswordNextLogin) {
        this.changePasswordNextLogin = changePasswordNextLogin;
    }

    public String getCannotChangePassword() {
        return cannotChangePassword;
    }

    public void setCannotChangePassword(String cannotChangePassword) {
        this.cannotChangePassword = cannotChangePassword;
    }

    public String getPasswordNeverExpired() {
        return passwordNeverExpired;
    }

    public void setPasswordNeverExpired(String passwordNeverExpired) {
        this.passwordNeverExpired = passwordNeverExpired;
    }

    public String getPasswordExpiredPeriod() {
        return passwordExpiredPeriod;
    }

    public void setPasswordExpiredPeriod(String passwordExpiredPeriod) {
        this.passwordExpiredPeriod = passwordExpiredPeriod;
    }

    public String getPasswordExpiredDate() {
        return passwordExpiredDate;
    }

    public void setPasswordExpiredDate(String passwordExpiredDate) {
        this.passwordExpiredDate = passwordExpiredDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getLoginPermittedMon() {
        return loginPermittedMon;
    }

    public void setLoginPermittedMon(String loginPermittedMon) {
        this.loginPermittedMon = loginPermittedMon;
    }

    public String getLoginPermittedTue() {
        return loginPermittedTue;
    }

    public void setLoginPermittedTue(String loginPermittedTue) {
        this.loginPermittedTue = loginPermittedTue;
    }

    public String getLoginPermittedWed() {
        return loginPermittedWed;
    }

    public void setLoginPermittedWed(String loginPermittedWed) {
        this.loginPermittedWed = loginPermittedWed;
    }

    public String getLoginPermittedThu() {
        return loginPermittedThu;
    }

    public void setLoginPermittedThu(String loginPermittedThu) {
        this.loginPermittedThu = loginPermittedThu;
    }

    public String getLoginPermittedFri() {
        return loginPermittedFri;
    }

    public void setLoginPermittedFri(String loginPermittedFri) {
        this.loginPermittedFri = loginPermittedFri;
    }

    public String getLoginPermittedSat() {
        return loginPermittedSat;
    }

    public void setLoginPermittedSat(String loginPermittedSat) {
        this.loginPermittedSat = loginPermittedSat;
    }

    public String getLoginPermittedSun() {
        return loginPermittedSun;
    }

    public void setLoginPermittedSun(String loginPermittedSun) {
        this.loginPermittedSun = loginPermittedSun;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getDisabledReason() {
        return disabledReason;
    }

    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    public String getShareMailBox() {
        return shareMailBox;
    }

    public void setShareMailBox(String shareMailBox) {
        this.shareMailBox = shareMailBox;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
