package com.arg.ccra3.models.report;

import com.arg.cb2.inquiry.DateUtil;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@JsonPropertyOrder({"header", "general","financial", "dataBlock" })
public class ReportBody extends AbstractReportObject {
    private Object header;
    private ReportGeneral general;
    private ReportFinancial financial;
    private List<Object> dataBlock;

    public Object getHeader() {
        return header;
    }

    public void setHeader(Object header) {
        this.header = header;
    }

    public ReportGeneral getGeneral() {
        return general;
    }

    public void setGeneral(ReportGeneral general) {
        this.general = general;
    }

    public ReportFinancial getFinancial() {
        return financial;
    }

    public void setFinancial(ReportFinancial financial) {
        this.financial = financial;
    }

    public List<Object> getAlternativeData() {
        return dataBlock;
    }

    public void setAlternativeData(List<Object> dataBlock) {
        this.dataBlock = dataBlock;
    }

    @Override
    public String toString() {
        return "ReportBody{" + "header=" + header + ", general=" + general + ", financial=" + financial + ", dataBlock=" + dataBlock + '}';
    }

    @Override
    public void setNullToPropertiesIfNoValue() {
        setNullToMapPropertiesIfNoValue(header);
        if(general != null)
            general.setNullToPropertiesIfNoValue();
        if(financial != null)
            financial.setNullToPropertiesIfNoValue();
        setNullToElementsIfZeroOrBlankString(dataBlock);
    }
    
    public void simplfyReportBody(){
        if(header instanceof Map map){
            tryFormattingReportIssueDate(map);
            
            boolean hasData = map.get("ccraData").toString().equals("Y");
            if(hasData)
                simplfyEachIfEmpty();
            else{
                general = null;
                financial = null;
            }
        }
    }    
    private void simplfyEachIfEmpty(){
        if(general != null && general.getExecutiveSummary() == null
                && general.getCourt() == null
                && general.getGroupStructure()== null
                && general.getProfile()== null
                && general.getRevOfConsent()== null)
            general = null;
        
        if(financial != null && financial.getNegativeLoan() == null
                && financial.getPositiveLoan() == null
                && financial.getWriteOff() == null)
            financial = null;
    }    
    private void tryFormattingReportIssueDate(Map<String, Object> map){
        try{
            if(map.containsKey("reportIssuedDate"))
                map.put("reportIssuedDate", DateUtil.convertToString(
                    new Date(((Timestamp)map.get("reportIssuedDate")).getTime()), Locale.ENGLISH, DateUtil.FULL_FORMAT));
        }
        catch(Exception e){}
    }
}
