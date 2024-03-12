

package com.arg.ccra3.dao.util.html;

import com.arg.cb2.inquiry.BaseCreditType;
import com.arg.cb2.inquiry.BaseTangibleSecurity;
import com.arg.cb2.inquiry.DateFormatException;
import com.arg.cb2.inquiry.DateUtil;
import com.arg.cb2.inquiry.ErrorMessages;
import com.arg.cb2.inquiry.ExportFileElements;
import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.ReportDataUtility;
import com.arg.cb2.inquiry.ReportRequest;
import com.arg.cb2.inquiry.bulk.BulkConstants;
import com.arg.cb2.inquiry.data.*;
import com.arg.cb2.spm.core.SPMConstants;
import com.arg.cb2.util.XMLUtil;
import static com.arg.ccra3.common.InquiryConstants.OBJECT_TYPE.*;

import com.arg.ccra3.dao.HTMLReportCreatorDAO;
import com.arg.ccra3.dao.util.ReportResourceJson;
import com.arg.util.GenericException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExportXML implements ExportFileElements
{
        
    private Long expenseID = null;
    private long transactionID;
    private PreferLanguage preLang = PreferLanguage.ENGLISH;
    private BaseCreditType bCreditType = null;
    private ReportRequest reportRequest = null;

    private int reportTypeID = 0;
    private ReportHeaderData rhd = null;
    private BaseTangibleSecurity bTangibleSecurity = null;
    private HashMap httpServletRequest = null;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private StringBuffer sb = new StringBuffer();
    private int reportVersion = 1;
    private int minorReportVersion = 0;
    private String reasonCodeLabel;
    private final  SimpleDateFormat simpleDateFormatNegativeReport2 =
            new SimpleDateFormat("MMMMMMMMMM yyyy", Locale.ENGLISH);
    private static final Log log = LogFactory.getLog(ExportXML.class);   

    public ExportXML(BaseCreditType bCreditType,
        String reasonCodeLabel,
        BaseTangibleSecurity bTangibleSecurity,
        ReportRequestImpl reportRequest,
        ReportHeaderData rhd)
    {
        this.bCreditType = bCreditType;
        this.reasonCodeLabel = reasonCodeLabel;
        this.bTangibleSecurity = bTangibleSecurity;
        this.reportRequest = reportRequest;
        this.rhd = rhd;
    }

    public int getReportVersion()
    {

        return reportVersion;
    }

    public int getMinorReportVersion()
    {
        return minorReportVersion;
    }

    public void setExpenseID(final Long expenseID)
    {
        this.expenseID = expenseID;
    }

    public void setTransactionID(long transactionID)
    {
        this.transactionID = transactionID;
    }

    
    public void setBaseCreditType(final BaseCreditType bCreditType)
    {
        this.bCreditType = bCreditType;
    }

    
    public ReportRequest getReportRequest() {
        return reportRequest;
    }

    public void setReportRequest(ReportRequest reportRequest) {
        this.reportRequest = reportRequest;
    }
    public void setHeaderReport(ReportHeaderData rhd) {
        this.rhd = rhd;
    }

    private void validateRequireLable()
            throws GenericException
    {
        if (null == bCreditType || null == bTangibleSecurity)
        {
            throw new GenericException("ICE-A00016",
                    ErrorMessages.getString("ICE-A00016"));
        }
    }

    public void setHttpServletRequest(HashMap httpServletRequest)
    {
        this.httpServletRequest = httpServletRequest;
    }

    public StringBuffer parseXML()
            throws Exception
    {
        Xml4HtmlSection section = new Xml4HtmlSection(preLang,
                httpServletRequest, rhd, reportTypeID);
        return section.toXML(sb);
    }

    public StringBuffer toXML()
            throws Exception
    {
        StringBuffer retSb = new StringBuffer();
        retSb.append(ExportFileElements.XML_HEADER);
        toXMLNoEncHeader(retSb);
        return retSb;
    }

    protected void toXMLNoEncHeader(StringBuffer retSb)
            throws Exception
    {
        retSb.append(ExportFileElements.XML_REPORT_TYPE_TAG_OPEN);
        retSb.append(sb);
        retSb.append(ExportFileElements.XML_REPORT_TAG_CLOSE);
    }

    public void initReport()
            throws Exception
    {

        validateRequireLable();
        sb.setLength(0);
        
        try
        {
            log.info("####----1");
            preLang = rhd.getPreLang();
            log.info("####----2");
            reportVersion = rhd.getReportVersion();
            log.info("####----3");
            minorReportVersion = rhd.getMinorReportVersion();
            log.info("####----4");
        }
        catch (Exception e)
        {
            sb.append("\"\"" + ExportFileElements.XML_CLOSE_ELEMENT_TAG);

            throw new GenericException("ICE-A00018",
                    ErrorMessages.getString("ICE-A00018",
                            expenseID.toString()));
        }

        if (null == rhd)
        {
            sb.append("\"\"" + ExportFileElements.XML_CLOSE_ELEMENT_TAG);

            throw new GenericException("ICE-A00018",
                    ErrorMessages.getString("ICE-A00018",
                            expenseID.toString()));
        }

        try
        {
            reportTypeID = switch(rhd.getObjectID()){
                case CHINESE_REPORT_API -> CHINESE_REPORT;
                default -> BASIC_REPORT;
            };
        }
        catch (Exception e)
        {
            sb.append("\"\"" + ExportFileElements.XML_CLOSE_ELEMENT_TAG);

            throw new GenericException("ICE-A00019",
                    ErrorMessages.getString("ICE-A00019",
                            expenseID.toString()));
        }
        try
        {
            switch (reportTypeID) {
                case CHINESE_REPORT ->
                    XMLUtil.getXml(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.BASIC_LABEL_CHINESE), sb);
                case BASIC_REPORT ->
                    XMLUtil.getXml(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.BASIC_LABEL_BASIC), sb);
                case SEARCH ->
                    XMLUtil.getXml(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.BASIC_LABEL_NO_TYPE), sb);
                default ->
                    XMLUtil.getXml(ExportFileElements.BLANK, sb);
            }
        }
        catch (Exception e)
        {
            sb.append("\"\"" + ExportFileElements.XML_CLOSE_ELEMENT_TAG);

            throw new GenericException("ICE-A00019",
                    ErrorMessages.getString("ICE-A00019",
                            expenseID.toString()));
        }

        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
        getHeaderXML();

        if (null == expenseID
                ||
                MONITORING_ALERT == reportTypeID)
        {

            return;
        }

        getGeneralInforXML();
        getFinanceXML();


    }

    private void getHeaderXML()
    {
        sb.append(ExportFileElements.OPEN_HEADER_TAG);

        try
        {
            XMLUtil.getCData(rhd.getAiRefCode1(),
                    ExportFileElements.AI_REF_CODE_1, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.AI_REF_CODE_1, sb);
        }

        try
        {
            XMLUtil.getCData(rhd.getAiRefCode2(),
                    ExportFileElements.AI_REF_CODE_2, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.AI_REF_CODE_2, sb);
        }

        try
        {
            XMLUtil.getCData(rhd.getAiRefCode3(),
                    ExportFileElements.AI_REF_CODE_3, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.AI_REF_CODE_3, sb);
        }

        try
        {
            XMLUtil.getCData(DateUtil.convertToString(
                    rhd.getReportDeliveredDate(), Locale.ENGLISH,
                    DateUtil.FULL_FORMAT),
                    ExportFileElements.REPORT_DELIVER_DATE, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.REPORT_DELIVER_DATE, sb);
        }

        try
        {
            XMLUtil.getCData(reasonCodeLabel, ExportFileElements.REASON_CODE, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.REASON_CODE, sb);
        }
        
        if (reportVersion == 2)
        {
            try
            {
                XMLUtil.getCData(rhd.getReportRefNumb(),
                        ExportFileElements.REPORT_REF_NUMBER, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.REPORT_REF_NUMBER,
                        sb);
            }
        }
        appendSAIOrderLists();

        sb.append(ExportFileElements.CLOSE_HEADER_TAG);
    }

    private void appendSAIOrderLists()
    {
        SAIOrderedLists saiOLs = rhd.getSaiOrderedLists();

        if (null == saiOLs)
        {
            return;
        }
        Boolean nonAIFlag = false;
        if ((rhd.getAIID() != null) &&
                (rhd.getAIID().intValue() != SPMConstants.GroupContants.CCRA_ID))
        {
            XMLUtil.appendOpenElementWithAttributes(sb, VIEW_REPORT_LIST);
            XMLUtil.appendAttribute(sb, NON_AI_FLAG, nonAIFlag.toString());
            XMLUtil.appendTagGT(sb);
            XMLUtil.appendCloseElement(sb, VIEW_REPORT_LIST);
            return;
        }

        nonAIFlag = true;

        SAIOrderedList[] saiOL = saiOLs.getAIOrderedLists();

        if (null == saiOL || 0 == saiOL.length)
        {
            XMLUtil.appendOpenElementWithAttributes(sb, VIEW_REPORT_LIST);
            XMLUtil.appendAttribute(sb, NON_AI_FLAG, nonAIFlag.toString());
            XMLUtil.appendTagGT(sb);
            XMLUtil.appendCloseElement(sb, VIEW_REPORT_LIST);
            return;
        }

        XMLUtil.appendOpenElementWithAttributes(sb, VIEW_REPORT_LIST);
        XMLUtil.appendAttribute(sb, NON_AI_FLAG, nonAIFlag.toString());
        XMLUtil.appendTagGT(sb);

        for (int i = 0; i < saiOL.length; i++)
        {
            if (null == saiOL[i])
            {
                continue;
            }
            XMLUtil.appendOpenElement(sb, REQUESTED_REPORT);
            try
            {
                XMLUtil.appendCData(
                        DateUtil.convertToString(saiOL[i].getOrderDate(),
                                Locale.ENGLISH, DateUtil.FULL_FORMAT),
                        ENQUIRY_DATE, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ENQUIRY_DATE, sb);
            }
            XMLUtil.appendCData(saiOL[i].getAIName(), REQUEST_REPORT_AI_NAME,
                    sb);
            XMLUtil.appendCData(saiOL[i].getProduct(), PRODUCT_TYPE, sb);
            XMLUtil.appendCData(saiOL[i].getReason(), ENQUIRY_REASON, sb);
            XMLUtil.appendCloseElement(sb, REQUESTED_REPORT);
        }
        XMLUtil.appendCloseElement(sb, VIEW_REPORT_LIST);
    }

    private void getGeneralInforXML()
            throws Exception
    {
        sb.append(ExportFileElements.XML_OPEN_ELEMENT_TAG + ExportFileElements
                .GENERAL_INFORMATION + ExportFileElements
                .XML_CLOSE_ELEMENT_TAG);
        try
        {
            getExecutiveSummaryXML();
            getProfilesXML();
            getComStructuresXML();
            getCourtXML();
            getNoticeOfConsentXML();
        }
        finally
        {
            sb.append(ExportFileElements.XML_OPEN_END_ELEMENT_TAG +
                    ExportFileElements.GENERAL_INFORMATION + ExportFileElements
                    .XML_CLOSE_ELEMENT_TAG);
        }
    }

    private void getExecutiveSummaryXML()
            throws Exception
    {
        SExecutiveSummary es = null;
        try
        {
            es = reportRequest.getExecutiveSummary(expenseID);
        }
        catch (Exception e)
        {
            log.error(e);
            throw e;
        }

        if (null == es)
        {
            return;
        }

        sb.append(ExportFileElements.XML_OPEN_ELEMENT_TAG + ExportFileElements
                .EXECUTIVE_SUMMARY + ExportFileElements.XML_CLOSE_ELEMENT_TAG);
        if (reportVersion == 2)
        {
            try
            {
                String fDate = DateUtil.convertToString(es.getFirstReportDate(),
                        Locale.ENGLISH);

                if (ExportFileElements.BLANK.equals(fDate))
                {
                    XMLUtil.getCData(rhd.getCompanyName(),
                            ExportFileElements.EXEC_COMPANY_NAME, sb);
                }
                else
                {
                    XMLUtil.getBlankElement(
                            ExportFileElements.EXEC_COMPANY_NAME, sb);
                }
            }
            catch (Exception e)
            {
                XMLUtil.getCData(rhd.getCompanyName(),
                        ExportFileElements.EXEC_COMPANY_NAME, sb);
            }
        }

        sb.append(ExportFileElements.XML_OPEN_ELEMENT_TAG + ExportFileElements
                .RESUBMISSIONS + ExportFileElements.XML_CLOSE_ELEMENT_TAG);

        SResubmitted[] sr = null;
        sr = es.getResubmitted();
        if (null != sr)
        {
            for (int i = 0; i < sr.length; i++)
            {
                if (null == sr[i])
                {
                    continue;
                }
                sb.append(ExportFileElements.XML_OPEN_ELEMENT_TAG +
                        ExportFileElements.RESUBMISSION + ExportFileElements
                        .XML_CLOSE_ELEMENT_TAG);
                XMLUtil.getCData(sr[i].getSubmittedBy(),
                        ExportFileElements.SUBMITTER, sb);
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(sr[i].getPeriod(),
                            Locale.ENGLISH), ExportFileElements.PERIOD, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.PERIOD, sb);
                }
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            sr[i].getResendDate(), Locale.ENGLISH,
                            DateUtil.LONG_FORMAT), ExportFileElements.DATE, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.DATE, sb);
                }
                sb.append(ExportFileElements.XML_OPEN_END_ELEMENT_TAG +
                        ExportFileElements.RESUBMISSION + ExportFileElements
                        .XML_CLOSE_ELEMENT_TAG);
            }
        }
        sb.append(ExportFileElements.XML_OPEN_END_ELEMENT_TAG +
                ExportFileElements.RESUBMISSIONS + ExportFileElements
                .XML_CLOSE_ELEMENT_TAG);

        sb.append(ExportFileElements.UNLOADS_OPEN_TAG);
        SUnloaded[] ul = es.getUnloaded();
        if (null != ul)
        {
            for (int i = 0; i < ul.length; i++)
            {
                if (null == ul[i])
                {
                    continue;
                }
                sb.append(ExportFileElements.UNLOAD_TAG_OPEN);
                XMLUtil.getCData(ul[i].getSubmittedBy(),
                        ExportFileElements.SUBMITTER, sb);
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(ul[i].getPeriod(),
                            Locale.ENGLISH), ExportFileElements.PERIOD, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.PERIOD, sb);
                }
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            ul[i].getUnloadDate(), Locale.ENGLISH,
                            DateUtil.LONG_FORMAT), ExportFileElements.DATE, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.DATE, sb);
                }
                sb.append(ExportFileElements.UNLOAD_TAG_CLOSE);
            }
        }
        sb.append(ExportFileElements.UNLOADS_CLOSE_TAG);

        if (reportVersion == 2)
        {

            sb.append(ExportFileElements.UPDATES_OPEN_TAG);
            SUpdated[] ud = es.getUpdated();
            if (null != ud)
            {
                for (int i = 0; i < ud.length; i++)
                {
                    if (null == ud[i])
                    {
                        continue;
                    }
                    sb.append(ExportFileElements.UPDATE_OPEN_TAG);
                    XMLUtil.getCData(ud[i].getSubmittedBy(),
                            ExportFileElements.SUBMITTER, sb);
                    try
                    {
                        XMLUtil.getCData(DateUtil.convertToString(ud[i].getPeriod(),
                                Locale.ENGLISH), ExportFileElements.PERIOD, sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(ExportFileElements.PERIOD, sb);
                    }
                    try
                    {
                        XMLUtil.getCData(DateUtil.convertToString(
                                ud[i].getUpdateDate(), Locale.ENGLISH,
                                DateUtil.LONG_FORMAT), ExportFileElements.DATE, sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(ExportFileElements.DATE, sb);
                    }
                    sb.append(ExportFileElements.UPDATE_CLOSE_TAG);
                }
            }
            sb.append(ExportFileElements.UPDATES_CLOSE_TAG);
        }

        try
        {
            XMLUtil.getCData(DateUtil.convertToString(
                    es.getFirstReportDate(),
                    Locale.ENGLISH), ExportFileElements.FIRST_REPORT, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.FIRST_REPORT, sb);
        }

        sb.append(ExportFileElements.SUBMITTERS_TAG_OPEN);
        try
        {
            XMLUtil.getCData(
                    DateUtil.convertToString(rhd.getReportDeliveredDate(),
                            Locale.ENGLISH, DateUtil.SHORT_FORMAT),
                    ExportFileElements.PERIOD, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.PERIOD, sb);
        }

        try
        {
            XMLUtil.getCData(es.getAIsReportNumber(), ExportFileElements.TOTAL,
                    sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.TOTAL, sb);
        }
        sb.append(ExportFileElements.SUBMITTERS_TAG_CLOSE);
        sb.append(ExportFileElements.REV_CONSENTS_TAG_OPEN);
        SNoticeOfConsent[] snocs = es.getNoticeOfRevocationOfConsent();

        if (null != snocs)
        {
            int count = 0;
            for (int i = 0; i < snocs.length; i++)
            {
                if (null == snocs[i])
                {
                    continue;
                }
                count++;
                sb.append(ExportFileElements.REV_CONSENTS_TAG_OPEN_WITHID);
                XMLUtil.getXml(count, sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                XMLUtil.getCData(snocs[i].getSubmittedBy(),
                        ExportFileElements.SUBMITTER, sb);
                XMLUtil.getCData(
                        DateUtil.convertToString(snocs[i].getNoticedDate(),
                                Locale.ENGLISH,
                                DateUtil.LONG_FORMAT),
                        ExportFileElements.NOTICE_RECEIVE_ON, sb);
                XMLUtil.getCData(
                        DateUtil.convertToString(snocs[i].getEffectedDate(),
                                Locale.ENGLISH,
                                DateUtil.LONG_FORMAT),
                        ExportFileElements.EFFECTIVE_ON, sb);
                sb.append(ExportFileElements.REV_CONSENT_TAG_CLOSE);
            }
        }
        sb.append(ExportFileElements.REV_CONSENTS_TAG_CLOSE);
        sb.append(ExportFileElements.EVIDENCE_OF_COURT_TAG_OPEN);
        SEvidanceOfCourt[] ecs = es.getEvidanceOfCourt();
        if (null != ecs)
        {
            for (int i = 0; i < ecs.length; i++)
            {
                if (null != ecs[i])
                {
                    sb.append(ExportFileElements.COURT_TAG_OPEN);
                    XMLUtil.getCData(DateUtil.convertToString(
                            ecs[i].getReportDate(), Locale.ENGLISH,
                            DateUtil.LONG_FORMAT), ExportFileElements.DATE, sb);
                    XMLUtil.getCData(ecs[i].getTotal(),
                            ExportFileElements.TOTAL, sb);
                    sb.append(ExportFileElements.COURT_TAG_CLOSE);
                }
            }
        }
        sb.append(ExportFileElements.EVIDENCE_OF_COURT_TAG_CLOSE);

        sb.append(ExportFileElements.PASTDUE_TAG_OPEN);
        try
        {
            XMLUtil.getCData(DateUtil.convertToString(
                    es.getTotalPastDueAmount60DaysPeriod(), Locale.ENGLISH),
                    PERIOD, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(PERIOD, sb);
        }
        try
        {
            XMLUtil.getCData(es.getTotalPastDueAmount60Days(), AMOUNT, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(AMOUNT, sb);
        }
        sb.append(ExportFileElements.PASTDUE_TAG_CLOSE);

        sb.append(ExportFileElements.DISPUTES_TAG_OPEN);
        SDispute[] ds = es.getDispute();
        if (null != ds)
        {
            int id = 0;
            for (int i = 0; i < ds.length; i++)
            {
                if (null == ds[i])
                {
                    continue;
                }
                id++;
                sb.append(ExportFileElements.DISPUTE_WITH_ID_TAG_OPEN);
                XMLUtil.getXml(id, sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                XMLUtil.getCData(ds[i].getSubmittedBy(), SUBMITTER, sb);
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            ds[i].getDisputedDate(), Locale.ENGLISH,
                            DateUtil.LONG_FORMAT), DATE, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(DATE, sb);
                }
                sb.append(ExportFileElements.DISPUTE_TAG_CLOSE);
            }
        }
        sb.append(ExportFileElements.DISPUTES_TAG_CLOSE);

        sb.append(ExportFileElements.RECOVERED_TAG_OPEN);
        try
        {
            XMLUtil.getCData(DateUtil.convertToString(es.getRecoveredPeriod(),
                    Locale.ENGLISH), PERIOD, sb);
        }
        catch (DateFormatException e)
        {
            XMLUtil.getBlankElement(PERIOD, sb);
        }
        try
        {
            XMLUtil.getCData(es.getRecoveredAmount(), AMOUNT, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(AMOUNT, sb);
        }
        sb.append(ExportFileElements.RECOVERED_TAG_CLOSE);
        getExecutiveFacilitiesCancel(es);
        sb.append(ExportFileElements.EXECUTIVE_SUMMARY_TAG_CLOSE);
    }

    private void getExecutiveFacilitiesCancel(SExecutiveSummary es)
    {
        sb.append(ExportFileElements.FACILITIES_TAG_OPEN);
        SFacilitiesCancel[] fc = es.getFacilitiesCancel();
        if (null != fc)
        {
            for (int i = 0; i < fc.length; i++)
            {
                if (null == fc[i])
                {
                    continue;
                }
                String status = fc[i].getStatus();
                sb.append(ExportFileElements.FACILITY_ID_TAG_OPEN);
                XMLUtil.getXml(status, sb);
                sb.append(ExportFileElements.IS_WRITE_OFF);
                XMLUtil.getXml(fc[i].isWriteOff(), sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                XMLUtil.getCData(fc[i].getSubmittedBy(),
                        ExportFileElements.SUBMITTER, sb);
                if (BulkConstants.FAC_CANCEL_DETAIL_STATUS_WRITEOFF_REPAID
                        .equals(status))
                {
                    if (fc[i].isWriteOff())
                    {
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.POSITIVE_LABEL_WRITEOFF),
                                ExportFileElements.STATUS, sb);
                    }
                    else
                    {
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.POSITIVE_LABEL_REPAID),
                                ExportFileElements.STATUS, sb);
                    }
                }
                else if (BulkConstants.FAC_CANCEL_DETAIL_STATUS_WITHDRAW
                        .equals(status))
                {
                    XMLUtil.getCData(ReportResourceJson.getMessage(
                            preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_WITHDRAWN),
                            ExportFileElements.STATUS, sb);
                }
                else if (BulkConstants.FAC_CANCEL_DETAIL_STATUS_CANCEL
                        .equals(status))
                {
                    XMLUtil.getCData(ReportResourceJson.getMessage(
                            preLang.getLocale(),
                            ExportFileElements.EXECUTIVE_LABEL_FAC_CANCEL),
                            ExportFileElements.STATUS, sb);
                }
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(fc[i].getPeriod(),
                            Locale.ENGLISH), ExportFileElements.PERIOD, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.PERIOD, sb);
                }

                sb.append(ExportFileElements.FACILITY_TAG_CLOSE);
            }
        }
        sb.append(ExportFileElements.FACILITIES_TAG_CLOSE);
    }

    private void getProfilesXML()
            throws Exception
    {
        SProfiles pf = null;
        SProfile[] p = null;
        try
        {
            pf = reportRequest.getProfiles(expenseID);
            if (null == pf)
            {
                XMLUtil.getBlankElement(ExportFileElements.PROFILE, sb);
                return;
            }
            p = pf.getProfiles();
        }
        catch (Exception e)
        {
            log.error(e);
            XMLUtil.getBlankElement(ExportFileElements.PROFILE, sb);
            throw e;
        }

        if (null == p || 0 == p.length)
        {
            XMLUtil.getBlankElement(ExportFileElements.PROFILE, sb);
            return;
        }

        SProfileLO p_lo = null;
        int max_count = 0;
        boolean hkci = false;
        boolean hkbrc = false;
        boolean other = false;

        for (int i = 0; i < p.length; i++)
        {
            int ident_count = 0;
            if (null == p[i])
            {
                continue;
            }
            if (null != p[i].getHKBRCNO() &&
                    0 < p[i].getHKBRCNO().trim().length())
            {
                hkbrc = true;
                ident_count++;
            }
            if (null != p[i].getHKCINO() &&
                    0 < p[i].getHKCINO().trim().length())
            {
                hkci = true;
                ident_count++;
            }
            if (null != p[i].getOtherNO() &&
                    0 < p[i].getOtherNO().trim().length())
            {
                other = true;
                ident_count++;
            }
            if (ident_count > max_count)
            {
                max_count = ident_count;
                if (max_count == 3)
                {
                    break;
                }
            }
        }
        sb.append(ExportFileElements.XML_OPEN_ELEMENT_TAG);
        sb.append(ExportFileElements.PROFILE);
        sb.append(ExportFileElements.MAX_IDENTIFIER);
        XMLUtil.getXml(max_count, sb);
        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);

        int count = 0;
        for (int i = 0; i < p.length; i++)
        {
            p_lo = (SProfileLO) p[i];
            if (null == p_lo)
            {
                continue;
            }
            count++;
            sb.append(ExportFileElements.SUBMITTER_ID_TAG_OPEN);
            XMLUtil.getXml(count, sb);
            sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
            XMLUtil.getCData(p_lo.getSubmittedBy(), ExportFileElements.NAME,
                    sb);
            try
            {
                XMLUtil.getCData(DateUtil.convertToString(
                        p_lo.getReportedDate(), Locale.ENGLISH),
                        ExportFileElements.REPORT_DATE, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.REPORT_DATE, sb);
            }
            sb.append(ExportFileElements.IDENTIFIERS_TAG_OPEN);
            sb.append(ExportFileElements.IDENTIFER_TAG_OPEN);
            XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                    ExportFileElements.GEN_PROFILE_LABEL_HKBRC),
                    ExportFileElements.TYPE, sb);
            if (hkbrc && null != p_lo.getHKBRCNO() &&
                    0 < p_lo.getHKBRCNO().trim().length())
            {
                XMLUtil.getCData(p_lo.getHKBRCNO(), ExportFileElements.NUMBER,
                        sb);
            }
            else
            {
                XMLUtil.getBlankElement(ExportFileElements.NUMBER, sb);
            }

            sb.append(ExportFileElements.IDENTIFIER_TAG_CLOSE);

            sb.append(ExportFileElements.IDENTIFER_TAG_OPEN);
            XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                    ExportFileElements.GEN_PROFILE_LABEL_HKCI),
                    ExportFileElements.TYPE, sb);
            if (hkci && null != p_lo.getHKCINO() &&
                    0 < p_lo.getHKCINO().trim().length())
            {
                XMLUtil.getCData(p_lo.getHKCINO(), ExportFileElements.NUMBER,
                        sb);
            }
            else
            {
                XMLUtil.getBlankElement(ExportFileElements.NUMBER, sb);
            }
            sb.append(ExportFileElements.IDENTIFIER_TAG_CLOSE);

            sb.append(ExportFileElements.IDENTIFER_TAG_OPEN);
            XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                    ExportFileElements.GEN_PROFILE_LABEL_OTHER),
                    ExportFileElements.TYPE, sb);
            if (other && null != p_lo.getOtherNO() &&
                    0 < p_lo.getOtherNO().trim().length())
            {
                XMLUtil.getCData(p_lo.getOtherNO(), ExportFileElements.NUMBER,
                        sb);
            }
            else
            {
                XMLUtil.getBlankElement(ExportFileElements.NUMBER, sb);
            }
            sb.append(ExportFileElements.IDENTIFIER_TAG_CLOSE);
            sb.append(ExportFileElements.IDENTIFIERS_TAG_CLOSE);

            if (other && null != p_lo.getOtherNO() &&
                    0 != p_lo.getOtherNO().trim().length())
            {
                try
                {
                    XMLUtil.getCData(p_lo.getPlaceOfRegistration(),
                            ExportFileElements.PLACE_REG, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.PLACE_REG, sb);
                }
            }
            try
            {
                XMLUtil.getCData(p_lo.getCurrentName(),
                        ExportFileElements.COMPANY_NAME, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.COMPANY_NAME, sb);
            }
            if (reportTypeID == BASIC_REPORT)
            {
                if (null != p_lo && null != p_lo.getCurrentNameLO() &&
                        0 != p_lo.getCurrentNameLO().trim().length())
                {
                    XMLUtil.getCData(ReportResourceJson.getMessage(
                            preLang.getLocale(),
                            ExportFileElements.REP_GENERAL_LABEL_CHINESE),
                            ExportFileElements.COMPANY_NAME_LO, sb);
                }
                else
                {
                    XMLUtil.getBlankElement(ExportFileElements.COMPANY_NAME_LO,
                            sb);
                }
            }
            else
            if (reportTypeID == CHINESE_REPORT)
            {
                try
                {
                    XMLUtil.getCData(p_lo.getCurrentNameLO(),
                            ExportFileElements.COMPANY_NAME_LO, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.COMPANY_NAME_LO,
                            sb);
                }
            }
            try
            {
                XMLUtil.getCData(p_lo.getTradeName(),
                        ExportFileElements.TRADE_NAME, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.TRADE_NAME, sb);
            }
            if (reportTypeID == BASIC_REPORT)
            {
                if (null != p_lo && null != p_lo.getTradeNameLO() &&
                        0 != p_lo.getTradeNameLO().trim().length())
                {
                    XMLUtil.getCData(ReportResourceJson.getMessage(
                            preLang.getLocale(),
                            ExportFileElements.REP_GENERAL_LABEL_CHINESE),
                            ExportFileElements.TRADE_NAME_LO, sb);
                }
                else
                {
                    XMLUtil.getBlankElement(ExportFileElements.TRADE_NAME_LO,
                            sb);
                }
            }
            else
            if (reportTypeID == CHINESE_REPORT)
            {
                try
                {
                    XMLUtil.getCData(p_lo.getTradeNameLO(),
                            ExportFileElements.TRADE_NAME_LO, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.TRADE_NAME_LO,
                            sb);
                }
            }
            try
            {
                XMLUtil.getCData(p_lo.getPreviousName(),
                        ExportFileElements.PREVIOUS_NAME, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.PREVIOUS_NAME, sb);
            }
            if (reportTypeID == BASIC_REPORT)
            {
                if (null != p_lo && null != p_lo.getPreviousNameLO() &&
                        0 != p_lo.getPreviousNameLO().trim().length())
                {
                    XMLUtil.getCData(ReportResourceJson.getMessage(
                            preLang.getLocale(),
                            ExportFileElements.REP_GENERAL_LABEL_CHINESE),
                            ExportFileElements.PREVIOUS_NAME_LO, sb);
                }
                else
                {
                    XMLUtil.getBlankElement(ExportFileElements.PREVIOUS_NAME_LO,
                            sb);
                }
            }
            else
            if (reportTypeID == CHINESE_REPORT)
            {
                try
                {
                    XMLUtil.getCData(p_lo.getPreviousNameLO(),
                            ExportFileElements.PREVIOUS_NAME_LO, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.PREVIOUS_NAME_LO,
                            sb);
                }
            }
            XMLUtil.getCData(getEnglishAddress(p_lo),
                    ExportFileElements.ADDRESS, sb);
            if (reportTypeID == BASIC_REPORT)
            {
                if (null != p_lo)
                {
                    if (null != p_lo.getAddress1LO() &&
                            0 != p_lo.getAddress1LO().trim().length())
                    {
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.REP_GENERAL_LABEL_CHINESE),
                                ExportFileElements.ADDRESS_LO, sb);
                    }
                    else
                    {
                        XMLUtil.getBlankElement(ExportFileElements.ADDRESS_LO,
                                sb);
                    }
                }
            }
            else
            if (reportTypeID == CHINESE_REPORT)
            {
                XMLUtil.getCData(getChineseAddress(p_lo),
                        ExportFileElements.ADDRESS_LO, sb);
            }
            XMLUtil.getCData(p_lo.getPhoneNO(), ExportFileElements.PHONE, sb);
            sb.append(ExportFileElements.SUBMITTER_TAG_CLOSE);
        }
        sb.append(ExportFileElements.XML_OPEN_END_ELEMENT_TAG);
        sb.append(ExportFileElements.PROFILE);
        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
    }

    private String getChineseAddress(SProfileLO p_lo)
    {
        if (null == p_lo)
        {
            return "";
        }
        String tmp = "";
        tmp = "";
        if (null != p_lo)
        {
            if (null != p_lo.getAddress1LO() &&
                    0 != p_lo.getAddress1LO().length())
            {
                tmp = p_lo.getAddress1LO();
            }
            else
            {
                return tmp;
            }
            if (null != p_lo.getAddress2LO() &&
                    0 != p_lo.getAddress2LO().length())
            {
                tmp = tmp + "\n" + p_lo.getAddress2LO();
            }
            if (null != p_lo.getAddress3LO() &&
                    0 != p_lo.getAddress3LO().length())
            {
                tmp = tmp + "\n" + p_lo.getAddress3LO();
            }
        }
        return tmp;
    }

    private String getEnglishAddress(SProfileLO p_lo)
    {
        if (null == p_lo)
        {
            return "";
        }
        String tmp = "";
        if (null != p_lo.getAddress1() && 0 != p_lo.getAddress1().length())
        {
            tmp = p_lo.getAddress1();
        }
        else
        {
            return tmp;
        }
        if (null != p_lo.getAddress2() && 0 != p_lo.getAddress2().length())
        {
            tmp = tmp + "\n" + p_lo.getAddress2();
        }
        if (null != p_lo.getAddress3() && 0 != p_lo.getAddress3().length())
        {
            tmp = tmp + "\n" + p_lo.getAddress3();
        }
        return tmp;
    }

    private void getComStructuresXML()
            throws Exception
    {
        SComStructures cs = null;
        try
        {
            cs = reportRequest.getCompanyGroupStructures(expenseID);
        }
        catch (Exception e)
        {
            log.error(e);
            XMLUtil.getBlankElement(ExportFileElements.GROUP_STRUCTURE, sb);
            throw e;
        }

        SComStructure[] css = null;
        if (null == cs ||
                null == (css = cs.getComStructures()) ||
                0 == css.length)
        {
            XMLUtil.getBlankElement(ExportFileElements.GROUP_STRUCTURE, sb);
            return;
        }


        SComStructureLO css_lo = null;
        boolean noData = true;
        for (int i = 0; i < css.length; i++)
        {
            css_lo = (SComStructureLO) css[i];
            if ((null == css_lo ||
                    null == css_lo.getShareholderName() ||
                    0 == css_lo.getShareholderName().trim().length()) &&
                    (null == css_lo ||
                            null == css_lo.getShareholderNameLO() ||
                            0 == css_lo.getShareholderNameLO().trim().length()))
            {
                continue;
            }
            else
            {
                noData = false;
                break;
            }
        }

        if (noData)
        {
            XMLUtil.getBlankElement(ExportFileElements.GROUP_STRUCTURE, sb);
        }
        else
        {

            XMLUtil.appendOpenElement(sb, ExportFileElements.GROUP_STRUCTURE);
            int count = 0;
            for (int i = 0; i < css.length; i++)
            {
                css_lo = (SComStructureLO) css[i];
                if (null == css_lo
                        ||
                        ((null == css_lo.getShareholderName()
                                || 0 ==
                                css_lo.getShareholderName().trim().length())
                                && (null == css_lo.getShareholderNameLO()
                                || 0 ==
                                css_lo.getShareholderNameLO().trim().length())))
                {
                    continue;
                }

                count++;

                sb.append(ExportFileElements.SUBMITTER_ID_TAG_OPEN);
                XMLUtil.getXml(count, sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                XMLUtil.getCData(css_lo.getSubmittedBy(), NAME, sb);
                XMLUtil.getCData(css_lo.getShareholderName(),
                        ExportFileElements.SHAREDHOLDER, sb);
                if (reportTypeID == BASIC_REPORT)
                {
                    if (null != css_lo.getShareholderNameLO() &&
                            0 != css_lo.getShareholderNameLO().trim().length())
                    {
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        REP_GENERAL_LABEL_CHINESE),
                                ExportFileElements.SHAREDHOLDER_LO, sb);
                    }
                    else
                    {
                        XMLUtil.getBlankElement(
                                ExportFileElements.SHAREDHOLDER_LO, sb);
                    }

                }
                else
                if (reportTypeID == CHINESE_REPORT)
                {
                    XMLUtil.getCData((null == css_lo) ? "" :
                            css_lo.getShareholderNameLO(),
                            ExportFileElements.SHAREDHOLDER_LO, sb);
                }
                XMLUtil.appendCloseElement(sb, ExportFileElements.SUBMITTER);
            }
            XMLUtil.appendCloseElement(sb, ExportFileElements.GROUP_STRUCTURE);
        }
    }

    private String getCourtDefendantName(SDefendantLO d_lo)
    {
        String tmp = "";
        if (null == d_lo)
        {
            return "";
        }

        if (null == d_lo.getSurnameEN() ||
                0 == d_lo.getSurnameEN().trim().length())
        {
            if (null == d_lo.getGivennameEN() ||
                    0 == d_lo.getGivennameEN().trim().length())
            {
            }
            else
            {
                tmp = tmp + d_lo.getGivennameEN();
            }
        }
        else
        {
            if (null == d_lo.getGivennameEN() ||
                    0 == d_lo.getGivennameEN().trim().length())
            {
                tmp = tmp + d_lo.getSurnameEN();
            }
            else
            {
                tmp = tmp + d_lo.getSurnameEN() + ExportFileElements.SPACE
                        + ExportFileElements.SPACE + d_lo.getGivennameEN();
            }
        }
        return tmp;
    }

    private void getCourtDefendantNameChinese(SDefendantLO d_lo,
                                              String element)
    {
        if (null == d_lo ||
                ((null == d_lo.getGivennameLO() ||
                        0 == d_lo.getGivennameLO().trim().length()) &&
                        (null == d_lo.getSurnameLO() ||
                                0 == d_lo.getSurnameLO().trim().length())))
        {
            XMLUtil.getBlankElement(element, sb);
        }

        if (null == d_lo.getGivennameLO() ||
                0 == d_lo.getGivennameLO().trim().length())
        {
            if (null == d_lo.getSurnameLO() ||
                    0 == d_lo.getSurnameLO().trim().length())
            {
            }
            else
            {
                XMLUtil.getCData(d_lo.getSurnameLO(), element, sb);
            }
        }
        else
        {
            if (null == d_lo.getSurnameLO() ||
                    0 == d_lo.getSurnameLO().trim().length())
            {
                XMLUtil.getCData(d_lo.getGivennameLO(), element, sb);
            }
            else
            {
                XMLUtil.getCData(
                        d_lo.getSurnameLO() + ExportFileElements.SPACE +
                                d_lo.getGivennameLO(), element, sb);
            }
        }
    }

    private void getCourtDefendant(SCourtLO cs_lo)
    {
        SDefendant[] d = null;
        if (null == cs_lo ||
                null == (d = cs_lo.getDefendants()) ||
                0 == d.length)
        {
            XMLUtil.getBlankElement(ExportFileElements.DEFENDANT, sb);
            return;
        }
        SDefendantLO d_lo = null;

        d_lo = (SDefendantLO) d[0];
        XMLUtil.getCData(getCourtDefendantName(d_lo),
                ExportFileElements.DEFENDANT, sb);

        if (reportTypeID == BASIC_REPORT)
        {
            getDataInChineseDefendant(d_lo, ExportFileElements.DEFENDANT_LO);
        }
        else if (reportTypeID == CHINESE_REPORT)
        {
            getCourtDefendantNameChinese(d_lo, ExportFileElements.DEFENDANT_LO);
        }
        for (int i = 1; i < d.length; i++)
        {
            d_lo = (SDefendantLO) d[i];
            if (null == d_lo)
            {
                continue;
            }
            XMLUtil.getCData(getCourtDefendantName(d_lo),
                    ExportFileElements.OTHER_DEFENDANT, sb);
            if (reportTypeID == BASIC_REPORT)
            {
                getDataInChineseDefendant(d_lo,
                        ExportFileElements.OTHER_DEFENDANT_LO);
            }
            else if (reportTypeID
                    == CHINESE_REPORT)
            {
                getCourtDefendantNameChinese(d_lo,
                        ExportFileElements.OTHER_DEFENDANT_LO);
            }
        }
    }

    private void getDataInChineseDefendant(SDefendantLO d_lo,
                                           String element)
    {
        if (null != d_lo)
        {
            if ((null != d_lo.getSurnameLO() &&
                    0 != d_lo.getSurnameLO().trim().length()) ||
                    (null != d_lo.getGivennameLO() &&
                            0 != d_lo.getGivennameLO().trim().length()))
            {
                XMLUtil.getCData(
                        ReportResourceJson.getMessage(preLang.getLocale(),
                                ExportFileElements.REP_GENERAL_LABEL_CHINESE),
                        element, sb);
            }
            else
            {
                XMLUtil.getBlankElement(element, sb);
            }
        }
        else
        {
            XMLUtil.getBlankElement(element, sb);
        }
    }

    private boolean isNoCourtData(SCourtLO cs_lo)
    {
        if (null == cs_lo || null == cs_lo.getDefendant()
                || 0 == cs_lo.getDefendant().size())
        {
            return true;
        }

        SDefendantLO defendantLO = (SDefendantLO) cs_lo.getDefendant().get(0);

        if (((null == defendantLO.getGivennameEN() ||
                0 == defendantLO.getGivennameEN().trim().length()) &&
                (null == defendantLO.getSurnameEN() ||
                        0 == defendantLO.getSurnameEN().trim().length())) &&
                (null == defendantLO.getGivennameLO() ||
                        0 == defendantLO.getGivennameLO().trim().length()) &&
                (null == defendantLO.getSurnameLO() ||
                        0 == defendantLO.getSurnameLO().trim().length()))
        {
            return true;
        }

        if (null == cs_lo.getActionDesc() ||
                0 == cs_lo.getActionDesc().trim().length())
        {
            return true;
        }

        if ((null == cs_lo.getPlaintiff() ||
                0 == cs_lo.getPlaintiff().trim().length()) &&
                (null == cs_lo.getPlaintiffLO() ||
                        0 == cs_lo.getPlaintiffLO().trim().length()))
        {
            return true;
        }

        if (null == cs_lo.getDate())
        {
            return true;
        }

        if (null == cs_lo.getFileNO() ||
                0 == cs_lo.getFileNO().trim().length())
        {
            return true;
        }

        if (null == cs_lo.getCause() ||
                0 == cs_lo.getCause().trim().length())
        {
            return true;
        }
        return false;
    }

    private void getCourtXML()
            throws Exception
    {
        SCourts c = null;
        SCourt[] cs = null;
        try
        {
            c = reportRequest.getCourts(expenseID);
            cs = c.getCourts();
        }
        catch (Exception e)
        {
            log.error(e);
            XMLUtil.getBlankElement(ExportFileElements.COURTS, sb);
            throw e;
        }

        if (null == cs || 0 == cs.length)
        {
            XMLUtil.getBlankElement(ExportFileElements.COURTS, sb);
            return;
        }

        sb.append(ExportFileElements.COURTS_TAG_OPEN);
        SCourtLO cs_lo = null;
        int count = 0;
        for (int i = 0; i < cs.length; i++)
        {
            cs_lo = (SCourtLO) cs[i];
            if (null == cs_lo ||
                    isNoCourtData(cs_lo))
            {
                continue;
            }
            count++;
            sb.append(ExportFileElements.COURTS_ID_TAG_OPEN);
            XMLUtil.getXml(count, sb);
            sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);

            cs_lo = (SCourtLO) cs[i];
            getCourtDefendant(cs_lo);

            XMLUtil.getCData(cs_lo.getActionDesc(),
                    ExportFileElements.ACTION, sb);

            XMLUtil.getCData(cs[i].getPlaintiff(),
                    ExportFileElements.PLAINTIFF, sb);
            if (reportTypeID == BASIC_REPORT)
            {
                if (null != cs_lo.getPlaintiffLO() &&
                        0 != cs_lo.getPlaintiffLO().trim().length())
                {
                    try
                    {
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.REP_GENERAL_LABEL_CHINESE),
                                ExportFileElements.PLAINTIFF_LO, sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(
                                ExportFileElements.PLAINTIFF_LO, sb);
                    }
                }
                else
                {
                    XMLUtil.getBlankElement(
                            ExportFileElements.PLAINTIFF_LO, sb);
                }
            }
            else
            if (reportTypeID == CHINESE_REPORT)
            {
                XMLUtil.getCData(cs_lo.getPlaintiffLO(),
                        ExportFileElements.PLAINTIFF_LO, sb);
            }

            try
            {
                XMLUtil.getCData(cs_lo.getCurrencyCode(),
                        ExportFileElements.CURRENCY_CODE, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.CURRENCY_CODE, sb);
            }

            try
            {
                XMLUtil.getCData(cs_lo.getAmount().toString(),
                        ExportFileElements.AMOUNT, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.AMOUNT, sb);
            }

            try
            {
                XMLUtil.getCData(
                        DateUtil.convertToString(
                                cs_lo.getDate(), Locale.ENGLISH,
                                DateUtil.LONG_FORMAT),
                        ExportFileElements.DATE, sb);
            }
            catch (Exception e)
            {
                XMLUtil.getBlankElement(ExportFileElements.DATE, sb);
            }
            XMLUtil.getCData(cs_lo.getFileNO(), ExportFileElements.FILE_NO, sb);
            XMLUtil.getCData(cs_lo.getCause(), ExportFileElements.CAUSE, sb);
            
            sb.append(ExportFileElements.COURT_TAG_CLOSE);
        }
        sb.append(ExportFileElements.COURTS_TAG_CLOSE);
    }

    private void getNoticeOfConsentXML()
            throws Exception
    {
        SNoticeOfConsents nos = null;
        SNoticeOfConsent[] noc = null;
        try
        {
            nos = reportRequest.getNoticeOfConsents(expenseID);
            noc = nos.getNoticeOfConsents();
        }
        catch (Exception e)
        {
            log.error(e);
            XMLUtil.getBlankElement(ExportFileElements.REV_CONSENT, sb);
            throw e;
        }

        if (null == noc || 0 == noc.length)
        {
            XMLUtil.getBlankElement(ExportFileElements.REV_CONSENT, sb);
            return;
        }

        sb.append(ExportFileElements.REV_CONSENT_TAG_OPEN);
        int count = 0;
        for (int i = 0; i < noc.length; i++)
        {
            if (null == noc[i])
            {
                continue;
            }
            count++;
            sb.append(ExportFileElements.SUBMITTER_ID_TAG_OPEN);
            XMLUtil.getXml(count, sb);
            sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);

            XMLUtil.getCData(noc[i].getSubmittedBy(), ExportFileElements.NAME,
                    sb);
            XMLUtil.getCData(DateUtil.convertToString(
                    noc[i].getNoticedDate(), Locale.ENGLISH,
                    DateUtil.LONG_FORMAT), ExportFileElements.NOTICE_RECEIVE_ON,
                    sb);
            XMLUtil.getCData(DateUtil.convertToString(
                    noc[i].getEffectedDate(), Locale.ENGLISH,
                    DateUtil.LONG_FORMAT), ExportFileElements.EFFECTIVE_ON, sb);
            sb.append(ExportFileElements.SUBMITTER_TAG_CLOSE);
        }
        sb.append(ExportFileElements.REV_CONSENT_TAG_CLOSE);
    }

    private void getFinanceXML()
            throws Exception
    {
        sb.append(ExportFileElements.FINANCIAL_TAG_OPEN);
        try
        {
            getPositiveFinanceXML();
            getNegativeFinancial();
        }
        finally
        {
            sb.append(ExportFileElements.FINANCIAL_TAG_CLOSE);
        }
    }

    private void getPositiveFinanceXML()
            throws Exception
    {
        SPositiveFinancials psf = null;
        try
        {
            psf = reportRequest.getPositiveFinancial(expenseID);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.POSITIVE, sb);
            throw e;
        }
        if (null == psf)
        {
            XMLUtil.getBlankElement(ExportFileElements.POSITIVE, sb);
            return;
        }

        if ((null == psf.getPositiveWithCancel() ||
                0 == psf.getPositiveWithCancel().length) &&
                (null == psf.getPositiveWithoutCancal() ||
                        0 == psf.getPositiveWithoutCancal().length))
        {
            XMLUtil.getBlankElement(ExportFileElements.POSITIVE, sb);
            return;
        }

        sb.append(ExportFileElements.POSITIVE_TAG_OPEN);
        if (null == psf.getPositiveWithoutCancal() ||
                0 == psf.getPositiveWithoutCancal().length)
        {
            XMLUtil.getBlankElement(ExportFileElements.SUMMARY, sb);
        }
        else
        {
            sb.append(ExportFileElements.SUMMARY_TAG_OPEN);
            sb.append(ExportFileElements.LOANS_TAG_OPEN);
            int loan_count;
            try
            {
                loan_count = 0;
                if (null != psf.getTotalRevolvingFacilityLimit() &&
                        0 != psf.getTotalRevolvingFacilityLimit().toString()
                                .trim().length())
                {
                    sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                    XMLUtil.getXml(loan_count++, sb);
                    sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                    XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_REVOLVING),
                            ExportFileElements.TYPE, sb);
                    XMLUtil.getCData(psf.getTotalRevolvingFacilityLimit(),
                            ExportFileElements.TOTAL_FAC, sb);
                    XMLUtil.getCData(positiveBoolean2YN(
                            psf.isTotalRevolvingShared3rdParty()),
                            ExportFileElements.SHARED, sb);
                    sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                }

                if (null != psf.getTotalNonRevolvingFacilityLimit() &&
                        0 != psf.getTotalNonRevolvingFacilityLimit().toString()
                                .trim().length())
                {
                    sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                    XMLUtil.getXml(loan_count++, sb);
                    sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                    XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_NON_REVOLVING),
                            ExportFileElements.TYPE, sb);
                    XMLUtil.getCData(psf.getTotalNonRevolvingFacilityLimit(),
                            ExportFileElements.TOTAL_FAC, sb);
                    XMLUtil.getCData(positiveBoolean2YN(
                            psf.isTotalNonRevolvingShared3rdParty()),
                            ExportFileElements.SHARED, sb);
                    sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                }
                if (null != psf.getTotalContigentFacilityLimit() &&
                        0 != psf.getTotalContigentFacilityLimit().toString()
                                .trim().length())
                {
                    sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                    XMLUtil.getXml(loan_count++, sb);
                    sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                    XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_CONTINGENT),
                            ExportFileElements.TYPE, sb);
                    XMLUtil.getCData(psf.getTotalContigentFacilityLimit(),
                            ExportFileElements.TOTAL_FAC, sb);
                    XMLUtil.getCData(positiveBoolean2YN(
                            psf.isTotalContigentShared3rdParty()),
                            ExportFileElements.SHARED, sb);
                    sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                }

                if (null != psf.getTotalCombinedFacilityLimit() &&
                        0 != psf.getTotalCombinedFacilityLimit().toString()
                                .trim().length())
                {
                    sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                    XMLUtil.getXml(loan_count++, sb);
                    sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                    XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_COMBINED),
                            ExportFileElements.TYPE, sb);
                    XMLUtil.getCData(psf.getTotalCombinedFacilityLimit(),
                            ExportFileElements.TOTAL_FAC, sb);
                    XMLUtil.getCData(positiveBoolean2YN(
                            psf.isTotalCombinedShared3rdParty()),
                            ExportFileElements.SHARED, sb);
                    sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                }

                if (null != psf.getTotalHPLeasingFacilityLimit() &&
                        0 != psf.getTotalHPLeasingFacilityLimit().toString()
                                .trim().length())
                {
                    sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                    XMLUtil.getXml(loan_count++, sb);
                    sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                    XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_HPLEASING),
                            ExportFileElements.TYPE, sb);
                    XMLUtil.getCData(psf.getTotalHPLeasingFacilityLimit(),
                            ExportFileElements.TOTAL_FAC, sb);
                    XMLUtil.getCData(positiveBoolean2YN(
                            psf.isTotalHPLeasingShared3rdParty()),
                            ExportFileElements.SHARED, sb);
                    sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                }

            }
            finally
            {
                sb.append(ExportFileElements.LOANS_TAG_CLOSE);
            }
            XMLUtil.getCData(psf.getTotalAIsReported(),
                    ExportFileElements.SUBMITTERS, sb);
            XMLUtil.getCData(positiveBoolean2YN(
                    psf.isTotalDisputedStatus()), ExportFileElements.DISPUTE,
                    sb);
            XMLUtil.getCData(psf.getTotalSummaryFacilityLimit(),
                    ExportFileElements.TOTAL, sb);
            sb.append(ExportFileElements.SUMMARY_TAG_CLOSE);
        }

        sb.append(ExportFileElements.SUBMITTERS_WITHOUT_FAC_TAG_OPEN);
        SPositiveFinancialWithoutCancel[] pfwoc =
                psf.getPositiveWithoutCancal();
        if (null != pfwoc && 0 < pfwoc.length)
        {
            int count = 0;
            for (int i = 0; i < pfwoc.length; i++)
            {
                if (null == pfwoc[i])
                {
                    continue;
                }
                count++;
                sb.append(ExportFileElements.SUBMITTER_ID_TAG_OPEN);
                XMLUtil.getXml(count, sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                XMLUtil.getCData(pfwoc[i].getSubmittedBy(),
                        ExportFileElements.NAME, sb);
                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            pfwoc[i].getPeriod(), Locale.ENGLISH),
                            ExportFileElements.REPORTED_IN, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.REPORTED_IN, sb);
                }
                sb.append(ExportFileElements.LOANS_TAG_OPEN);
                try
                {
                    int loan_count = 0;
                    if (null != pfwoc[i].getRevolvingFacilityLimit() &&
                            0 != pfwoc[i].getRevolvingFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_REVOLVING),
                                ExportFileElements.TYPE, sb);
                        XMLUtil.getCData(
                                pfwoc[i].getRevolvingFacilityLimit(),
                                ExportFileElements.TOTAL_FAC, sb);
                        XMLUtil.getCData(positiveBoolean2YN(
                                pfwoc[i].isRevolvingShared3rdParty()),
                                ExportFileElements.SHARED, sb);
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfwoc[i].getNonRevolvingFacilityLimit() &&
                            0 != pfwoc[i].getNonRevolvingFacilityLimit()
                                    .toString().trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_NON_REVOLVING),
                                ExportFileElements.TYPE, sb);
                        XMLUtil.getCData(
                                pfwoc[i].getNonRevolvingFacilityLimit(),
                                ExportFileElements.TOTAL_FAC, sb);
                        XMLUtil.getCData(positiveBoolean2YN(
                                pfwoc[i].isNonRevolvingShared3rdParty()),
                                ExportFileElements.SHARED, sb);
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfwoc[i].getContigentFacilityLimit() &&
                            0 != pfwoc[i].getContigentFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.POSITIVE_LABEL_CONTINGENT),
                                ExportFileElements.TYPE, sb);
                        XMLUtil.getCData(pfwoc[i].getContigentFacilityLimit(),
                                ExportFileElements.TOTAL_FAC, sb);
                        XMLUtil.getCData(positiveBoolean2YN(
                                pfwoc[i].isContigentShared3rdParty()),
                                ExportFileElements.SHARED, sb);
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfwoc[i].getCombinedFacilityLimit() &&
                            0 != pfwoc[i].getCombinedFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_COMBINED),
                                ExportFileElements.TYPE, sb);
                        XMLUtil.getCData(pfwoc[i].getCombinedFacilityLimit(),
                                ExportFileElements.TOTAL_FAC, sb);
                        XMLUtil.getCData(positiveBoolean2YN(
                                pfwoc[i].isCombinedShared3rdParty()),
                                ExportFileElements.SHARED, sb);
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfwoc[i].getHPLeasingFacilityLimit() &&
                            0 != pfwoc[i].getHPLeasingFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_HPLEASING),
                                ExportFileElements.TYPE, sb);
                        XMLUtil.getCData(pfwoc[i].getHPLeasingFacilityLimit(),
                                ExportFileElements.TOTAL_FAC, sb);
                        XMLUtil.getCData(positiveBoolean2YN(
                                pfwoc[i].isHPLeasingShared3rdParty()),
                                ExportFileElements.SHARED, sb);
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                }
                finally
                {
                    sb.append(ExportFileElements.LOANS_TAG_CLOSE);
                }

                if (PreferLanguage.ENGLISH.equals(preLang))
                {
                    try
                    {
                        XMLUtil.getCData(bTangibleSecurity
                                .getTangibleSecurity(pfwoc[i]
                                        .getTangibleSecurity())
                                .getDescriptionEN(),
                                ExportFileElements.TANGIBLE, sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(ExportFileElements.TANGIBLE,
                                sb);
                    }
                }
                else
                {
                    try
                    {
                        XMLUtil.getCData(bTangibleSecurity
                                .getTangibleSecurity(pfwoc[i]
                                        .getTangibleSecurity())
                                .getDescriptionLO(),
                                ExportFileElements.TANGIBLE, sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(ExportFileElements.TANGIBLE,
                                sb);
                    }
                }

                XMLUtil.getCData(
                        positiveBoolean2YN(pfwoc[i].isDisputedStatus()),
                        ExportFileElements.DISPUTE, sb);

                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            pfwoc[i].getDisputedDate(), Locale.ENGLISH,
                            DateUtil.LONG_FORMAT),
                            ExportFileElements.DISPUTE_DATE, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.DISPUTE_DATE,
                            sb);
                }

                XMLUtil.getCData(pfwoc[i].getTotalFacilityLimit(),
                        ExportFileElements.TOTAL, sb);

                try
                {
                    XMLUtil.getCData(
                            ReportDataUtility.getCombineDetail(bCreditType,
                                    pfwoc[i].getCombinedLimitDetails(),
                                    preLang),
                            ExportFileElements.COMBINED_DETAIL, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.COMBINED_DETAIL,
                            sb);
                }
                sb.append(ExportFileElements.SUBMITTER_TAG_CLOSE);
            }
        }
        sb.append(ExportFileElements.SUBMITTERS_WITHOUT_FAC_TAG_CLOSE);

        sb.append(ExportFileElements.SUBMITTERS_WITH_FAC_TAG_OPEN);
        SPositiveFinancialWithCancel[] pfc = psf.getPositiveWithCancel();
        if (null != pfc && 0 < pfc.length)
        {
            int count = 0;
            for (int i = 0; i < pfc.length; i++)
            {
                if (null == pfc[i])
                {
                    continue;
                }
                count++;
                sb.append(ExportFileElements.SUBMITTER_ID_TAG_OPEN);
                XMLUtil.getXml(count, sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);

                XMLUtil.getCData(pfc[i].getSubmittedBy(),
                        ExportFileElements.NAME, sb);

                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            pfc[i].getFacilityCancelledDetailsPeriod(),
                            Locale.ENGLISH), ExportFileElements.REPORTED_IN,
                            sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.REPORTED_IN, sb);
                }

                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            pfc[i].getPreviousPeriod(), Locale.ENGLISH),
                            ExportFileElements.REPORTED_ASOF, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.REPORTED_ASOF,
                            sb);
                }
                sb.append(ExportFileElements.LOANS_TAG_OPEN);

                try
                {
                    int loan_count = 0;
                    if (null != pfc[i].getRevolvingFacilityLimit() &&
                            0 != pfc[i].getRevolvingFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_REVOLVING),
                                ExportFileElements.TYPE, sb);
                        try
                        {
                            XMLUtil.getCData(pfc[i].getRevolvingFacilityLimit(),
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        try
                        {
                            XMLUtil.getCData(positiveBoolean2YN(
                                    pfc[i].isRevolvingShared3rdParty()),
                                    ExportFileElements.SHARED, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(ExportFileElements.SHARED,
                                    sb);
                        }
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfc[i].getNonRevolvingFacilityLimit() &&
                            0 != pfc[i].getNonRevolvingFacilityLimit()
                                    .toString().trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_NON_REVOLVING),
                                ExportFileElements.TYPE, sb);
                        try
                        {
                            XMLUtil.getCData(
                                    pfc[i].getNonRevolvingFacilityLimit(),
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        try
                        {
                            XMLUtil.getCData(positiveBoolean2YN(
                                    pfc[i].isNonRevolvingShared3rdParty()),
                                    ExportFileElements.SHARED, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(ExportFileElements.SHARED,
                                    sb);
                        }
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfc[i].getContigentFacilityLimit() &&
                            0 != pfc[i].getContigentFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_CONTINGENT),
                                ExportFileElements.TYPE, sb);
                        try
                        {
                            XMLUtil.getCData(pfc[i].getContigentFacilityLimit(),
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        try
                        {
                            XMLUtil.getCData(positiveBoolean2YN(
                                    pfc[i].isContigentShared3rdParty()),
                                    ExportFileElements.SHARED, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(ExportFileElements.SHARED,
                                    sb);
                        }
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfc[i].getCombinedFacilityLimit() &&
                            0 != pfc[i].getCombinedFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_COMBINED),
                                ExportFileElements.TYPE, sb);
                        try
                        {
                            XMLUtil.getCData(pfc[i].getCombinedFacilityLimit(),
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        try
                        {
                            XMLUtil.getCData(positiveBoolean2YN(
                                    pfc[i].isCombinedShared3rdParty()),
                                    ExportFileElements.SHARED, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(ExportFileElements.SHARED,
                                    sb);
                        }
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                    if (null != pfc[i].getHPLeasingFacilityLimit() &&
                            0 != pfc[i].getHPLeasingFacilityLimit().toString()
                                    .trim().length())
                    {
                        sb.append(ExportFileElements.LOAN_ID_TAG_OPEN);
                        XMLUtil.getXml(loan_count++, sb);
                        sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                        XMLUtil.getCData(
                                ReportResourceJson.getMessage(preLang.getLocale(),
                                        ExportFileElements.POSITIVE_LABEL_HPLEASING),
                                ExportFileElements.TYPE, sb);
                        try
                        {
                            XMLUtil.getCData(pfc[i].getHPLeasingFacilityLimit(),
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(
                                    ExportFileElements.TOTAL_FAC, sb);
                        }
                        try
                        {
                            XMLUtil.getCData(positiveBoolean2YN(
                                    pfc[i].isHPLeasingShared3rdParty()),
                                    ExportFileElements.SHARED, sb);
                        }
                        catch (Exception e)
                        {
                            XMLUtil.getBlankElement(ExportFileElements.SHARED,
                                    sb);
                        }
                        sb.append(ExportFileElements.LOAN_TAG_CLOSE);
                    }
                }
                finally
                {
                    sb.append(ExportFileElements.LOANS_TAG_CLOSE);
                }

                if (PreferLanguage.ENGLISH.equals(preLang))
                {
                    try
                    {
                        XMLUtil.getCData(
                                bTangibleSecurity.getTangibleSecurity(
                                        pfc[i].getTangibleSecurity())
                                        .getDescriptionEN(),
                                ExportFileElements.TANGIBLE,
                                sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(ExportFileElements.TANGIBLE,
                                sb);
                    }
                }
                else
                {
                    try
                    {
                        XMLUtil.getCData(
                                bTangibleSecurity.getTangibleSecurity(
                                        pfc[i].getTangibleSecurity())
                                        .getDescriptionLO(),
                                ExportFileElements.TANGIBLE, sb);
                    }
                    catch (Exception e)
                    {
                        XMLUtil.getBlankElement(ExportFileElements.TANGIBLE,
                                sb);
                    }
                }

                XMLUtil.getCData(positiveBoolean2YN(
                        pfc[i].isDisputedStatus()), ExportFileElements.DISPUTE,
                        sb);

                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            pfc[i].getDisputedDate(), Locale.ENGLISH,
                            DateUtil.LONG_FORMAT),
                            ExportFileElements.DISPUTE_DATE, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.DISPUTE_DATE,
                            sb);
                }

                String status = pfc[i].getFacilityCancelledDetailsStatus();
                sb.append(ExportFileElements.FAC_DETAIL_ID_TAG_OPEN);
                XMLUtil.getXml(status, sb);
                sb.append(ExportFileElements.IS_WRITE_OFF);
                XMLUtil.getXml(pfc[i].isWriteOff(), sb);
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);

                try
                {
                    XMLUtil.getCData(DateUtil.convertToString(
                            pfc[i].getFacilityCancelledDetailsPeriod(),
                            Locale.ENGLISH), ExportFileElements.DATE,
                            sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.DATE, sb);
                }

                if (BulkConstants.FAC_CANCEL_DETAIL_STATUS_WRITEOFF_REPAID
                        .equals(status))
                {
                    if (pfc[i].isWriteOff())
                    {
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.POSITIVE_LABEL_WRITEOFF),
                                ExportFileElements.STATUS, sb);
                    }
                    else
                    {
                        XMLUtil.getCData(ReportResourceJson.getMessage(
                                preLang.getLocale(),
                                ExportFileElements.POSITIVE_LABEL_REPAID),
                                ExportFileElements.STATUS, sb);
                    }
                }
                else if (BulkConstants.FAC_CANCEL_DETAIL_STATUS_WITHDRAW
                        .equals(status))
                {
                    XMLUtil.getCData(ReportResourceJson.getMessage(
                            preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_WITHDRAWN),
                            ExportFileElements.STATUS, sb);
                }
                sb.append(ExportFileElements.FAC_DETAIL_TAG_CLOSE);
                XMLUtil.getCData(pfc[i].getTotalFacilityLimit(),
                        ExportFileElements.TOTAL, sb);
                try
                {
                    XMLUtil.getCData(
                            ReportDataUtility.getCombineDetail(bCreditType,
                                    pfc[i].getCombinedLimitDetails(),
                                    preLang),
                            ExportFileElements.COMBINED_DETAIL, sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getBlankElement(ExportFileElements.COMBINED_DETAIL,
                            sb);
                }
                sb.append(ExportFileElements.SUBMITTER_TAG_CLOSE);
            }
        }
        sb.append(ExportFileElements.SUBMITTERS_WITH_FAC_TAG_CLOSE);
        sb.append(ExportFileElements.POSITIVE_TAG_CLOSE);
    }

    private boolean negativeValid(SNegativeFinancials nf)
    {
        SNegativeFinancial nf12 = nf.getMax12Months();
        SNegativeFinancial nf24 = nf.getMax24Months();

        if (null == nf12 || null == nf24)
        {
            return false;
        }

        if ((null != nf12.getPeriod() &&
                0 != nf12.getPeriod().trim().length() ||
                (null != nf24.getPeriod() &&
                        0 != nf24.getPeriod().trim().length())))
        {
            return true;
        }

        if (0 != nf12.getTotalAIsPastDue() || 0 != nf24.getTotalAIsPastDue())
        {
            return true;
        }

        if (0 != nf12.getTotalAIsWriteOff() || 0 != nf24.getTotalAIsWriteOff())
        {
            return true;
        }

        if (0 != nf12.getTotalAIsRecovery() || 0 != nf24.getTotalAIsRecovery())
        {
            return true;
        }
        return false;
    }

    private void getNegativeFinancial()
            throws Exception
    {
        SNegativeFinancials nf = null;
        try
        {
            nf = reportRequest.getNegativeFinancial(expenseID);
        }
        catch (Exception e)
        {
            log.error(e);
            XMLUtil.getBlankElement(ExportFileElements.NEGATIVE, sb);
            throw e;
        }
        if (null == nf)
        {
            XMLUtil.getBlankElement(ExportFileElements.NEGATIVE, sb);
            return;
        }

        sb.append(ExportFileElements.NEGATIVE_TAG_OPEN);
        sb.append(ExportFileElements.SUMMARY_TAG_OPEN);
        SNegativeFinancial nf12 = nf.getMax12Months();
        SNegativeFinancial nf24 = nf.getMax24Months();
        sb.append(ExportFileElements.FIRST_REPORT_TAG_OPEN);
        try
        {
            XMLUtil.getCData(DateUtil.convertToString(nf12.getPeriod(),
                    Locale.ENGLISH), ExportFileElements.PAST12MONTH, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        try
        {
            XMLUtil.getCData(DateUtil.convertToString(nf24.getPeriod(),
                    Locale.ENGLISH), ExportFileElements.PAST24MONTH, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.FIRST_REPORT_TAG_CLOSE);

        sb.append(ExportFileElements.SUBMITTERS_TAG_OPEN);
        if (0 == nf12.getTotalAIsPastDue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getTotalAIsPastDue(),
                    ExportFileElements.PAST12MONTH, sb);
        }

        if (0 == nf24.getTotalAIsPastDue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getTotalAIsPastDue(),
                    ExportFileElements.PAST24MONTH, sb);
        }

        sb.append(ExportFileElements.SUBMITTERS_TAG_CLOSE);

        sb.append(ExportFileElements.MAX_PASTDUE_TAG_OPEN);
        if (0 == nf12.getMaximumDaysPastDue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getMaximumDaysPastDue(),
                    ExportFileElements.PAST12MONTH, sb);
        }

        if (0 == nf24.getMaximumDaysPastDue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getMaximumDaysPastDue(),
                    ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.MAX_PAST_DUE_TAG_CLOSE);

        sb.append(ExportFileElements.MAX_PAST_DUE_60_TAG_OPEN);
        if (null == nf12.getMaximunAmountPastDue60Days() ||
                0 == nf12.getMaximunAmountPastDue60Days().intValue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getMaximunAmountPastDue60Days(),
                    ExportFileElements.PAST12MONTH, sb);
        }

        if (null == nf24.getMaximunAmountPastDue60Days() ||
                0 == nf24.getMaximunAmountPastDue60Days().intValue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getMaximunAmountPastDue60Days(),
                    ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.MAX_PAST_DUE_60_TAG_CLOSE);
        sb.append(ExportFileElements.AMOUNT_WRITEOFF_TAG_OEPN);
        if (null == nf12.getTotalWriteOff() ||
                0 == nf12.getTotalWriteOff().intValue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getTotalWriteOff(),
                    ExportFileElements.PAST12MONTH, sb);
        }
        if (null == nf24.getTotalWriteOff() ||
                0 == nf24.getTotalWriteOff().intValue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getTotalWriteOff(),
                    ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.AMOUNT_WRITEOFF_TAG_CLOSE);

        sb.append(ExportFileElements.TOTAL_WRITEOFF_TAG_OPEN);
        if (0 == nf12.getTotalAIsWriteOff())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getTotalAIsWriteOff(),
                    ExportFileElements.PAST12MONTH, sb);
        }

        if (0 == nf24.getTotalAIsWriteOff())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getTotalAIsWriteOff(),
                    ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.TOTAL_WRITEOFF_TAG_CLOSE);

        sb.append(ExportFileElements.AMOUNT_RECOVERY_TAG_OEPN);

        if (null == nf12.getTotalRecovery() ||
                0 == nf12.getTotalRecovery().intValue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getTotalRecovery(),
                    ExportFileElements.PAST12MONTH, sb);
        }

        if (null == nf24.getTotalRecovery() ||
                0 == nf24.getTotalRecovery().intValue())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getTotalRecovery(),
                    ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.AMOUNT_RECOVERY_TAG_CLOSE);
        sb.append(ExportFileElements.TOTAL_RECOVERY_TAG_OPEN);
        if (0 == nf12.getTotalAIsRecovery())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST12MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf12.getTotalAIsRecovery(),
                    ExportFileElements.PAST12MONTH, sb);
        }
        if (0 == nf24.getTotalAIsRecovery())
        {
            XMLUtil.getBlankElement(ExportFileElements.PAST24MONTH, sb);
        }
        else
        {
            XMLUtil.getCData(nf24.getTotalAIsRecovery(),
                    ExportFileElements.PAST24MONTH, sb);
        }
        sb.append(ExportFileElements.TOTAL_RECOVERY_TAG_CLOSE);
        sb.append(ExportFileElements.SUMMARY_TAG_CLOSE);


        if (reportVersion == 2)
        {

            if (null != nf.getNegativePast60Months() &&
                        0 != nf.getNegativePast60Months().length &&
                        minorReportVersion == 1)
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.PAST_DUE);

                sb.append(ExportFileElements.REPORT_PERIOD_TAG_OPEN);
                try
                {
                    XMLUtil.getXml(DateUtil.convertToString(
                            rhd.getReportDeliveredDate(), Locale.ENGLISH,
                            DateUtil.SHORT_FORMAT), sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getXml(ExportFileElements.BLANK, sb);
                }
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                getNegativeReport2(nf.getNegativePast60Months());
                sb.append(ExportFileElements.XML_REPORT_TAG_CLOSE);
                XMLUtil.appendCloseElement(sb, ExportFileElements.PAST_DUE);
           }
           else if (null != nf.getNegativePast12Months() &&
                        0 != nf.getNegativePast12Months().length
                        && minorReportVersion == 0)
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.PAST_DUE);

                sb.append(ExportFileElements.REPORT_PERIOD_TAG_OPEN);
                try
                {
                    XMLUtil.getXml(DateUtil.convertToString(
                            rhd.getReportDeliveredDate(), Locale.ENGLISH,
                            DateUtil.SHORT_FORMAT), sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getXml(ExportFileElements.BLANK, sb);
                }
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                getNegativeReport2(nf.getNegativePast12Months());
                sb.append(ExportFileElements.XML_REPORT_TAG_CLOSE);
                XMLUtil.appendCloseElement(sb, ExportFileElements.PAST_DUE);
            }
            else if (null != nf.getNegativePast60Months() &&
                        0 != nf.getNegativePast60Months().length &&
                        minorReportVersion == 2)
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.PAST_DUE);

                sb.append(ExportFileElements.REPORT_PERIOD_TAG_OPEN);
                try
                {
                    XMLUtil.getXml(DateUtil.convertToString(
                            rhd.getReportDeliveredDate(), Locale.ENGLISH,
                            DateUtil.SHORT_FORMAT), sb);
                }
                catch (Exception e)
                {
                    XMLUtil.getXml(ExportFileElements.BLANK, sb);
                }
                sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
                getNegativeReport2(nf.getNegativePast24Months());
                sb.append(ExportFileElements.XML_REPORT_TAG_CLOSE);
                XMLUtil.appendCloseElement(sb, ExportFileElements.PAST_DUE);
           }
            else
            {
                XMLUtil.getBlankElement(ExportFileElements.PAST_DUE, sb);
            }


        }
        else if (reportVersion == 1)
        {

            sb.append(ExportFileElements.REPORT_PERIOD_TAG_OPEN);
            try
            {
                XMLUtil.getXml(DateUtil.convertToString(
                        rhd.getReportDeliveredDate(), Locale.ENGLISH,
                        DateUtil.SHORT_FORMAT), sb);
            }
            catch (Exception e)
            {
                XMLUtil.getXml(ExportFileElements.BLANK, sb);
            }
            sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
            if (null != nf.getNegativePast60Months())
            {
                getNegativeReport(nf.getNegativePast60Months());
            }
            else if (null != nf.getNegativePast12Months())
            {
                getNegativeReport(nf.getNegativePast12Months());
            }
            sb.append(ExportFileElements.XML_REPORT_TAG_CLOSE);
        }

        sb.append(ExportFileElements.NEGATIVE_TAG_CLOSE);
        getWriteOff();
    }

    private void getNegativeReport(SNegativeFinancial[] negativeFinancial)
    {
        String tmp = null;

        try
        {
            cal.setTime(rhd.getReportDeliveredDate());
        }
        catch (Exception e)
        {
            return;
        }
        for (int i = 0; i < 12; i++)
        {
            sb.append(ExportFileElements.REPORT_DATE_START);
            XMLUtil.getCData(DateUtil.convertToString(
                    cal.getTime(), Locale.ENGLISH, DateUtil.SHORT_FORMAT),
                    ExportFileElements.DATE, sb);
            int loop = 0;
            tmp = DateUtil.convertToString(cal.getTime(),
                    Locale.ENGLISH,
                    DateUtil.SHORT_FORMAT);

            String currentPeriod = null;
            for (loop = 0; loop < negativeFinancial.length; loop++)
            {
                try
                {
                    currentPeriod = DateUtil.convertToString(
                            negativeFinancial[loop].getPeriod(),
                            Locale.ENGLISH);
                }
                catch (Exception e)
                {
                    cal.add(Calendar.MONTH, -1);
                    continue;
                }

                if (tmp.equalsIgnoreCase(currentPeriod))
                {
                    if (null == negativeFinancial[loop]
                            .getMaximunAmountPastDue60Days() ||
                            0 == negativeFinancial[loop]
                                    .getMaximunAmountPastDue60Days().intValue())
                    {
                        XMLUtil.getBlankElement(ExportFileElements.PAST_DUE_60,
                                sb);
                    }
                    else
                    {
                        XMLUtil.getCData(
                                negativeFinancial[loop].getMaximunAmountPastDue60Days(),
                                ExportFileElements.PAST_DUE_60, sb);
                    }

                    if (0 == negativeFinancial[loop].getMaximumDaysPastDue())
                    {
                        XMLUtil.getBlankElement(ExportFileElements.MAX_PAST_DUE,
                                sb);
                    }
                    else
                    {
                        XMLUtil.getCData(
                                negativeFinancial[loop].getMaximumDaysPastDue(),
                                ExportFileElements.MAX_PAST_DUE, sb);
                    }
                    if (0 == negativeFinancial[loop].getTotalAIsPastDue())
                    {
                        XMLUtil.getBlankElement(ExportFileElements.SUBMITTERS,
                                sb);
                    }
                    else
                    {
                        XMLUtil.getCData(
                                negativeFinancial[loop].getTotalAIsPastDue(),
                                ExportFileElements.SUBMITTERS, sb);
                    }
                    break;
                }
            }
            if (loop == negativeFinancial.length)
            {
                XMLUtil.getBlankElement(ExportFileElements.PAST_DUE_60, sb);
                XMLUtil.getBlankElement(ExportFileElements.MAX_PAST_DUE, sb);
                XMLUtil.getBlankElement(ExportFileElements.SUBMITTERS, sb);
            }
            sb.append(ExportFileElements.REPORT_DATE_END);
            cal.add(Calendar.MONTH, -1);
        }
    }

    private void getNegativeReport2(SNegativeFinancial[] negativeFinancial)
    {
        for (int i = 0; i < negativeFinancial.length; i++)
        {

            String period = negativeFinancial[i].getPeriod();
            Calendar c = Calendar.getInstance(Locale.ENGLISH);

            c.set(Calendar.YEAR, Integer.parseInt(period.substring(0, 4)));
            c.set(Calendar.MONTH, Integer.parseInt(period.substring(4, 6)) - 1);
            c.set(Calendar.DATE, 1);


            if (null != negativeFinancial[i].getMaximunAmountPastDue60Days() &&
                    0 != negativeFinancial[i].getMaximunAmountPastDue60Days()
                            .intValue())
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.REPORTED_DATE);
                String reportBy = negativeFinancial[i].getReportBy();
                if(null != reportBy && reportBy.length() > 0)
                {
                    XMLUtil.getCData(negativeFinancial[i].getReportBy(),
                            ExportFileElements.REPORT_BY, sb);
                }
                XMLUtil.getCData(
                        simpleDateFormatNegativeReport2.format(c.getTime()),
                        ExportFileElements.DATE, sb);

                XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_AGGREGATE),
                            ExportFileElements.TYPE, sb);
                XMLUtil.getCData(
                        negativeFinancial[i].getMaximunAmountPastDue60Days(),
                        ExportFileElements.PAST_DUE_60, sb);
                XMLUtil.getCData(negativeFinancial[i].getMaximumDaysPastDue(),
                        ExportFileElements.MAX_PAST_DUE, sb);
                XMLUtil.appendCloseElement(sb,
                        ExportFileElements.REPORTED_DATE);
            }

            if (null != negativeFinancial[i].getTotalRevolvingPastDue() &&
                    0 != negativeFinancial[i].getTotalRevolvingPastDue()
                            .intValue())
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.REPORTED_DATE);
                String reportBy = negativeFinancial[i].getReportBy();
                if(null != reportBy && reportBy.length() > 0)
                {
                    XMLUtil.getCData(negativeFinancial[i].getReportBy(),
                            ExportFileElements.REPORT_BY, sb);
                }
                XMLUtil.getCData(
                        simpleDateFormatNegativeReport2.format(c.getTime()),
                        ExportFileElements.DATE, sb);
                XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_REVOLVING),
                            ExportFileElements.TYPE, sb);
                XMLUtil.getCData(
                        negativeFinancial[i].getTotalRevolvingPastDue(),
                        ExportFileElements.PAST_DUE_60, sb);
                XMLUtil.getCData(negativeFinancial[i].getMaximumDaysRevolving(),
                        ExportFileElements.MAX_PAST_DUE, sb);
                XMLUtil.appendCloseElement(sb,
                        ExportFileElements.REPORTED_DATE);
            }

            if (null != negativeFinancial[i].getTotalNonRevolvingPastDue() &&
                    0 != negativeFinancial[i].getTotalNonRevolvingPastDue()
                            .intValue())
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.REPORTED_DATE);
                String reportBy = negativeFinancial[i].getReportBy();
                if(null != reportBy && reportBy.length() > 0)
                {
                    XMLUtil.getCData(negativeFinancial[i].getReportBy(),
                            ExportFileElements.REPORT_BY, sb);
                }
                XMLUtil.getCData(
                        simpleDateFormatNegativeReport2.format(c.getTime()),
                        ExportFileElements.DATE, sb);
                XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_NON_REVOLVING),
                            ExportFileElements.TYPE, sb);
                XMLUtil.getCData(
                        negativeFinancial[i].getTotalNonRevolvingPastDue(),
                        ExportFileElements.PAST_DUE_60, sb);
                XMLUtil.getCData(
                        negativeFinancial[i].getMaximumDaysNonRevolving(),
                        ExportFileElements.MAX_PAST_DUE, sb);
                XMLUtil.appendCloseElement(sb,
                        ExportFileElements.REPORTED_DATE);
            }

            if (null != negativeFinancial[i].getTotalContigentPastDue() &&
                    0 != negativeFinancial[i].getTotalContigentPastDue()
                            .intValue())
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.REPORTED_DATE);
                String reportBy = negativeFinancial[i].getReportBy();
                if(null != reportBy && reportBy.length() > 0)
                {
                    XMLUtil.getCData(negativeFinancial[i].getReportBy(),
                            ExportFileElements.REPORT_BY, sb);
                }
                XMLUtil.getCData(
                        simpleDateFormatNegativeReport2.format(c.getTime()),
                        ExportFileElements.DATE, sb);
                XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_CONTINGENT),
                            ExportFileElements.TYPE, sb);
                XMLUtil.getCData(
                        negativeFinancial[i].getTotalContigentPastDue(),
                        ExportFileElements.PAST_DUE_60, sb);
                XMLUtil.getCData(negativeFinancial[i].getMaximumDaysContigent(),
                        ExportFileElements.MAX_PAST_DUE, sb);
                XMLUtil.appendCloseElement(sb,
                        ExportFileElements.REPORTED_DATE);
            }

            if (null != negativeFinancial[i].getTotalHPLeasingPastDue() &&
                    0 != negativeFinancial[i].getTotalHPLeasingPastDue()
                            .intValue())
            {
                XMLUtil.appendOpenElement(sb, ExportFileElements.REPORTED_DATE);
                String reportBy = negativeFinancial[i].getReportBy();
                if(null != reportBy && reportBy.length() > 0)
                {
                    XMLUtil.getCData(negativeFinancial[i].getReportBy(),
                            ExportFileElements.REPORT_BY, sb);
                }
                XMLUtil.getCData(
                        simpleDateFormatNegativeReport2.format(c.getTime()),
                        ExportFileElements.DATE, sb);
                XMLUtil.getCData(ReportResourceJson.getMessage(preLang.getLocale(),
                            ExportFileElements.POSITIVE_LABEL_HPLEASING),
                            ExportFileElements.TYPE, sb);
                XMLUtil.getCData(
                        negativeFinancial[i].getTotalHPLeasingPastDue(),
                        ExportFileElements.PAST_DUE_60, sb);
                XMLUtil.getCData(negativeFinancial[i].getMaximumDaysHPLeasing(),
                        ExportFileElements.MAX_PAST_DUE, sb);
                XMLUtil.appendCloseElement(sb,
                        ExportFileElements.REPORTED_DATE);
            }
        }
    }

    private void getWriteOff()
            throws Exception
    {
        SWriteOffFinancials wof = null;
        try
        {
            wof = reportRequest.getWriteOffFinancial(expenseID);
        }
        catch (Exception e)
        {
            log.error(e);
            XMLUtil.getBlankElement(ExportFileElements.WRITE_OFF, sb);
            throw e;
        }
        SWriteOffFinancial[] swf = null;
        if (null == wof ||
                null == (swf = wof.getWriteOffs()))
        {
            XMLUtil.getBlankElement(ExportFileElements.WRITE_OFF, sb);
            return;
        }
        try
        {
            cal.setTime(rhd.getReportDeliveredDate());
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(ExportFileElements.WRITE_OFF, sb);
            return;
        }

        boolean hasWriteOff = false;

        for (SWriteOffFinancial sWriteOffFinancial : swf)
        {
            if ((null == sWriteOffFinancial.getTotalWriteOff()
                    || 0 == sWriteOffFinancial.getTotalWriteOff().intValue())
                    && (null == sWriteOffFinancial.getTotalRecovery()
                    || 0 == sWriteOffFinancial.getTotalRecovery().intValue()))
            {
                continue;
            }
            else
            {
                hasWriteOff = true;
            }
        }

        if (hasWriteOff)
        {
            sb.append(ExportFileElements.WRITE_OFF_START);
            sb.append(ExportFileElements.REPORT_PERIOD_TAG_OPEN);
            XMLUtil.getXml(DateUtil.convertToString(
                    cal.getTime(), Locale.ENGLISH, DateUtil.SHORT_FORMAT), sb);
            sb.append(ExportFileElements.XML_CLOSE_ELEMENT_TAG);
            String tmp = null;

            int loop = 0;
            
            for (loop = 0; loop < swf.length; loop++)
            {

                if ((null == swf[loop].getTotalWriteOff()
                        || 0 == swf[loop].getTotalWriteOff().intValue())
                        && (null == swf[loop].getTotalRecovery()
                        ||
                        0 == swf[loop].getTotalRecovery().intValue()))
                {
                    continue;
                }
                cal.set(new Integer(swf[loop].getPeriod().substring(0,4)),new Integer(swf[loop].getPeriod().substring(4,6))-1,1);
                sb.append(ExportFileElements.REPORT_DATE_START);
                XMLUtil.getCData(DateUtil.convertToString(
                        cal.getTime(), Locale.ENGLISH,
                        DateUtil.SHORT_FORMAT),
                        ExportFileElements.DATE, sb);

                String reportBy = swf[loop].getReportBy();
                if(null != reportBy && reportBy.length() > 0)
                {
                    XMLUtil.getCData(swf[loop].getReportBy(),
                        ExportFileElements.REPORT_BY, sb);
                }


                if (null == swf[loop].getTotalWriteOff() ||
                        0 == swf[loop].getTotalWriteOff().intValue())
                {
                    XMLUtil.getBlankElement(ExportFileElements.AMOUNT,
                            sb);
                }
                else
                {
                    XMLUtil.getCData(swf[loop].getTotalWriteOff(),
                            ExportFileElements.AMOUNT, sb);
                }

                if (null == swf[loop].getTotalRecovery() ||
                        0 == swf[loop].getTotalRecovery().intValue())
                {
                    XMLUtil.getBlankElement(ExportFileElements.RECOVERY,
                            sb);
                }
                else
                {
                    XMLUtil.getCData(swf[loop].getTotalRecovery(),
                            ExportFileElements.RECOVERY, sb);
                }
                sb.append(ExportFileElements.REPORT_DATE_END);               
            }

            cal.add(Calendar.MONTH, -1);

            sb.append(ExportFileElements.XML_REPORT_TAG_CLOSE);
            sb.append(ExportFileElements.WRITE_OFF_TAG_CLOSE);
        }
        else
        {
            XMLUtil.getBlankElement(ExportFileElements.WRITE_OFF, sb);
        }
    }

    private String positiveBoolean2YN(boolean b)
    {
        if (b)
        {
            return ReportResourceJson.getMessage(preLang.getLocale(),
                    ExportFileElements.POSIITVE_LABEL_YES);
        }
        else
        {
            return ReportResourceJson.getMessage(preLang.getLocale(),
                    ExportFileElements.POSITIVE_LABEL_NO);
        }
    }
}
