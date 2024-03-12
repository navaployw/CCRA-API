
package com.arg.ccra3.models.inquiry.data;

import java.util.Date;

public class SearchByIdData {
    private Date disputeDate;
    private String disputeReason;
    private String district;
    private String dunsNo;
    private String currentNameEn;
    private String currentNameLo;
    private String nameType;
    private boolean disputeStatus;
    private int cbuid;
    private int disputeBy;
    
    public int getCbuid()
    {
        return cbuid;
    }
    public int getDisputeBy()
    {
        return disputeBy;
    }
    public Date getDisputeDate()
    {
        return disputeDate;
    }
    public String getDisputeReason()
    {
        return disputeReason;
    }
    public String getDistrict()
    {
        return district;
    }
    public String getDunsNo()
    {
        return dunsNo;
    }
    public String getNameType()
    {
        return nameType;
    }
    public boolean isDisputeStatus()
    {
        return disputeStatus;
    }
    public void setCbuid(int cbuid)
    {
        this.cbuid = cbuid;
    }
    public void setDisputeBy(int disputeBy)
    {
        this.disputeBy = disputeBy;
    }
    public void setDisputeDate(Date disputeDate)
    {
        this.disputeDate = disputeDate;
    }
    public void setDisputeReason(String disputeReason)
    {
        this.disputeReason = disputeReason;
    }
    public void setDisputeStatus(boolean disputeStatus)
    {
        this.disputeStatus = disputeStatus;
    }
    public void setDistrict(String district)
    {
        this.district = district;
    }
    public void setDunsNo(String dunsNo)
    {
        this.dunsNo = dunsNo;
    }
    public String getCurrentNameEn()
    {
        return currentNameEn;
    }
    public void setCurrentNameEn(String currentNameEn)
    {
        this.currentNameEn = currentNameEn;
    }
    public String getCurrentNameLo()
    {
        return currentNameLo;
    }
    public void setCurrentNameLo(String currentNameLo)
    {
        this.currentNameLo = currentNameLo;
    }
    public void setNameType(String nameType)
    {
        this.nameType = nameType;
    }
}
