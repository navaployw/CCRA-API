package com.arg.ccra3.online.form;

import com.arg.ccra3.models.User;

public class ReportAPIRequest {
    private String cdiToken;
    private User user;
    private String aiCode;
    private String companyID;
    private String reportType;
    private String reasonCode;
    private String aiRefCode1;
    private String aiRefCode2;
    private String aiRefCode3;
    private String reorderIn7Days;
    private String userID;
    private Long matchTranId;
    private Long tokenId;
    private Long expenseId;
    
    public String getCdiToken() {
        return cdiToken;
    }

    public void setCdiToken(String cdiToken) {
        this.cdiToken = cdiToken;
    }

    public String getAiCode() {
        return aiCode;
    }

    public void setAiCode(String aiCode) {
        this.aiCode = aiCode;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getAiRefCode1() {
        return aiRefCode1;
    }

    public void setAiRefCode1(String aiRefCode1) {
        this.aiRefCode1 = aiRefCode1;
    }

    public String getAiRefCode2() {
        return aiRefCode2;
    }

    public void setAiRefCode2(String aiRefCode2) {
        this.aiRefCode2 = aiRefCode2;
    }

    public String getAiRefCode3() {
        return aiRefCode3;
    }

    public void setAiRefCode3(String aiRefCode3) {
        this.aiRefCode3 = aiRefCode3;
    }

    public String getReorderIn7Days() {
        return reorderIn7Days;
    }

    public void setReorderIn7Days(String reorderIn7Days) {
        this.reorderIn7Days = reorderIn7Days;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Long getMatchTranId() {
        return matchTranId;
    }

    public void setMatchTranId(Long matchTranId) {
        this.matchTranId = matchTranId;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }
    
    @Override
    public String toString() {
        return "ReportAPIRequest{" + "cdiToken=" + cdiToken + ", user=" + user + ", aiCode=" + aiCode + ", companyID=" + companyID + ", reportType=" + reportType + ", reasonCode=" + reasonCode + ", aiRefCode1=" + aiRefCode1 + ", aiRefCode2=" + aiRefCode2 + ", aiRefCode3=" + aiRefCode3 + ", reorderIn7Days=" + reorderIn7Days + ", userID=" + userID + ", matchTranId=" + matchTranId + ", tokenId=" + tokenId + '}';
    }
    
    public String toJsonStringReport() {
        return '{' + "\"cdiToken\":\"" + cdiToken + "\", \"aiCode\":\"" + aiCode + "\", \"companyID\":" + companyID + ", \"reportType\":" + reportType + ", \"reasonCode\":" + reasonCode + ", \"aiRefCode1\":\"" + aiRefCode1 + "\", \"aiRefCode2\":\"" + aiRefCode2 + "\", \"aiRefCode3\":\"" + aiRefCode3 + "\", \"reorderIn7Days\":\"" + reorderIn7Days + "\"}";
    }
    
}
