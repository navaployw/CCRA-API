package com.arg.ccra3.models.report;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@JsonPropertyOrder({"executiveSummary", "profile","groupStructure", "court", "revOfConsent" })
public class ReportGeneral extends AbstractReportObject {
    private static final Logger logger = LogManager.getLogger(ReportGeneral.class);
    private Object executiveSummary;
    private List<Object> profile;
    private List<Object> groupStructure;
    private List<Object> court;
    private List<Object> revOfConsent;

    public Object getExecutiveSummary() {
        return executiveSummary;
    }

    public void setExecutiveSummary(Object executiveSummary) {
        this.executiveSummary = executiveSummary;
    }

    public List<Object> getProfile() {
        return profile;
    }

    public void setProfile(List<Object> profile) {
        this.profile = profile;
    }

    public List<Object> getGroupStructure() {
        return groupStructure;
    }

    public void setGroupStructure(List<Object> groupStructure) {
        this.groupStructure = groupStructure;
    }

    public List<Object> getCourt() {
        return court;
    }

    public void setCourt(List<Object> court) {
        this.court = court;
    }

    public List<Object> getRevOfConsent() {
        return revOfConsent;
    }

    public void setRevOfConsent(List<Object> revOfConsent) {
        this.revOfConsent = revOfConsent;
    }

    @Override
    public String toString() {
        return "ReportGeneral{" + "executiveSummary=" + executiveSummary + ", profile=" + profile + ", groupStructure=" + groupStructure + ", court=" + court + ", revOfConsent=" + revOfConsent + '}';
    }

    @Override
    public void setNullToPropertiesIfNoValue() {
        logger.info("before simp executiveSummary: " + executiveSummary);
        setNullToMapPropertiesIfNoValue(executiveSummary);
        logger.info("after simp executiveSummary: " + executiveSummary);
        setNullToElementsIfZeroOrBlankString(profile);
        setNullToElementsIfZeroOrBlankString(groupStructure);
        setNullToElementsIfZeroOrBlankString(court);
        setNullToElementsIfZeroOrBlankString(revOfConsent);    
    }
}
