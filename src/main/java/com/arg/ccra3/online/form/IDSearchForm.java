
package com.arg.ccra3.online.form;

public class IDSearchForm {    
    private String aiRefCode1;    
    private String aiRefCode2;    
    private String aiRefCode3;    
    private String cbuid;    
    private String companyName;    
    private String expenseID;    
    private String idNumber;    
    private String idType;    
    private String placeRegist;    
    private String reasonCode;    
    private String reasonCodeDesc;    
    private String recordPerPage;    
    private String searchIdNumber;    
    private String transactionId;    
    private boolean charge = false;    
    private boolean newSearch = true;    
    
    public String getAiRefCode1()
    {
        return aiRefCode1;
    }
    
    public String getAiRefCode2()
    {
        return aiRefCode2;
    }
    
    public String getAiRefCode3()
    {
        return aiRefCode3;
    }
    
    public String getCbuid()
    {
        return cbuid;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
    public String getExpanseID()
    {
        return expenseID;
    }
    
    public String getIdNumber()
    {
        return idNumber;
    }
    
    public String getIdType()
    {
        return (null == idType) ? null : idType.trim();
    }
    
    public String getPlaceRegist()
    {
        return (null == placeRegist) ? null : placeRegist.trim();
    }
    
    public String getReasonCode()
    {
        return reasonCode;
    }
    
    public String getReasonCodeDesc()
    {
        return reasonCodeDesc;
    }
    
    public String getRecordPerPage()
    {
        return recordPerPage;
    }
    
    public String getSearchIdNumber()
    {
        return searchIdNumber;
    }
    
    public String getTransactionId()
    {
        return transactionId;
    }
    
    public boolean isCharge()
    {
        return charge;
    }
    
    public boolean isNewSearch()
    {
        return newSearch;
    }
    
    public void setAiRefCode1(String string)
    {
        this.aiRefCode1 = (null == string) ? null : string.trim();
    }
    
    public void setAiRefCode2(String string)
    {
        this.aiRefCode2 = (null == string) ? null : string.trim();
    }
    
    public void setAiRefCode3(String string)
    {
        this.aiRefCode3 = (null == string) ? null : string.trim();
    }
    
    public void setCbuid(String string)
    {
        cbuid = (null == string) ? null : string.trim();
    }
    
    public void setCharge(boolean b)
    {
        charge = b;
    }
    
    public void setCompanyName(String string)
    {
        this.companyName = (null == string) ? null : string;
    }
    
    public void setExpanseID(String string)
    {
        expenseID = (null == string) ? null : string.trim();
    }
    
    public void setIdNumber(String string)
    {
        this.idNumber = (null == string) ? null : string.trim();
    }
    
    public void setIdType(String string)
    {
        idType = (null == string) ? null : string.trim();
    }
    
    public void setNewSearch(boolean b)
    {
        newSearch = b;
    }
    
    public void setPlaceRegist(String string)
    {
        placeRegist = (null == string) ? null : string.trim();
    }
    
    public void setReasonCode(String string)
    {
        reasonCode = (null == string) ? null : string.trim();
    }
    
    public void setReasonCodeDesc(String string)
    {
        this.reasonCodeDesc = (null == string) ? null : string.trim();
    }
    
    public void setRecordPerPage(String string)
    {
        recordPerPage = (null == string) ? null : string.trim();
    }
    
    public void setSearchIdNumber(String string)
    {
        this.searchIdNumber = (null == string) ? null : string.trim();
    }
    
    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }
}
