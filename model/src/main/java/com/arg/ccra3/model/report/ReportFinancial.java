package com.arg.ccra3.model.report;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"positiveLoan", "negativeLoan", "writeOff" })
public class ReportFinancial extends AbstractReportObject {
    private PositiveLoan positiveLoan;
    private Object negativeLoan;
    private Object writeOff;

    public PositiveLoan getPositiveLoan() {
        return positiveLoan;
    }

    public void setPositiveLoan(PositiveLoan positiveLoan) {
        this.positiveLoan = positiveLoan;
    }

    public Object getNegativeLoan() {
        return negativeLoan;
    }

    public void setNegativeLoan(Object negativeLoan) {
        this.negativeLoan = negativeLoan;
    }

    public Object getWriteOff() {
        return writeOff;
    }

    public void setWriteOff(Object writeOff) {
        this.writeOff = writeOff;
    }

    @Override
    public String toString() {
        return "ReportFinancial{" + "positiveLoan=" + positiveLoan + ", negativeLoan=" + negativeLoan + ", writeOff=" + writeOff + '}';
    }

    @Override
    public void setNullToPropertiesIfNoValue() {
        if(positiveLoan != null)
            positiveLoan.setNullToPropertiesIfNoValue();
        
        setNullToMapPropertiesIfNoValue(negativeLoan);
        setNullToMapPropertiesIfNoValue(writeOff);
    }
    
}
