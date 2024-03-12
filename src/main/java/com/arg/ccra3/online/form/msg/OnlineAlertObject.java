
package com.arg.ccra3.online.form.msg;
public class OnlineAlertObject {
    private int cbuid;
    private int groupAIID;
    private int groupID;
    private int productUsage;
    private int reasonCode;
    private int sessionID;
    private int uID;
    private long expenseID;
    
    
    public int getCbuid()
    {
        return cbuid;
    }
    public void setCbuid(final int cbuid)
    {
        this.cbuid = cbuid;
    }
    public int getGroupAIID()
    {
        return groupAIID;
    }
    public void setGroupAIID(final int groupAIID)
    {
        this.groupAIID = groupAIID;
    }
    public int getGroupID()
    {
        return groupID;
    }
    public void setGroupID(final int groupID)
    {
        this.groupID = groupID;
    }
    public int getProductUsage()
    {
        return productUsage;
    }
    public void setProductUsage(final int productUsage)
    {
        this.productUsage = productUsage;
    }
    public int getSessionID()
    {
        return sessionID;
    }
    public void setSessionID(final int sessionID)
    {
        this.sessionID = sessionID;
    }
    public int getUID()
    {
        return uID;
    }
    public void setUID(final int uid)
    {
        uID = uid;
    }
    public long getExpenseID()
    {
        return expenseID;
    }
    public void setExpenseID(final long expenseID)
    {
        this.expenseID = expenseID;
    }
    public int getReasonCode()
    {
        return reasonCode;
    }
    public void setReasonCode(final int reasonCode)
    {
        this.reasonCode = reasonCode;
    }
}
