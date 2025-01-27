package com.arg.ccra3.model.report;

public class Attention extends AbstractReportObject{
    private String userID;
    private String address1;
    private String address2;
    private String address3;
    private String country;
    private String areaCode;
    private String province;
    private String city;

    public String getUserID() {
        return userID;
    }

    public Attention setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public String getAddress1() {
        return address1;
    }

    public Attention setAddress1(String address1) {
        this.address1 = address1;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public Attention setAddress2(String address2) {
        this.address2 = address2;
        return this;
    }

    public String getAddress3() {
        return address3;
    }

    public Attention setAddress3(String address3) {
        this.address3 = address3;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Attention setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public Attention setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public Attention setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Attention setCity(String city) {
        this.city = city;
        return this;
    }

    @Override
    void setNullToPropertiesIfNoValue() {
        if(userID != null && userID.isBlank()) userID = null;
        if(address1 != null && address1.isBlank()) address1 = null;
        if(address2 != null && address2.isBlank()) address2 = null;
        if(address3 != null && address3.isBlank()) address3 = null;
        if(country != null && country.isBlank()) country = null;
        if(areaCode != null && areaCode.isBlank()) areaCode = null;
        if(province != null && province.isBlank()) province = null;
        if(city != null && city.isBlank()) city = null;
    }
    
}
