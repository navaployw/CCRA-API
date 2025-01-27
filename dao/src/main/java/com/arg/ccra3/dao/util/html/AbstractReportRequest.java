
package com.arg.ccra3.dao.util.html;

import com.arg.cb2.inquiry.ErrorMessages;
import com.arg.cb2.inquiry.InquiryConstants;
import com.arg.cb2.inquiry.ReportRequest;
import com.arg.cb2.inquiry.data.ObjectData;
import com.arg.cb2.inquiry.data.ReportHeaderData;
import com.arg.cb2.inquiry.data.SAIOrderedLists;
import com.arg.cb2.inquiry.data.SComStructures;
import com.arg.cb2.inquiry.data.SCourts;
import com.arg.cb2.inquiry.data.SExecutiveSummary;
import com.arg.cb2.inquiry.data.SNegativeFinancials;
import com.arg.cb2.inquiry.data.SNoticeOfConsents;
import com.arg.cb2.inquiry.data.SPositiveFinancials;
import com.arg.cb2.inquiry.data.SProfiles;
import com.arg.cb2.inquiry.data.SWriteOffFinancials;
import com.arg.ccra3.dao.HTMLReportCreatorDAO;
import com.arg.util.GenericException;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractReportRequest 
    implements ReportRequest{
    
    protected final JdbcTemplate jdbcTemplate;
    private final Log logger = LogFactory.getLog(AbstractReportRequest.class);
    private String loggerError = "";
    
    AbstractReportRequest(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void addSAIOrderLists(final ReportHeaderData data)
        throws Exception
    {
        if (null == data)
            return;
        
        Long expenseID = data.getExpenseID();
        
        if (expenseID == null)
            return;

        SAIOrderedLists sol;
        sol = (SAIOrderedLists) getReport(expenseID,
                InquiryConstants.PRODUCT_DATA_CONSTANTS.AI_ORDERED_LIST);

        if (null == sol)
            return;

        data.setSaiOrderedLists(sol);
    } 

    
    public ObjectData getReportType(final Long expenseID)
        throws Exception
    {
        
        if (null == expenseID)
        {
            throw new GenericException("INQ-A00008",
                ErrorMessages.getString("INQ-A00008"));
        } 

        String sql = "select b.objectid, b.objectlabel, b.objectdesc "
                + "from spm_expense a, spm_object b "
                + "where a.productusage = b.objectid and a.expenseid = ?";
        
        ObjectData data = new ObjectData();
        
        jdbcTemplate.query(
            sql,
            (ResultSet rs) -> {
                do{
                    data.setObjectID(rs.getInt(1));
                    data.setLabel(rs.getString(2));
                    data.setDescription(rs.getString(3));
                }while (rs.next());
            },
            expenseID
        );

        return data;
    } 
   
    public SExecutiveSummary getExecutiveSummary(final Long expenseID)
    {
        SExecutiveSummary obj = null;

        try
        {
            obj = (SExecutiveSummary) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.EXECUTIVE_SUMMARY);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getExecutiveSummary: %s", ignore);
            logger.error(loggerError);
        }
        
        if (null == obj)
            return new SExecutiveSummary(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);

        return obj;
    } 

    
    public SProfiles getProfiles(final Long expenseID)
    {
        SProfiles obj = null;

        try
        {
            obj = (SProfiles) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.PROFILE);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getExecutiveSummary: %s", ignore);
            logger.error(loggerError);
        }
        
        if (null == obj)
                obj = new SProfiles(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);

        return obj;
    } 

    
    public SComStructures getCompanyGroupStructures(final Long expenseID)
    {
        SComStructures obj = null;

        try
        {
            obj = (SComStructures) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.COMPANY_GROUP_STRUCTURE);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getExecutiveSummary: %s", ignore);
            logger.error(loggerError);
        }
        
        if (null == obj)
            obj = new SComStructures(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);
        
        return obj;
    } 

    
    public SCourts getCourts(final Long expenseID)
    {
        SCourts obj = null;

        try
        {
            obj = (SCourts) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.COURT);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getCourts: %s", ignore);
            logger.error(loggerError);
        }

        if (null == obj)
            obj = new SCourts(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);
        
        return obj;
    } 

    
    public SNoticeOfConsents getNoticeOfConsents(final Long expenseID)
    {
        SNoticeOfConsents obj = null;

        try
        {
            obj = (SNoticeOfConsents) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.REVOCATION_OF_CONSENT);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getNoticeOfConsents: %s", ignore);
            logger.error(loggerError);
        }
        
        if (null == obj)
            obj = new SNoticeOfConsents(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);

        return obj;
    } 

    
    public SPositiveFinancials getPositiveFinancial(final Long expenseID)
    {
        SPositiveFinancials obj = null;

        try
        {
            obj = (SPositiveFinancials) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.POSITIVE_LOAN_DATA);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getPositiveFinancial: %s", ignore);
            logger.error(loggerError);
        }

        
        if (null == obj)
            obj = new SPositiveFinancials(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);

        return obj;
    } 

    
    public SNegativeFinancials getNegativeFinancial(final Long expenseID)
    {
        SNegativeFinancials obj  = null;

        try
        {
            obj = (SNegativeFinancials) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.NEGATIVE_LOAN_DATA);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getNegativeFinancial: %s", ignore);
            logger.error(loggerError);
        }
        
        if (null == obj)
            obj = new SNegativeFinancials(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);
            
        return obj;
    } 

    
    public SWriteOffFinancials getWriteOffFinancial(final Long expenseID)
        throws Exception
    {
        SWriteOffFinancials obj = null;

        try
        {
            obj = (SWriteOffFinancials) getReport(expenseID,
                    InquiryConstants.PRODUCT_DATA_CONSTANTS.WRITEOFF);
        } 
        catch (final Exception ignore){
            loggerError = String.format("Exception:getWriteOffFinancial: %s", ignore);
            logger.error(loggerError);
        }
        
        if (null == obj)
            obj = new SWriteOffFinancials(InquiryConstants.DOCUMENT_LANGUAGE.ENGLISH);

        return obj;
    }
}
