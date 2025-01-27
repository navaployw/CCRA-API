/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.model;

/**
 *
 * @author kumpeep
 */
public class ReportRequestModel {
    String aiCode;
    String cdiToken;
    String companyID;

    public String getAiCode() {
        return aiCode;
    }

    public void setAiCode(String aiCode) {
        this.aiCode = aiCode;
    }

    public String getCdiToken() {
        return cdiToken;
    }

    public void setCdiToken(String cdiToken) {
        this.cdiToken = cdiToken;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    @Override
    public String toString() {
        return "ReportRequestModel{" + "aiCode=" + aiCode + ", cdiToken=" + cdiToken + ", companyID=" + companyID + '}';
    }
    
    
}
