package com.arg.ccra3.model.inquiry.data;

import java.util.Date;

public class SearchByNameData {
    private int cbuid;
    private Date disputeDate;
    private String disputeReason;
    private String district;
    private String duns_no;
    private String company_name_en;
    private String company_name_lo;
    private String nameType;
    private Boolean disputeStatus;
    private Integer disputeBy;

    public void trimStringData(){
        if(district != null)
            district = district.trim();
        if(company_name_en != null)
            company_name_en = company_name_en.trim();
        if(company_name_lo != null)
            company_name_lo = company_name_lo.trim();
    }

    public int getCbuid() {
        return cbuid;
    }

    public void setCbuid(int cbuid) {
        this.cbuid = cbuid;
    }

    public Date getDisputeDate() {
        return disputeDate;
    }

    public void setDisputeDate(Date disputeDate) {
        this.disputeDate = disputeDate;
    }

    public String getDisputeReason() {
        return disputeReason;
    }

    public void setDisputeReason(String disputeReason) {
        this.disputeReason = disputeReason;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDuns_no() {
        return duns_no;
    }

    public void setDuns_no(String duns_no) {
        this.duns_no = duns_no;
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

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public Boolean getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(Boolean disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public Integer getDisputeBy() {
        return disputeBy;
    }

    public void setDisputeBy(Integer disputeBy) {
        this.disputeBy = disputeBy;
    }

    @Override
    public String toString() {
        return "SearchByNameData{" + "cbuid=" + cbuid + ", disputeDate=" + disputeDate + ", disputeReason=" + disputeReason + ", district=" + district + ", duns_no=" + duns_no + ", company_name_en=" + company_name_en + ", company_name_lo=" + company_name_lo + ", nameType=" + nameType + ", disputeStatus=" + disputeStatus + ", disputeBy=" + disputeBy + '}';
    }

}
