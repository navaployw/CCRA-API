/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.models.security;
public class Company {
    
   private Long cbuId;
   private String companyNameEng;
   private String companyNameLo;

    public Long getCbuId() {
        return cbuId;
    }

    public void setCbuId(Long cbuId) {
        this.cbuId = cbuId;
    }

    public String getCompanyNameEng() {
        return companyNameEng;
    }

    public void setCompanyNameEng(String companyNameEng) {
        this.companyNameEng = companyNameEng;
    }

    public String getCompanyNameLo() {
        return companyNameLo;
    }

    public void setCompanyNameLo(String companyNameLo) {
        this.companyNameLo = companyNameLo;
    }

    @Override
    public String toString() {
        return "Company{" + "cbuId=" + cbuId + ", companyNameEng=" + companyNameEng + ", companyNameLo=" + companyNameLo + '}';
    }
   
   
    
    
    
}
