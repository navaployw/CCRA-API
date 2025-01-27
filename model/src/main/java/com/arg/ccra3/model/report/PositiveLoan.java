package com.arg.ccra3.model.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class PositiveLoan extends AbstractReportObject {
    private Object summary;
    private List<Object> submitterWithOutFacCancel;
    private List<Object> submitterWithFacCancel;

    public Object getSummary() {
        return summary;
    }

    public void setSummary(Object summary) {
        this.summary = summary;
    }

    public List<Object> getSubmitterWithOutFacCancel() {
        return submitterWithOutFacCancel;
    }

    public void setSubmitterWithOutFacCancel(List<Object> submitterWithOutFacCancel) {
        this.submitterWithOutFacCancel = submitterWithOutFacCancel;
    }

    public List<Object> getSubmitterWithFacCancel() {
        return submitterWithFacCancel;
    }

    public void setSubmitterWithFacCancel(List<Object> submitterWithFacCancel) {
        this.submitterWithFacCancel = submitterWithFacCancel;
    }

    @Override
    public void setNullToPropertiesIfNoValue() {
        skipKey.add("totalFac");
        setNullToMapPropertiesIfNoValue(summary);
        setNullToElementsIfZeroOrBlankString(submitterWithOutFacCancel);
        setNullToElementsIfZeroOrBlankString(submitterWithFacCancel);
    }
    
}
