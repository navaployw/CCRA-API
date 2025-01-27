
package com.arg.ccra3.online.form;


public class NameSearchForm {
    private String aiRefCode1 = null;
    private String aiRefCode2 = null;
    private String aiRefCode3 = null;
    private String cbuid = null;
    private String companyName = null;
    private String criteria = null;
    private String district = null;
    private String expenseID = null;
    private String langType = null;
    private String nameType = null;
    private String reasonCode = null;
    private String reasonCodeDesc = null;
    private String recordPerPage = null;
    private String transactionId = null;
    private boolean charge = false;
    private boolean newSearch = true;

    public String getAiRefCode1(){
        return aiRefCode1;
    }

    public String getAiRefCode2(){
        return aiRefCode2;
    }

    public String getAiRefCode3(){
        return aiRefCode3;
    }

    public String getCbuid(){
        return cbuid;
    }

    public String getCompanyName(){
        return companyName;
    }

    public String getCriteria(){
        return criteria;
    }

    public String getDistrict(){
        return district;
    }

    public String getExpenseID(){
        return expenseID;
    }

    public String getLangType(){
        return langType;
    }

    public String getNameType(){
        return nameType;
    }

    public String getReasonCode(){
        return reasonCode;
    }

    public String getReasonCodeDesc(){
        return reasonCodeDesc;
    }

    public String getRecordPerPage(){
        return recordPerPage;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public boolean isCharge(){
        return charge;
    }

    public boolean isNewSearch(){
        return newSearch;
    }

    public void setAiRefCode1(String string){
        this.aiRefCode1 = string;
    }

    public void setAiRefCode2(String string){
        this.aiRefCode2 = string;
    }

    public void setAiRefCode3(String string){
        this.aiRefCode3 = string;
    }

    public void setCbuid(String string){
        cbuid = string;
    }

    public void setCharge(boolean b){
        charge = b;
    }

    public void setCompanyName(String string){
        this.companyName = string;
    }

    public void setCriteria(String string){
        this.criteria = string;
    }

    public void setDistrict(String string){
        this.district = string;
    }

    public void setExpenseID(String string){
        if (null != string)
        {
            string = string.trim();
        }        expenseID = string;
    }

    public void setLangType(String string){
        this.langType = (null == string) ? null : string.trim();
    }

    public void setNameType(String string){
        nameType = (null == string) ? null : string.trim();
    }

    public void setNewSearch(boolean b){
        newSearch = b;
    }

    public void setReasonCode(String reasonCode){
        this.reasonCode = (null == reasonCode) ? null : reasonCode;
    }

    public void setReasonCodeDesc(String string){
        this.reasonCodeDesc = (null == string) ? null : string.trim();
    }

    public void setRecordPerPage(String string){
        recordPerPage = string;
    }

    public void setTransactionId(String transactionId){
        this.transactionId = (null == transactionId) ? null : transactionId.trim();
    }
    /*public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request){
        errors.clear();        if (null == langType)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("errors.nameType"));
        }        if (null == criteria)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("errors.companyName"));
        }        if (null == reasonCode)
        {
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("errors.reasonCode"));
        }        return errors;
    }*/
}
