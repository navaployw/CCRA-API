package com.arg.ccra3.online.form;

public class RequestReportByIDForm {

    protected String idNumber = null;
    protected String idType = null;
    protected String placeRegist = null;

    public String getIdNumber()
    {
        return idNumber;
    }
    public String getIdType()
    {
        return idType;
    }
    public String getPlaceRegist()
    {
        return placeRegist;
    }
    public void setIdNumber(String string)
    {
        this.idNumber = string;
    }
    public void setIdType(String string)
    {
        idType = string;
    }
    public void setPlaceRegist(String string)
    {
        placeRegist = string;
    }
}
