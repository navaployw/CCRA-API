
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
@Table(name = "API_SPM_EXPENSE")
@EntityListeners(AuditingEntityListener.class)
public class ExpenseAPI {    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EXPENSEID", nullable = false, insertable = false, updatable = false)
    private Long expenseid;
    @Column(name = "UID", nullable = false)
    private Long uid;
    @Column(name = "SESSIONID", nullable = false)
    private Long sessionID;
    @Column(name = "GROUPID", nullable = false)
    private Long groupid;
    @Column(name = "GROUPAIID", nullable = false)
    private Long groupaiid;
    @Column(name = "PRODUCTUSAGE")
    private Integer productusage;
    @Column(name = "FLAGRESULT")
    private Boolean flagresult;
    @Column(name = "CBUID")
    private Long cbuid;
    @Column(name = "LOC_BRANCH_ID")
    private String loc_branch_id;
    @Column(name = "ACC_MNGER_CODE")
    private String acc_mnger_code;
    @Column(name = "BRC_NO")
    private String brc_no;
    @Column(name = "CI_NO")
    private String ci_no;
    @Column(name = "OTHER_REG_NO")
    private String other_reg_no;
    @Column(name = "PLACE_OF_REG")
    private String place_of_reg;
    @Column(name = "CUSTOMER_NO")
    private String customer_no;
    @Column(name = "COMPANY_NAME_EN")
    private String company_name_en;
    @Column(name = "COMPANY_NAME_LO")
    private String company_name_lo;
    @Column(name = "FLAGEXPENSE")
    private String flagexpense;
    @Column(name = "FLAGCHARGE")
    private Boolean flagcharge;
    @Column(name = "DISABLED", nullable = false)
    private Boolean disabled;
    @Column(name = "REQUESTTIME")
    private Date requesttime;
    @Column(name = "RESPONSETIME")
    private Date responsetime;
    @Column(name = "UNITCHARGE")
    private Long unitcharge;
    @Column(name = "CREATEDBY", nullable = false)
    private Long createdby;
    @CreatedDate
    @Column(name = "CREATEDDATE", nullable = false)
    private Date createddate;
    @Column(name = "UPDATEDBY")
    private Long updatedby;
    @LastModifiedDate
    @Column(name = "UPDATEDDATE")
    private Date updateddate;
    @Column(name = "SUBMISSION_FLAG", nullable = false)
    private Boolean submission_flag;
    @Column(name = "REVOC_EFFECT_DATE")
    private Date revoc_effect_date;
    @Column(name = "REPORT_REF_NO")
    private String report_ref_no;
    @Column(name = "CONVERTED_REQUESTTIME")
    private String converted_requesttime;

    public Long getExpenseid() {
        return expenseid;
    }

    public void setExpenseid(Long expenseid) {
        this.expenseid = expenseid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getSessionID() {
        return sessionID;
    }

    public void setSessionID(Long sessionID) {
        this.sessionID = sessionID;
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public Long getGroupaiid() {
        return groupaiid;
    }

    public void setGroupaiid(Long groupaiid) {
        this.groupaiid = groupaiid;
    }

    public Integer getProductusage() {
        return productusage;
    }

    public void setProductusage(Integer productusage) {
        this.productusage = productusage;
    }

    public Boolean getFlagresult() {
        return flagresult;
    }

    public void setFlagresult(Boolean flagresult) {
        this.flagresult = flagresult;
    }

    public Long getCbuid() {
        return cbuid;
    }

    public void setCbuid(Long cbuid) {
        this.cbuid = cbuid;
    }

    public String getLoc_branch_id() {
        return loc_branch_id;
    }

    public void setLoc_branch_id(String loc_branch_id) {
        this.loc_branch_id = loc_branch_id;
    }

    public String getAcc_mnger_code() {
        return acc_mnger_code;
    }

    public void setAcc_mnger_code(String acc_mnger_code) {
        this.acc_mnger_code = acc_mnger_code;
    }

    public String getBrc_no() {
        return brc_no;
    }

    public void setBrc_no(String brc_no) {
        this.brc_no = brc_no;
    }

    public String getCi_no() {
        return ci_no;
    }

    public void setCi_no(String ci_no) {
        this.ci_no = ci_no;
    }

    public String getOther_reg_no() {
        return other_reg_no;
    }

    public void setOther_reg_no(String other_reg_no) {
        this.other_reg_no = other_reg_no;
    }

    public String getPlace_of_reg() {
        return place_of_reg;
    }

    public void setPlace_of_reg(String place_of_reg) {
        this.place_of_reg = place_of_reg;
    }

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getCompany_name_en() {
        return company_name_en;
    }

    public void setCompany_name_en(String company_name_en) {
        this.company_name_en = company_name_en;
    }

    public String getCompany_name_lo() {
        return company_name_lo;
    }

    public void setCompany_name_lo(String company_name_lo) {
        this.company_name_lo = company_name_lo;
    }

    public String getFlagexpense() {
        return flagexpense;
    }

    public void setFlagexpense(String flagexpense) {
        this.flagexpense = flagexpense;
    }

    public Boolean getFlagcharge() {
        return flagcharge;
    }

    public void setFlagcharge(Boolean flagcharge) {
        this.flagcharge = flagcharge;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(Date requesttime) {
        this.requesttime = requesttime;
    }

    public Date getResponsetime() {
        return responsetime;
    }

    public void setResponsetime(Date responsetime) {
        this.responsetime = responsetime;
    }

    public Long getUnitcharge() {
        return unitcharge;
    }

    public void setUnitcharge(Long unitcharge) {
        this.unitcharge = unitcharge;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Long updatedby) {
        this.updatedby = updatedby;
    }

    public Date getUpdateddate() {
        return updateddate;
    }

    public void setUpdateddate(Date updateddate) {
        this.updateddate = updateddate;
    }

    public Boolean getSubmission_flag() {
        return submission_flag;
    }

    public void setSubmission_flag(Boolean submission_flag) {
        this.submission_flag = submission_flag;
    }

    public Date getRevoc_effect_date() {
        return revoc_effect_date;
    }

    public void setRevoc_effect_date(Date revoc_effect_date) {
        this.revoc_effect_date = revoc_effect_date;
    }

    public String getReport_ref_no() {
        return report_ref_no;
    }

    public void setReport_ref_no(String report_ref_no) {
        this.report_ref_no = report_ref_no;
    }

    public String getConverted_requesttime() {
        return converted_requesttime;
    }

    public void setConverted_requesttime(String converted_requesttime) {
        this.converted_requesttime = converted_requesttime;
    }

    @Override
    public String toString() {
        return "ExpenseAPI{" + "expenseid=" + expenseid + ", uid=" + uid + ", sessionID=" + sessionID + ", groupid=" + groupid + ", groupaiid=" + groupaiid + ", productusage=" + productusage + ", flagresult=" + flagresult + ", cbuid=" + cbuid + ", loc_branch_id=" + loc_branch_id + ", acc_mnger_code=" + acc_mnger_code + ", brc_no=" + brc_no + ", ci_no=" + ci_no + ", other_reg_no=" + other_reg_no + ", place_of_reg=" + place_of_reg + ", customer_no=" + customer_no + ", company_name_en=" + company_name_en + ", company_name_lo=" + company_name_lo + ", flagexpense=" + flagexpense + ", flagcharge=" + flagcharge + ", disabled=" + disabled + ", requesttime=" + requesttime + ", responsetime=" + responsetime + ", unitcharge=" + unitcharge + ", createdby=" + createdby + ", createddate=" + createddate + ", updatedby=" + updatedby + ", updateddate=" + updateddate + ", submission_flag=" + submission_flag + ", revoc_effect_date=" + revoc_effect_date + ", report_ref_no=" + report_ref_no + ", converted_requesttime=" + converted_requesttime + '}';
    }
    
    
}
