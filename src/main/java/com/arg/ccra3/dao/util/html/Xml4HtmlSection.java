package com.arg.ccra3.dao.util.html;

import com.arg.cb2.inquiry.InquiryConstants;
import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.ResourceXML;
import com.arg.cb2.inquiry.data.ReportHeaderData;
import com.arg.cb2.util.XMLUtil;
import java.util.Enumeration;
import java.util.HashMap;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Xml4HtmlSection
{
    private PreferLanguage preLang = PreferLanguage.ENGLISH;
    private HashMap<String, Object> httpServletRequest;
    private ReportHeaderData rhd = null;
    private int reportTypeID = 0;
    private StringBuffer sb = new StringBuffer();
    private static final Log log = LogFactory.getLog(Xml4HtmlSection.class);
    public Xml4HtmlSection(PreferLanguage preLang,
                           HashMap<String, Object> httpServletRequest,
                           ReportHeaderData rhd,
                           int reportTypeID)
    {
        this.httpServletRequest = null;
        this.preLang = preLang;
        this.httpServletRequest = httpServletRequest;
        this.rhd = rhd;
        this.reportTypeID = reportTypeID;
    }


    private void start()
    {
        sb.setLength(0);
        sb.append(com.arg.cb2.inquiry.ExportFileElements.XML_HEADER);
        sb.append(com.arg.cb2.inquiry.ExportFileElements.XML_REPORT_TYPE_TAG_OPEN);
    }

    private void close()
    {
        try
        {
            getCoverPage();
            getFooterXML();
            if (preLang.equals(PreferLanguage.CHINESE))
            {
                ResourceXML.toLocalXML(sb);
            }
            else
            {
                ResourceXML.toEngXML(sb);
            }
        }
        catch (Exception ignore)
        {
            String loggerError = String.format("Bug: %s", ignore);
            log.error(loggerError);
        }
        
        if (!(null == httpServletRequest || httpServletRequest.isEmpty()))
        {
            getRequestForm();
        }
        sb.append(com.arg.cb2.inquiry.ExportFileElements.XML_REPORT_TAG_CLOSE);
    }

    public StringBuffer toXML(StringBuffer reportSB)
    {
        start();
        sb.append(reportSB);
        close();
        return sb;
    }

    private void getCoverPage()
    {
        try
        {
            XMLUtil.getCData (rhd.getAIName(), com.arg.cb2.inquiry.ExportFileElements.AI_NAME, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.AI_NAME, sb);
        }

        try
        {
            XMLUtil.getCData(rhd.getUserID(), com.arg.cb2.inquiry.ExportFileElements.USER_ID, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.USER_ID, sb);
        }

        try
        {
            XMLUtil.getCData(rhd.getAddressEnglish(), com.arg.cb2.inquiry.ExportFileElements.ADDRESS, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.ADDRESS, sb);
        }
        try
        {
            XMLUtil.getCData(rhd.getCity(), com.arg.cb2.inquiry.ExportFileElements.CITY, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.CITY, sb);
        }
        try
        {
            XMLUtil.getCData(rhd.getProvince(), com.arg.cb2.inquiry.ExportFileElements.PROVINCE, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.PROVINCE, sb);
        }

        try
        {
            XMLUtil.getCData(rhd.getCountry(), com.arg.cb2.inquiry.ExportFileElements.COUNTRY, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.COUNTRY, sb);
        }
        try
        {
            XMLUtil.getCData(rhd.getAreaCode(), com.arg.cb2.inquiry.ExportFileElements.AREA_CODE, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.AREA_CODE, sb);
        }
        try
        {
            XMLUtil.getCData(rhd.getPhoneNo(), com.arg.cb2.inquiry.ExportFileElements.PHONE_NO, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.PHONE_NO, sb);
        }
        try
        {
            XMLUtil.getCData(rhd.getFaxNo(), com.arg.cb2.inquiry.ExportFileElements.FAX_NO, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.FAX_NO, sb);
        }
        try
        {
            XMLUtil.getCData(rhd.getCompanyName(), com.arg.cb2.inquiry.ExportFileElements.COMPANY_NAME_HEADER, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getBlankElement(com.arg.cb2.inquiry.ExportFileElements.COMPANY_NAME_HEADER, sb);
        }

        try
        {
            XMLUtil.getCData (InquiryConstants.OBJECT_TYPE.SEARCH == reportTypeID, com.arg.cb2.inquiry.ExportFileElements.NO_MATCH_REPORT, sb);
        }
        catch (Exception e)
        {
            XMLUtil.getCData(false, com.arg.cb2.inquiry.ExportFileElements.NO_MATCH_REPORT, sb);
        }
    }

    private void getFooterXML()
    {
        sb.append(com.arg.cb2.inquiry.ExportFileElements.REQUEST_TAG_OPEN);
        XMLUtil.getCData(
                reportTypeID == InquiryConstants.OBJECT_TYPE.CHINESE_REPORT,
                        com.arg.cb2.inquiry.ExportFileElements.CHINESS_REPORT, sb);
        sb.append(com.arg.cb2.inquiry.ExportFileElements.REQUEST_TAG_CLOSE);
    }
    
    public void getRequestForm()
    { 

        Enumeration<String> enumeration = new IteratorEnumeration(httpServletRequest.keySet().iterator());

        String attribute;
        String attName;
        sb.append(com.arg.cb2.inquiry.ExportFileElements.REQUEST_FORM_TAG_OPEN);
        while (enumeration.hasMoreElements())
        {
            try
            {
                attName = enumeration.nextElement().toString();
                attribute = httpServletRequest.get(attName).toString();
            }
            catch (Exception e)
            {
                continue;
            }
            if (null == attName || 0 == attName.trim().length() ||
                    null== attribute || 0 == attribute.trim().length())
            {
                continue;
            }
            XMLUtil.getCData(attribute, attName, sb);
        }
        sb.append(com.arg.cb2.inquiry.ExportFileElements.REQUEST_FORM_TAG_CLOSE);
    }
    
}
