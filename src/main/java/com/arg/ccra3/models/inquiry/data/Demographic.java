package com.arg.ccra3.models.inquiry.data;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "PROD_DEMOGRAPHIC")
public class Demographic extends AbstractPersistable<Long> {
    @Column(name = "CBUID", nullable = false)
    private Integer CBUID;    
    @Column(name = "GROUPID", nullable = false)
    private Integer groupID;    
    @Column(name = "REGISTRATIONDATAID", nullable = false)
    private int registrationDataID = -1;
    @Column(name = "PERIOD", nullable = false)
    private String period;
    @Column(name = "DUNSNO", nullable = false)
    private String dunsNO;
    @Column(name = "NAME_LO", nullable = false)
    private String nameLO;
    @Column(name = "NAME_EN", nullable = false)
    private String nameEN;
    @Column(name = "COM_TYPE", nullable = false)    
    private int companyType = -1;
    @Column(name = "BRC_NO", nullable = false)
    private String BRCNO;    
    @Column(name = "CI_NO", nullable = false)
    private String CINO;    
    @Column(name = "ADDRESS_1", nullable = false)
    private String addressEN1;    
    @Column(name = "ADDRESS_2", nullable = false)
    private String addressEN2;    
    @Column(name = "ADDRESS_3", nullable = false)
    private String addressEN3;    
    @Column(name = "ADDRESS_LO_1", nullable = false)
    private String addressLO1;
    @Column(name = "ADDRESS_LO_2", nullable = false)
    private String addressLO2;
    @Column(name = "ADDRESS_LO_3", nullable = false)
    private String addressLO3;
    @Column(name = "OTHER_REG_NO", nullable = false)
    private String otherRegNO;
    @Column(name = "PLACE_OF_REG", nullable = false)
    private String placeOfReg;
    @Column(name = "CUSTOMER_NO", nullable = false)
    private String customerNO;
    @Column(name = "TRADE_NAME_LO", nullable = false)
    private String tradeNameLO;    
    @Column(name = "TRADE_NAME_EN", nullable = false)
    private String tradeNameEN;
    @Column(name = "PREV_NAME_LO", nullable = false)
    private String prevNameLO;
    @Column(name = "PREV_NAME_EN", nullable = false)    
    private String prevNameEN;
    @Column(name = "CITY_LO", nullable = false)
    private String cityLO;
    @Column(name = "PROVINCE_LO", nullable = false)  
    private String provinceLO;
    @Column(name = "POST_CODE", nullable = false)
    private String postCode;
    @Column(name = "CITY", nullable = false)
    private String cityEN;
    @Column(name = "PROVINCE", nullable = false)
    private String provinceEN;
    @Column(name = "COUNTRY", nullable = false)
    private String country;
    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;
    @Column(name = "AREA_CODE", nullable = false)
    private String areaCode;    
    @Column(name = "PHONE_NO", nullable = false)
    private String phoneNO;
    @Column(name = "REVOC_UPDATE", nullable = false)
    private boolean revocationUpdated = false;
    @Column(name = "REVOC_NOTICED_DATE", nullable = false)
    private Date revocationNoticedDate;    
    @Column(name = "REVOC_EFFECT_DATE", nullable = false)
    private Date revocationEffectedDate;
    @Column(name = "WITHDRAW_REVOC", nullable = false)
    private String withdrawRevocation;
    @Column(name = "SHAREHOLDER_NAME_LO", nullable = false)
    private String sharedHolderNameLO;
    @Column(name = "SHAREHOLDER_NAME_EN", nullable = false)
    private String sharedHolderNameEN;
    @Column(name = "FLAGRESEND", nullable = false)
    private Boolean flagResend = false;    
    @Column(name = "CREATEDBY", nullable = false)    
    private int createdBy = -1;
    @Column(name = "CREATEDDATE", nullable = false)
    private Date createdDate;    
    @Column(name = "UPDATEDBY", nullable = false)    
    private int updatedBy = -1;
    @Column(name = "UPDATEDDATE", nullable = false)    
    private Date updatedDate;    
    @Column(name = "VERSION", nullable = false)

    public Integer getCBUID() {
        return CBUID;
    }

    public void setCBUID(Integer CBUID) {
        this.CBUID = CBUID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public int getRegistrationDataID() {
        return registrationDataID;
    }

    public void setRegistrationDataID(int registrationDataID) {
        this.registrationDataID = registrationDataID;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDunsNO() {
        return dunsNO;
    }

    public void setDunsNO(String dunsNO) {
        this.dunsNO = dunsNO;
    }

    public String getNameLO() {
        return nameLO;
    }

    public void setNameLO(String nameLO) {
        this.nameLO = nameLO;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public int getCompanyType() {
        return companyType;
    }

    public void setCompanyType(int companyType) {
        this.companyType = companyType;
    }

    public String getBRCNO() {
        return BRCNO;
    }

    public void setBRCNO(String BRCNO) {
        this.BRCNO = BRCNO;
    }

    public String getCINO() {
        return CINO;
    }

    public void setCINO(String CINO) {
        this.CINO = CINO;
    }

    public String getAddressEN1() {
        return addressEN1;
    }

    public void setAddressEN1(String addressEN1) {
        this.addressEN1 = addressEN1;
    }

    public String getAddressEN2() {
        return addressEN2;
    }

    public void setAddressEN2(String addressEN2) {
        this.addressEN2 = addressEN2;
    }

    public String getAddressEN3() {
        return addressEN3;
    }

    public void setAddressEN3(String addressEN3) {
        this.addressEN3 = addressEN3;
    }

    public String getAddressLO1() {
        return addressLO1;
    }

    public void setAddressLO1(String addressLO1) {
        this.addressLO1 = addressLO1;
    }

    public String getAddressLO2() {
        return addressLO2;
    }

    public void setAddressLO2(String addressLO2) {
        this.addressLO2 = addressLO2;
    }

    public String getAddressLO3() {
        return addressLO3;
    }

    public void setAddressLO3(String addressLO3) {
        this.addressLO3 = addressLO3;
    }

    public String getOtherRegNO() {
        return otherRegNO;
    }

    public void setOtherRegNO(String otherRegNO) {
        this.otherRegNO = otherRegNO;
    }

    public String getPlaceOfReg() {
        return placeOfReg;
    }

    public void setPlaceOfReg(String placeOfReg) {
        this.placeOfReg = placeOfReg;
    }

    public String getCustomerNO() {
        return customerNO;
    }

    public void setCustomerNO(String customerNO) {
        this.customerNO = customerNO;
    }

    public String getTradeNameLO() {
        return tradeNameLO;
    }

    public void setTradeNameLO(String tradeNameLO) {
        this.tradeNameLO = tradeNameLO;
    }

    public String getTradeNameEN() {
        return tradeNameEN;
    }

    public void setTradeNameEN(String tradeNameEN) {
        this.tradeNameEN = tradeNameEN;
    }

    public String getPrevNameLO() {
        return prevNameLO;
    }

    public void setPrevNameLO(String prevNameLO) {
        this.prevNameLO = prevNameLO;
    }

    public String getPrevNameEN() {
        return prevNameEN;
    }

    public void setPrevNameEN(String prevNameEN) {
        this.prevNameEN = prevNameEN;
    }

    public String getCityLO() {
        return cityLO;
    }

    public void setCityLO(String cityLO) {
        this.cityLO = cityLO;
    }

    public String getProvinceLO() {
        return provinceLO;
    }

    public void setProvinceLO(String provinceLO) {
        this.provinceLO = provinceLO;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCityEN() {
        return cityEN;
    }

    public void setCityEN(String cityEN) {
        this.cityEN = cityEN;
    }

    public String getProvinceEN() {
        return provinceEN;
    }

    public void setProvinceEN(String provinceEN) {
        this.provinceEN = provinceEN;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNO() {
        return phoneNO;
    }

    public void setPhoneNO(String phoneNO) {
        this.phoneNO = phoneNO;
    }

    public boolean isRevocationUpdated() {
        return revocationUpdated;
    }

    public void setRevocationUpdated(boolean revocationUpdated) {
        this.revocationUpdated = revocationUpdated;
    }

    public Date getRevocationNoticedDate() {
        return revocationNoticedDate;
    }

    public void setRevocationNoticedDate(Date revocationNoticedDate) {
        this.revocationNoticedDate = revocationNoticedDate;
    }

    public Date getRevocationEffectedDate() {
        return revocationEffectedDate;
    }

    public void setRevocationEffectedDate(Date revocationEffectedDate) {
        this.revocationEffectedDate = revocationEffectedDate;
    }

    public String getWithdrawRevocation() {
        return withdrawRevocation;
    }

    public void setWithdrawRevocation(String withdrawRevocation) {
        this.withdrawRevocation = withdrawRevocation;
    }

    public String getSharedHolderNameLO() {
        return sharedHolderNameLO;
    }

    public void setSharedHolderNameLO(String sharedHolderNameLO) {
        this.sharedHolderNameLO = sharedHolderNameLO;
    }

    public String getSharedHolderNameEN() {
        return sharedHolderNameEN;
    }

    public void setSharedHolderNameEN(String sharedHolderNameEN) {
        this.sharedHolderNameEN = sharedHolderNameEN;
    }

    public boolean isFlagResend() {
        return flagResend;
    }

    public void setFlagResend(Boolean flagResend) {
        this.flagResend = flagResend;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public boolean checkLocalStringNotEmpty(){
        if (addressLO1 != null && addressLO1.trim().length() > 0)
            return true;
        if (nameLO != null && nameLO.trim().length() > 0)
            return true;
        if (tradeNameLO != null && tradeNameLO.trim().length() > 0)
            return true;
        if (prevNameLO != null && prevNameLO.trim().length() > 0)
            return true;
        if (sharedHolderNameLO != null && sharedHolderNameLO.trim().length() > 0)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Demographic{" + "CBUID=" + CBUID + ", groupID=" + groupID + ", registrationDataID=" + registrationDataID + ", period=" + period + ", dunsNO=" + dunsNO + ", nameLO=" + nameLO + ", nameEN=" + nameEN + ", companyType=" + companyType + ", BRCNO=" + BRCNO + ", CINO=" + CINO + ", addressEN1=" + addressEN1 + ", addressEN2=" + addressEN2 + ", addressEN3=" + addressEN3 + ", addressLO1=" + addressLO1 + ", addressLO2=" + addressLO2 + ", addressLO3=" + addressLO3 + ", otherRegNO=" + otherRegNO + ", placeOfReg=" + placeOfReg + ", customerNO=" + customerNO + ", tradeNameLO=" + tradeNameLO + ", tradeNameEN=" + tradeNameEN + ", prevNameLO=" + prevNameLO + ", prevNameEN=" + prevNameEN + ", cityLO=" + cityLO + ", provinceLO=" + provinceLO + ", postCode=" + postCode + ", cityEN=" + cityEN + ", provinceEN=" + provinceEN + ", country=" + country + ", countryCode=" + countryCode + ", areaCode=" + areaCode + ", phoneNO=" + phoneNO + ", revocationUpdated=" + revocationUpdated + ", revocationNoticedDate=" + revocationNoticedDate + ", revocationEffectedDate=" + revocationEffectedDate + ", withdrawRevocation=" + withdrawRevocation + ", sharedHolderNameLO=" + sharedHolderNameLO + ", sharedHolderNameEN=" + sharedHolderNameEN + ", flagResend=" + flagResend + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + '}';
    }
    
}
