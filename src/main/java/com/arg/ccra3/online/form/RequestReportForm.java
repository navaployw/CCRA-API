package com.arg.ccra3.online.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestReportForm {

    private static final String CRLF = "\r\n";


    protected Log log = LogFactory.getLog(this.getClass());
    protected String aiRefCode1;
    protected String aiRefCode2;
    protected String aiRefCode3;
    protected String reportRefNumb;
    protected Integer cbuid;
    protected String companyName;
    protected String enquiryBuyer;
    protected Long expenseID;
    protected String monitorBuyer;
    protected Integer reasonCode;
    protected String reasonCodeDesc;
    protected String recordPerPage;
    protected String requestName;
    protected Long transactionId;
    protected String typeOfReport;
    protected Integer reportType;
    protected String placeRegist;
    protected String idType;
    protected String idNumber;
    protected boolean basic = false;
    protected boolean basicOrdered = false;
    protected boolean charge = false;
    protected boolean chinese = false;
    protected boolean chineseAvailable = false;
    protected boolean enquiry = false;
    protected boolean enquiryOrdered = false;
    protected boolean monAutoRenew = true;
    protected boolean monitorOrdered = false;
    protected boolean monitoring = false;
    protected boolean newSearch = false;
    protected boolean sevenDayBasic = false;
    protected boolean sevenDayChinese = false;
    protected boolean yourEnquiry = false;
    protected boolean yourMonitoring = false;


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
    public String getReportRefNumb()
    {
        return reportRefNumb;
    }
    public Integer getCbuid()
    {
        return cbuid;
    }
    public String getCompanyName()
    {
        return companyName;
    }
    public String getEnquiryBuyer()
    {
        return enquiryBuyer;
    }
    public Long getExpenseID()
    {
        return expenseID;
    }
    public String getMonitorBuyer()
    {
        return monitorBuyer;
    }
    public Integer getReasonCode()
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
    public String getRequestName()
    {
        return requestName;
    }
    public Long getTransactionId()
    {
        return transactionId;
    }
    public String getTypeOfReport()
    {
        return typeOfReport;
    }
    public boolean isBasic()
    {
        return basic;
    }
    public boolean isBasicOrdered()
    {
        return basicOrdered;
    }
    public boolean isCharge()
    {
        return charge;
    }
    public boolean isChinese()
    {
        return chinese;
    }
    public boolean isChineseAvailable()
    {
        return chineseAvailable;
    }
    public boolean isEnquiry()
    {
        return enquiry;
    }
    public boolean isEnquiryOrdered()
    {
        return enquiryOrdered;
    }
    public boolean isMonAutoRenew()
    {
        return monAutoRenew;
    }
    public boolean isMonitorOrdered()
    {
        return monitorOrdered;
    }
    public boolean isMonitoring()
    {
        return monitoring;
    }
    public boolean isNewSearch()
    {
        return newSearch;
    }
    public boolean isSevenDayBasic()
    {
        return sevenDayBasic;
    }
    public boolean isSevenDayChinese()
    {
        return sevenDayChinese;
    }
    public boolean isYourEnquiry()
    {
        return yourEnquiry;
    }
    public boolean isYourMonitoring()
    {
        return yourMonitoring;
    }
    public void setAiRefCode1(String string)
    {
        aiRefCode1 = string;
    }
    public void setAiRefCode2(String string)
    {
        aiRefCode2 = string;
    }
    public void setAiRefCode3(String string)
    {
        aiRefCode3 = string;
    }
    public void setReportRefNumb(String reportRefNumb)
    {
        this.reportRefNumb = reportRefNumb;
    }
    public void setBasic(boolean b)
    {
        basic = b;
    }
    public void setBasicOrdered(boolean b)
    {
        basicOrdered = b;
    }
    public void setCbuid(Integer cbuid)
    {
        this.cbuid = cbuid;
    }
    public void setCharge(boolean b)
    {
        charge = b;
    }
    public void setChinese(boolean b)
    {
        chinese = b;
    }
    public void setChineseAvailable(boolean b)
    {
        chineseAvailable = b;
    }
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    public void setEnquiry(boolean b)
    {
        enquiry = b;
    }
    public void setEnquiryBuyer(String enquiryBuyer)
    {
        this.enquiryBuyer = enquiryBuyer;
    }
    public void setEnquiryOrdered(boolean enquiryOrdered)
    {
        this.enquiryOrdered = enquiryOrdered;
    }

    public void setExpenseID(Long expenseID)
    {
        this.expenseID = expenseID;
    }
    public void setMonAutoRenew(boolean monAutoRenew)
    {
        this.monAutoRenew = monAutoRenew;
    }
    public void setMonitorBuyer(String monitorBuyer)
    {
        this.monitorBuyer = monitorBuyer;
    }
    public void setMonitorOrdered(boolean monitorOrdered)
    {
        this.monitorOrdered = monitorOrdered;
    }
    public void setMonitoring(boolean b)
    {
        monitoring = b;
    }
    public void setNewSearch(boolean b)
    {
        newSearch = b;
    }
    public void setReasonCode(Integer reasonCode)
    {
        this.reasonCode = reasonCode;
    }
    public void setReasonCodeDesc(String string)
    {
        this.reasonCodeDesc = string;
    }
    public void setRecordPerPage(String string)
    {
        recordPerPage = string;
    }
    public void setRequestName(String requestName)
    {
        this.requestName = requestName;
    }
    public void setSevenDayBasic(boolean b)
    {
        sevenDayBasic = b;
    }
    public void setSevenDayChinese(boolean b)
    {
        sevenDayChinese = b;
    }
    public void setTransactionId(Long string)
    {
        transactionId = string;
    }
    public void setTypeOfReport(String typeOfReport)
    {
        this.typeOfReport = typeOfReport;
    }
    public void setYourEnquiry(boolean yourEnquiry)
    {
        this.yourEnquiry = yourEnquiry;
    }
    public void setYourMonitoring(boolean yourMonitoring)
    {
        this.yourMonitoring = yourMonitoring;
    }
    public String toString()
    {
        StringBuffer buf = new StringBuffer();

        buf.append("cbuid: ").append(cbuid).append(CRLF);
        buf.append("companyName: ").append(companyName).append(CRLF);
        buf.append("requestName: ").append(requestName).append(CRLF);
        buf.append("reasonCode: ").append(reasonCode).append(CRLF);
        buf.append("reasonCodeDesc: ").append(reasonCodeDesc).append(CRLF);
        buf.append("aiRefCode1: ").append(aiRefCode1).append(CRLF);
        buf.append("aiRefCode2: ").append(aiRefCode2).append(CRLF);
        buf.append("aiRefCode3: ").append(aiRefCode3).append(CRLF);
        buf.append("reportRefNumb: ").append(reportRefNumb).append(CRLF);
        buf.append("recordPerPage: ").append(recordPerPage).append(CRLF);
        buf.append("typeOfReport: ").append(typeOfReport).append(CRLF);
        buf.append("newSearch: ").append(newSearch).append(CRLF);
        buf.append("charge: ").append(charge).append(CRLF);
        buf.append("basic: ").append(basic).append(CRLF);
        buf.append("chinese: ").append(chinese).append(CRLF);
        buf.append("monitoring: ").append(monitoring).append(CRLF);
        buf.append("enquiry: ").append(enquiry).append(CRLF);
        buf.append("basicOrdered: ").append(basicOrdered).append(CRLF);
        buf.append("sevenDayBasic: ").append(sevenDayBasic).append(CRLF);
        buf.append("sevenDayChinese: ").append(sevenDayChinese).append(CRLF);
        buf.append("chineseAvailable: ").append(chineseAvailable).append(CRLF);
        buf.append("expenseID: ").append(expenseID).append(CRLF);
        buf.append("typeOfReport: ").append(typeOfReport).append(CRLF);
        buf.append("transactionId: ").append(transactionId).append(CRLF);
        buf.append("enquiryBuyer: ").append(enquiryBuyer).append(CRLF);
        buf.append("monitoringBuyer: ").append(monitorBuyer).append(CRLF);
        buf.append("yourEnquiry: ").append(yourEnquiry).append(CRLF);
        buf.append("yourMonitoring: ").append(yourMonitoring).append(CRLF);

        return buf.toString();
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getPlaceRegist() {
        return placeRegist;
    }

    public void setPlaceRegist(String placeRegist) {
        this.placeRegist = placeRegist;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    
    
}
