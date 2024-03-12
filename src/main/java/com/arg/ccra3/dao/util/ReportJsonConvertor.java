
package com.arg.ccra3.dao.util;

import com.arg.cb2.inquiry.BaseCreditType;
import com.arg.cb2.inquiry.BaseTangibleSecurity;
import com.arg.cb2.inquiry.DateFormatException;
import com.arg.cb2.inquiry.DateUtil;
import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.ReportDataUtility;
import com.arg.cb2.inquiry.TangibleSecurity;
import static com.arg.cb2.inquiry.bulk.BulkConstants.*;
import com.arg.cb2.inquiry.data.SDispute;
import com.arg.cb2.inquiry.data.SEvidanceOfCourt;
import com.arg.cb2.inquiry.data.SExecutiveSummary;
import com.arg.cb2.inquiry.data.SFacilitiesCancel;
import com.arg.cb2.inquiry.data.SNoticeOfConsent;
import com.arg.cb2.inquiry.data.SResubmitted;
import com.arg.cb2.inquiry.data.SUnloaded;
import com.arg.cb2.inquiry.data.SUpdated;
import com.arg.ccra3.models.report.ReportFinancial;
import com.arg.ccra3.models.report.ReportGeneral;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;
import com.arg.cb2.inquiry.data.SComStructure;
import com.arg.cb2.inquiry.data.SComStructureLO;
import com.arg.cb2.inquiry.data.SCourt;
import com.arg.cb2.inquiry.data.SCourtLO;
import com.arg.cb2.inquiry.data.SDefendant;
import com.arg.cb2.inquiry.data.SDefendantLO;
import com.arg.cb2.inquiry.data.SNegativeFinancial;
import com.arg.cb2.inquiry.data.SNegativeFinancials;
import com.arg.cb2.inquiry.data.SPositiveFinancialWithCancel;
import com.arg.cb2.inquiry.data.SPositiveFinancialWithoutCancel;
import com.arg.cb2.inquiry.data.SPositiveFinancials;
import com.arg.cb2.inquiry.data.SProfile;
import com.arg.cb2.inquiry.data.SProfileLO;
import com.arg.cb2.inquiry.data.SWriteOffFinancial;
import com.arg.cb2.inquiry.data.SWriteOffFinancials;
import com.arg.ccra3.models.report.PositiveLoan;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static com.arg.ccra3.common.InquiryConstants.*;
import com.arg.util.GenericException;
import static java.math.BigDecimal.ZERO;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportJsonConvertor {
    private Integer orderedLang;
    private PreferLanguage userPrefLang = PreferLanguage.ENGLISH;
    private Integer minorReportVersion;
    private JSONObject rhd;
    
    private BaseCreditType bCreditType;
    private BaseTangibleSecurity bTangibleSecurity;
    
    private final SimpleDateFormat simpleDateFormatNegativeReport2 =
            new SimpleDateFormat("MMMMMMMMMM yyyy", Locale.ENGLISH);
    
    private static final Logger logger = LogManager.getLogger(ReportJsonConvertor.class);
    
    public ReportJsonConvertor(){
        logger.info("ReportJsonConvertor");
    }

    public BaseCreditType getbCreditType() {
        return bCreditType;
    }

    public ReportJsonConvertor setbCreditType(BaseCreditType bCreditType) {
        this.bCreditType = bCreditType;
        return this;
    }

    public BaseTangibleSecurity getbTangibleSecurity() {
        return bTangibleSecurity;
    }

    public ReportJsonConvertor setbTangibleSecurity(BaseTangibleSecurity bTangibleSecurity) {
        this.bTangibleSecurity = bTangibleSecurity;
        return this;
    }

    public Integer getOrderedLang() {
        return orderedLang;
    }

    public ReportJsonConvertor setOrderedLang(Integer orderedLang) {
        this.orderedLang = orderedLang;
        return this;
    }

    public PreferLanguage getUserPrefLang() {
        return userPrefLang;
    }

    public ReportJsonConvertor setUserPrefLang(PreferLanguage userPrefLang) {
        if(userPrefLang != null)
            this.userPrefLang = userPrefLang;
        return this;
    }

    public Integer getMinorReportVersion() {
        return minorReportVersion;
    }

    public ReportJsonConvertor setMinorReportVersion(Integer minorReportVersion) {
        this.minorReportVersion = minorReportVersion;
        return this;
    }
    
    public ReportJsonConvertor setRhd(JSONObject rhd) {
        this.rhd = rhd;
        return this;
    }    
    
    public ReportGeneral toGeneral(Map<String,Object> reports) throws NullPointerException,DateFormatException{
        try{
            ReportGeneral general = new ReportGeneral();
            
            LinkedHashMap<String, Object> execSum = getExecutiveSummaryJson(reports);
            logger.info("toGeneral.execSum: " + execSum);
            if(execSum != null)
                general.setExecutiveSummary(execSum);
            
            List<Object> js = getProfilesJson(reports);
            general.setProfile(returnNullIfEmpty(js));

            js = getComStructuresJson(reports);
            general.setGroupStructure(returnNullIfEmpty(js));

            js = getCourtXML(reports);
            general.setCourt(returnNullIfEmpty(js));

            js = getNoticeOfConsentJson(reports);
            general.setRevOfConsent(returnNullIfEmpty(js));
            
            return general;
        }
        catch(NullPointerException|DateFormatException e){
            logger.error(e.getMessage());
            throw e;
        }
    }
    private LinkedHashMap<String, Object> getExecutiveSummaryJson(Map<String,Object> reports)
            throws DateFormatException
    {
        LinkedHashMap<String, Object> execSum = new LinkedHashMap<>();
        SExecutiveSummary es = (SExecutiveSummary)reports.get("execuSummary");
        if (null == es)
            return null;
        
        logger.info("getExecutiveSummaryJson's doing its job: " + es);
        
        LinkedHashMap<String, Object> submitter = new LinkedHashMap<>();
        submitter.put("total", es.getAIsReportNumber());
        Date deliverDate = (Date)rhd.get("reportIssuedDate");
        submitter.put("period", 
            DateUtil.convertToString((deliverDate) ,Locale.ENGLISH, DateUtil.SHORT_FORMAT));
        execSum.put("submitter", submitter);
        
        LinkedHashMap<String, Object> pastDue60 = new LinkedHashMap<>();
        pastDue60.put("period", DateUtil.convertToString(
            es.getTotalPastDueAmount60DaysPeriod(), Locale.ENGLISH));
        pastDue60.put("amount", es.getTotalPastDueAmount60Days());
        execSum.put("pastDue60", pastDue60);        
        
        LinkedHashMap<String, Object> recovered = new LinkedHashMap<>();
        recovered.put("period", DateUtil.convertToString(
            es.getRecoveredPeriod(), Locale.ENGLISH));
        recovered.put("amount", es.getRecoveredAmount());
        execSum.put("recovered", recovered);
        
        SResubmitted[] resubmitteds = es.getResubmitted();
        if (null != resubmitteds)
        {
            List<Object> reSubmissions = new ArrayList<>();
            for (SResubmitted sr : resubmitteds)
            {
                LinkedHashMap<String, Object> reSubmission = new LinkedHashMap<>();
                reSubmission.put("submitter", sr.getSubmittedBy());
                reSubmission.put("period", DateUtil.convertToString(
                    sr.getPeriod(),Locale.ENGLISH));
                reSubmission.put("date", DateUtil.convertToString(
                        sr.getResendDate(), Locale.ENGLISH,DateUtil.LONG_FORMAT));
                reSubmissions.add(reSubmission);
            }
            execSum.put("reSubmission", returnNullIfEmpty(reSubmissions));
        }
        
        SUnloaded[] unloadData = es.getUnloaded();
        if (null != unloadData)
        {
            List<Object> unloads = new ArrayList<>();
            for (SUnloaded ul : unloadData)
            {
                LinkedHashMap<String, Object> unload = new LinkedHashMap<>();
                unload.put("submitter", ul.getSubmittedBy());
                unload.put("period", DateUtil.convertToString(
                        ul.getPeriod(),Locale.ENGLISH));
                unload.put("date", DateUtil.convertToString(
                        ul.getUnloadDate(), Locale.ENGLISH,DateUtil.LONG_FORMAT));
                unloads.add(unload);
            }
            execSum.put("unload", returnNullIfEmpty(unloads));
        }
        
        SUpdated[] updateRecordsData = es.getUpdated();        
        if (null != updateRecordsData)
        {
            List<Object> updateRecords = new ArrayList<>();
            for (SUpdated ud : updateRecordsData)
            {                
                LinkedHashMap<String, Object> updateRecord = new LinkedHashMap<>();
                updateRecord.put("submitter", ud.getSubmittedBy());
                updateRecord.put("period", DateUtil.convertToString(
                    ud.getPeriod(),Locale.ENGLISH));
                updateRecord.put("date", DateUtil.convertToString(
                    ud.getUpdateDate(), Locale.ENGLISH,DateUtil.LONG_FORMAT));
                updateRecords.add(updateRecord);
            }
            execSum.put("updateRecord", returnNullIfEmpty(updateRecords));
        }
        
        SNoticeOfConsent[] snocsData = es.getNoticeOfRevocationOfConsent();
        if (null != snocsData)
        {
            List<Object> snocss = new ArrayList<>();
            for (SNoticeOfConsent snocs : snocsData)
            {
                LinkedHashMap<String, Object> revConsent = new LinkedHashMap<>();
                revConsent.put("submitter", snocs.getSubmittedBy());
                revConsent.put("noticeReceivedOn", DateUtil.convertToString(
                    snocs.getNoticedDate(),Locale.ENGLISH,DateUtil.LONG_FORMAT));
                revConsent.put("effectiveOn", DateUtil.convertToString(
                    snocs.getEffectedDate(),Locale.ENGLISH,DateUtil.LONG_FORMAT));
                snocss.add(revConsent);
            }
            execSum.put("revOfConsent", returnNullIfEmpty(snocss));
        }
        
        SEvidanceOfCourt[] ecsData = es.getEvidanceOfCourt();
        if (null != ecsData)
        {
            List<Object> ecss = new ArrayList<>();
            for (SEvidanceOfCourt ecs : ecsData)
            {                
                LinkedHashMap<String, Object> court = new LinkedHashMap<>();
                court.put("date", DateUtil.convertToString(
                    ecs.getReportDate(), Locale.ENGLISH,DateUtil.LONG_FORMAT));
                court.put("total", ecs.getTotal());
                ecss.add(court);
            }
            execSum.put("evidenceOfCourt", returnNullIfEmpty(ecss));
        }

        SDispute[] dsData = es.getDispute();
        if (null != dsData)
        {
            List<Object> disputes = new ArrayList<>();
            for (SDispute ds : dsData)
            {
                LinkedHashMap<String, Object> dispute = new LinkedHashMap<>();
                dispute.put("submitter", ds.getSubmittedBy());
                dispute.put("date", DateUtil.convertToString(
                        ds.getDisputedDate(), Locale.ENGLISH, DateUtil.LONG_FORMAT));
                disputes.add(dispute);
            }
            execSum.put("dispute", returnNullIfEmpty(disputes));
        }
        
        SFacilitiesCancel[] fcData = es.getFacilitiesCancel();
        if (null != fcData)
        {
            List<Object> fcs = new ArrayList<>();
            for (SFacilitiesCancel fc : fcData)
            {                
                LinkedHashMap<String, Object> facility = new LinkedHashMap<>();
                
                facility.put("period", DateUtil.convertToString(
                        fc.getPeriod(), Locale.ENGLISH));
                facility.put("submitter", fc.getSubmittedBy());                
                
                String status = switch(fc.getStatus()){
                    case FAC_CANCEL_DETAIL_STATUS_WRITEOFF_REPAID -> 
                        fc.isWriteOff() ? "positive.label.writeoff" : "positive.label.repaid";
                    case FAC_CANCEL_DETAIL_STATUS_WITHDRAW -> "positive.label.withdrawn";
                    case FAC_CANCEL_DETAIL_STATUS_CANCEL -> "executive.fac-cancel";
                    default -> null;
                };
                if(status != null)
                    facility.put("status", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), status));
                else
                    facility.put("status", null);
                
                fcs.add(facility);
            }
            execSum.put("fac", returnNullIfEmpty(fcs));
        }
        
        
        String fDate = DateUtil.convertToString(es.getFirstReportDate(),
            Locale.ENGLISH);
        execSum.put("first", fDate);
        
        if (fDate.isBlank() && (
                pastDue60.get("amount") == null ||
                pastDue60.get("amount").equals(ZERO)
            ) && 
                (int)submitter.get("total") == 0
            && (
                dsData == null ||
                dsData.length == 0
            ) && (
                recovered.get("amount") == null ||
                recovered.get("amount").equals(ZERO)
            ) && (
                fcData == null ||
                fcData.length == 0
            )
        )execSum.put("execCompanyName", rhd.get("companyName"));
        
        return execSum;
    }
    
    private List<Object> getProfilesJson(Map<String,Object> reports) throws DateFormatException
    {
        List<Object> profilesRet = new ArrayList<>();
        
        SProfile[] profiles = (SProfile[])reports.get("profile");
        if(profiles == null || profiles.length == 0)
            return new ArrayList<>();

        for (SProfile p : profiles)
        {
            SProfileLO p_lo = (SProfileLO) p;
            if (null == p_lo) continue;
            
            LinkedHashMap<String, Object> profile = new LinkedHashMap<>();
            profile.put("currentName", p_lo.getCurrentName());
            profile.put("previousName", p_lo.getPreviousName());
            profile.put("tradeName", p_lo.getTradeName());
            profile.put("phone", p_lo.getPhoneNO());
            profile.put("placeOfReg", p_lo.getPlaceOfRegistration());
            profile.put("submittedBy", p_lo.getSubmittedBy());
            profile.put("memberReportDate", DateUtil.convertToString(
                p_lo.getReportedDate(), Locale.ENGLISH));
            profile.put("address", concatAddress(
                new String[]{
                    p_lo.getAddress1(),
                    p_lo.getAddress2(),
                    p_lo.getAddress3()
                }
            ));
            handleChineseValueFromOrderLang(profile, "previousNameLO", p_lo.getPreviousNameLO());
            handleChineseValueFromOrderLang(profile, "currentNameLO", p_lo.getCurrentNameLO());
            handleChineseValueFromOrderLang(profile, "tradeNameLO", p_lo.getTradeNameLO());
            handleChineseValueFromOrderLang(profile, "addressLO", concatAddress(
                new String[]{
                    p_lo.getAddress1LO(),
                    p_lo.getAddress2LO(),
                    p_lo.getAddress3LO()
                })
            );
            
            List<Object> identifiers = new ArrayList<>();
            if (null != p_lo.getHKBRCNO() &&
                    0 != p_lo.getHKBRCNO().trim().length()){
                LinkedHashMap<String, Object> identifier = new LinkedHashMap<>();
                identifier.put("identifierType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "genprofile.label.hkbrc"));
                identifier.put("identifierOfCompany", p_lo.getHKBRCNO());
                identifiers.add(identifier);
            }
            if (null != p_lo.getHKCINO() &&
                    0 != p_lo.getHKCINO().trim().length()){
                LinkedHashMap<String, Object> identifier = new LinkedHashMap<>();
                identifier.put("identifierType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "genprofile.label.hkci"));
                identifier.put("identifierOfCompany", p_lo.getHKCINO());
                identifiers.add(identifier);
            }
            if (null != p_lo.getOtherNO() &&
                    0 != p_lo.getOtherNO().trim().length()){
                LinkedHashMap<String, Object> identifier = new LinkedHashMap<>();
                identifier.put("identifierType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "genprofile.label.other"));
                identifier.put("identifierOfCompany", p_lo.getOtherNO());
                identifiers.add(identifier);
            }
            profile.put("identifier", returnNullIfEmpty(identifiers));
            
            profilesRet.add(profile);
        }
        return profilesRet;
    }
    private String concatAddress(String[] addrs){
        String newLine = "";
        StringBuffer ret = new StringBuffer();
        for(String addr : addrs){
            if(addr != null){
                ret.append(addr);
                ret.append(newLine);
                newLine = "\n";
            }
        }
        return ret.toString();
    }
    
    private List<Object> getComStructuresJson(Map<String,Object> reports)throws NullPointerException
    {
        SComStructure[] cssData = (SComStructure[])reports.get("groupStructure");
        
        if (null == cssData || 0 == cssData.length)
            return new ArrayList<>();
        
        List<Object> comstructRet = new ArrayList<>();
                
        for (SComStructure css : cssData)
        {
            SComStructureLO css_lo = (SComStructureLO) css;
            
            LinkedHashMap<String, Object> comStruct = new LinkedHashMap<>();
            comStruct.put("submittedBy", css_lo.getSubmittedBy());
            comStruct.put("shareHolderName", css_lo.getShareholderName());
            handleChineseValueFromOrderLang(comStruct, "shareHolderNameLO", css_lo.getShareholderNameLO());
            
            comstructRet.add(comStruct);
        }
        return comstructRet;
    }
    
    private List<Object> getCourtXML(Map<String,Object> reports)
            throws NullPointerException
    {
        SCourt[] csData = (SCourt[])reports.get("court");

        if (null == csData || 0 == csData.length)
            return new ArrayList<>();
        
        List<Object> courts = new ArrayList<>();

        for (SCourt cs : csData)
        {
            SCourtLO cs_lo = (SCourtLO) cs;
            if (null == cs_lo || isNoCourtData(cs_lo))
                continue;
            
            LinkedHashMap<String, Object> court = new LinkedHashMap<>();
            
            SDefendant[] def = cs_lo.getDefendants();
            SDefendantLO defLo = (SDefendantLO)def[0];
            court.put("defendant", getDefendantName(defLo.getGivennameEN(), defLo.getSurnameEN()));
            court.put("action", cs_lo.getActionDesc());
            court.put("plaintiff", cs_lo.getPlaintiff());
            court.put("currencyCode", cs_lo.getCurrencyCode());
            court.put("amount", cs_lo.getAmount());
            court.put("date", DateUtil.convertToString(
                cs_lo.getDate(), Locale.ENGLISH, DateUtil.LONG_FORMAT));
            court.put("fileNo", cs_lo.getFileNO());
            court.put("cause", cs_lo.getCause());
            handleChineseValueFromOrderLang(court, "defendantLO", getDefendantName(defLo.getGivennameLO(), defLo.getSurnameLO()));
            handleChineseValueFromOrderLang(court, "plaintiffLO", cs_lo.getPlaintiffLO());
                
            courts.add(court);
        }
        return courts;
    }
        
    private boolean isNoCourtData(SCourtLO cs_lo)
    {
        if (null == cs_lo || null == cs_lo.getDefendant()
                || cs_lo.getDefendant().isEmpty())
            return true;

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
            return true;

        if ((null == cs_lo.getPlaintiff() ||
                0 == cs_lo.getPlaintiff().trim().length()) &&
                (null == cs_lo.getPlaintiffLO() ||
                        0 == cs_lo.getPlaintiffLO().trim().length()))
            return true;

        if (null == cs_lo.getDate())
            return true;
        
        if (null == cs_lo.getFileNO() ||
                0 == cs_lo.getFileNO().trim().length())
            return true;

        if (null == cs_lo.getCause() ||
                0 == cs_lo.getCause().trim().length())
            return true;
        
        return false;
    }
    private String getDefendantName(String givenName, String surname){
        return givenName != null ? givenName : surname;
    }
    
    private List<Object> getNoticeOfConsentJson(Map<String,Object> reports)throws DateFormatException
    {
        SNoticeOfConsent[] nocData = (SNoticeOfConsent[])reports.get("revOfConsent");
         if (null == nocData || 0 == nocData.length)
            return new ArrayList<>();
        
        List<Object> consents = new ArrayList<>();

        for (SNoticeOfConsent noc : nocData)
        {
            if (null == noc)continue;
            
            LinkedHashMap<String, Object> consent = new LinkedHashMap<>();
            consent.put("submitter", noc.getSubmittedBy());
            consent.put("noticeReceivedOn", DateUtil.convertToString(
                noc.getNoticedDate(), Locale.ENGLISH, DateUtil.LONG_FORMAT));
            consent.put("effectiveOn", DateUtil.convertToString(
                noc.getEffectedDate(), Locale.ENGLISH, DateUtil.LONG_FORMAT));
            consents.add(consent);
        }
        return consents;
    }
   
    public ReportFinancial toFinancial(Map<String,Object> reports) throws DateFormatException,GenericException{
        try{
            ReportFinancial financial = new ReportFinancial();

            PositiveLoan pFinancials = getPositiveFinanceJson((SPositiveFinancials)reports.get("pFinancials"));
            financial.setPositiveLoan(pFinancials);

            LinkedHashMap<String, Object> nFinancials = getNegativeFinancialJson((SNegativeFinancials)reports.get("nFinancials"));
            financial.setNegativeLoan(nFinancials);

            LinkedHashMap<String, Object> writeOff = getWriteOff((SWriteOffFinancials)reports.get("writeOff"));
            if(writeOff.get("data") != null)
                financial.setWriteOff(writeOff);

            return financial;
        }
        catch(GenericException | NullPointerException e){
            logger.error(e.getMessage());
            throw e;
        }
    }
    private PositiveLoan getPositiveFinanceJson(SPositiveFinancials psf) throws DateFormatException,GenericException
    {
        PositiveLoan posLoan = new PositiveLoan();
        
        posLoan.setSummary(getPositiveFinancelSummary(psf));
        
        List<Object> list = getSubmitterWithFacCancelJson(psf);
        posLoan.setSubmitterWithFacCancel(returnNullIfEmpty(list));
        
        list = getSubmitterWithOutFacCancelJson(psf);
        posLoan.setSubmitterWithOutFacCancel(returnNullIfEmpty(list));
        
        return posLoan;
    }
    private LinkedHashMap<String, Object> getPositiveFinancelSummary(SPositiveFinancials psf) throws NullPointerException
    {
        LinkedHashMap<String, Object> posSum = new LinkedHashMap<>();
        posSum.put("noOfSubmitter", psf.getTotalAIsReported());
        posSum.put("dispute", positiveBoolean2YN(psf.isTotalDisputedStatus()));
        posSum.put("totalFac", psf.getTotalSummaryFacilityLimit());
        
        List<Object> loans = new ArrayList<>();
        if (null != psf.getTotalRevolvingFacilityLimit() &&
                0 != psf.getTotalRevolvingFacilityLimit().toString()
                        .trim().length())
        {
            LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
            loan.put("loanType", ReportResourceJson.getMessage(
                userPrefLang.getLocale(), "positive.label.revolving"));
            loan.put("totalFac", psf.getTotalRevolvingFacilityLimit());
            loan.put("shared", positiveBoolean2YN(psf.isTotalRevolvingShared3rdParty()));
            loans.add(loan);
        }

        if (null != psf.getTotalNonRevolvingFacilityLimit() &&
                0 != psf.getTotalNonRevolvingFacilityLimit().toString()
                        .trim().length())
        {
            LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
            loan.put("loanType", ReportResourceJson.getMessage(
                userPrefLang.getLocale(), "positive.label.non-revolving"));
            loan.put("totalFac", psf.getTotalNonRevolvingFacilityLimit());
            loan.put("shared", positiveBoolean2YN(psf.isTotalNonRevolvingShared3rdParty()));
            loans.add(loan);
        }
        if (null != psf.getTotalContigentFacilityLimit() &&
                0 != psf.getTotalContigentFacilityLimit().toString()
                        .trim().length())
        {
            LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
            loan.put("loanType", ReportResourceJson.getMessage(
                userPrefLang.getLocale(), "positive.label.contigent"));
            loan.put("totalFac", psf.getTotalContigentFacilityLimit());
            loan.put("shared", positiveBoolean2YN(psf.isTotalContigentShared3rdParty()));
            loans.add(loan);
        }

        if (null != psf.getTotalCombinedFacilityLimit() &&
                0 != psf.getTotalCombinedFacilityLimit().toString()
                        .trim().length())
        {
            LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
            loan.put("loanType", ReportResourceJson.getMessage(
                userPrefLang.getLocale(), "positive.label.combined"));
            loan.put("totalFac", psf.getTotalCombinedFacilityLimit());
            loan.put("shared", positiveBoolean2YN(psf.isTotalCombinedShared3rdParty()));
            loans.add(loan);
        }

        if (null != psf.getTotalHPLeasingFacilityLimit() &&
                0 != psf.getTotalHPLeasingFacilityLimit().toString()
                        .trim().length())
        {
            LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
            loan.put("loanType", ReportResourceJson.getMessage(
                userPrefLang.getLocale(), "positive.label.hpleasing"));
            loan.put("totalFac", psf.getTotalHPLeasingFacilityLimit());
            loan.put("shared", positiveBoolean2YN(psf.isTotalHPLeasingShared3rdParty()));
            loans.add(loan);
        }
        posSum.put("loan", returnNullIfEmpty(loans));
        return posSum;
    }
    
    private List<Object> getSubmitterWithOutFacCancelJson(SPositiveFinancials psf) throws GenericException{
        
        List<Object> woFacCancel = new ArrayList<>();
        
        SPositiveFinancialWithoutCancel[] pfwocData =
                psf.getPositiveWithoutCancal();
        
        if (null != pfwocData && 0 < pfwocData.length)
        {
            for (SPositiveFinancialWithoutCancel pfwoc : pfwocData)
            {
                if (null == pfwoc)continue;
                
                LinkedHashMap<String, Object> sub = new LinkedHashMap<>();
                sub.put("name", pfwoc.getSubmittedBy());
                sub.put("reportedIn", DateUtil.convertToString(
                    pfwoc.getPeriod(), Locale.ENGLISH));
                sub.put("dispute", positiveBoolean2YN(pfwoc.isDisputedStatus()));
                sub.put("disputeDate", DateUtil.convertToString(
                    pfwoc.getDisputedDate(), Locale.ENGLISH, DateUtil.LONG_FORMAT));
                sub.put("totalFac", pfwoc.getTotalFacilityLimit());
                sub.put("combinedDetail", ReportDataUtility.getCombineDetail(bCreditType,
                    pfwoc.getCombinedLimitDetails(), userPrefLang));
                
                String tangibleSecurityCode = pfwoc.getTangibleSecurity();
                if(tangibleSecurityCode != null){
                    TangibleSecurity fullTangibleSecurity = bTangibleSecurity.getTangibleSecurity(tangibleSecurityCode);
                    if(fullTangibleSecurity != null){
                        if(PreferLanguage.CHINESE.equals(userPrefLang))
                            sub.put("tangible", fullTangibleSecurity.getDescriptionLO());
                        else
                            sub.put("tangible", fullTangibleSecurity.getDescriptionEN());
                    }
                    else
                        sub.put("tangible", null);
                }
                else
                    sub.put("tangible", null);
                
                List<Object> loans = new ArrayList<>();
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.revolving"));
                    loan.put("totalFac", pfwoc.getRevolvingFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfwoc.isRevolvingShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.non-revolving"));
                    loan.put("totalFac", pfwoc.getNonRevolvingFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfwoc.isNonRevolvingShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.contigent"));
                    loan.put("totalFac", pfwoc.getContigentFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfwoc.isContigentShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.combined"));
                    loan.put("totalFac", pfwoc.getCombinedFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfwoc.isCombinedShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.hpleasing"));
                    loan.put("totalFac", pfwoc.getHPLeasingFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfwoc.isHPLeasingShared3rdParty()));
                    loans.add(loan);
                }
                sub.put("loan", returnNullIfEmpty(loans));
                woFacCancel.add(sub);
            }
        }
        return woFacCancel;
    }
    
    private List<Object> getSubmitterWithFacCancelJson(SPositiveFinancials psf) throws DateFormatException,GenericException{
        List<Object> wFacCancel = new ArrayList<>();
        
        SPositiveFinancialWithCancel[] pfcData = psf.getPositiveWithCancel();
        if (null != pfcData && 0 < pfcData.length)
        {
            for (SPositiveFinancialWithCancel pfc : pfcData)
            {
                if (null == pfc)continue;
                
                LinkedHashMap<String, Object> sub = new LinkedHashMap<>();
                sub.put("name", pfc.getSubmittedBy());
                sub.put("reportedIn", DateUtil.convertToString(
                    pfc.getFacilityCancelledDetailsPeriod(), Locale.ENGLISH));
                sub.put("reportedAsOf", DateUtil.convertToString(
                    pfc.getPreviousPeriod(), Locale.ENGLISH)) ;
                sub.put("dispute", positiveBoolean2YN(
                        pfc.isDisputedStatus()));
                sub.put("disputeDate", DateUtil.convertToString(
                    pfc.getDisputedDate(), Locale.ENGLISH, DateUtil.LONG_FORMAT));
                sub.put("facCanceledDate", DateUtil.convertToString(
                    pfc.getFacilityCancelledDetailsPeriod(), Locale.ENGLISH));
                sub.put("combinedDetail", ReportDataUtility.getCombineDetail(bCreditType,
                    pfc.getCombinedLimitDetails(), userPrefLang));

                String tangibleSecurityCode = pfc.getTangibleSecurity();
                if(tangibleSecurityCode != null){
                    TangibleSecurity fullTangibleSecurity = bTangibleSecurity.getTangibleSecurity(tangibleSecurityCode);
                    if(fullTangibleSecurity != null){
                        if(PreferLanguage.CHINESE.equals(userPrefLang))
                            sub.put("tangible", fullTangibleSecurity.getDescriptionLO());
                        else
                            sub.put("tangible", fullTangibleSecurity.getDescriptionEN());
                    }
                    else
                        sub.put("tangible", null);
                }
                else
                    sub.put("tangible", null);
                
                String status = pfc.getFacilityCancelledDetailsStatus();
                String statusCode = null;
                if (FAC_CANCEL_DETAIL_STATUS_WRITEOFF_REPAID.equals(status))
                    statusCode = pfc.isWriteOff() ?  "positive.label.writeoff" : "positive.label.repaid";
                else if (FAC_CANCEL_DETAIL_STATUS_WITHDRAW.equals(status))
                    statusCode = "positive.label.withdrawn";
                if(statusCode != null)
                    sub.put("facCanceledStatus", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), statusCode));
                else
                    sub.put("facCanceledStatus", null);
                
                sub.put("totalFac", pfc.getTotalFacilityLimit());
            
                List<Object> loans = new ArrayList<>();
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.revolving"));
                    loan.put("totalFac", pfc.getRevolvingFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfc.isRevolvingShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.non-revolving"));
                    loan.put("totalFac", pfc.getNonRevolvingFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfc.isNonRevolvingShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.contigent"));
                    loan.put("totalFac", pfc.getContigentFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfc.isContigentShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.combined"));
                    loan.put("totalFac", pfc.getCombinedFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfc.isCombinedShared3rdParty()));
                    loans.add(loan);
                }
                {
                    LinkedHashMap<String, Object> loan = new LinkedHashMap<>();
                    loan.put("loanType", ReportResourceJson.getMessage(
                        userPrefLang.getLocale(), "positive.label.hpleasing"));
                    loan.put("totalFac", pfc.getHPLeasingFacilityLimit());
                    loan.put("shared", positiveBoolean2YN(
                        pfc.isHPLeasingShared3rdParty()));
                    loans.add(loan);
                }
                sub.put("loan", returnNullIfEmpty(loans));
                wFacCancel.add(sub);
            }
        }
        return wFacCancel;
    }
    
    private String positiveBoolean2YN(boolean b)
    {
        try{
            return ReportResourceJson.getMessage(
                userPrefLang.getLocale(),
                b ? "positive.label.yes" : "positive.label.no"
            );
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }
    
    
    private LinkedHashMap<String, Object> getNegativeFinancialJson(SNegativeFinancials nf) throws DateFormatException{
        LinkedHashMap<String, Object> nFinancials = new LinkedHashMap<>();
        
        SNegativeFinancial nf12 = nf.getMax12Months();
        SNegativeFinancial nf24 = nf.getMax24Months();
        
        nFinancials.put("summary", getNegativeSummary(nf12, nf24));        
        nFinancials.put("pastDue", getNegatovePastDue(nf));
        
        return nFinancials;
    }
    private LinkedHashMap<String, Object> getNegativeSummary(SNegativeFinancial nf12, SNegativeFinancial nf24) throws DateFormatException{
        LinkedHashMap<String, Object> sum = new LinkedHashMap<>();

        LinkedHashMap<String, Object> firstReport = new LinkedHashMap<>();
        String past12M = DateUtil.convertToString(nf12.getPeriod(), Locale.ENGLISH);
        String past24M = DateUtil.convertToString(nf24.getPeriod(), Locale.ENGLISH);
        firstReport.put("past12M", past12M);
        firstReport.put("past24M", past24M);
        sum.put("firstReport", firstReport);
        
        LinkedHashMap<String, Object> submitter = new LinkedHashMap<>();
        submitter.put("past12M", nf12.getTotalAIsPastDue());
        submitter.put("past24M", nf24.getTotalAIsPastDue());
        sum.put("submitter", submitter);

        LinkedHashMap<String, Object> maxDaysPastDue = new LinkedHashMap<>();
        maxDaysPastDue.put("past12M", nf12.getMaximumDaysPastDue());
        maxDaysPastDue.put("past24M", nf24.getMaximumDaysPastDue());
        sum.put("maxDaysPastDue", maxDaysPastDue);

        LinkedHashMap<String, Object> maxAmtPastDueOver60Days = new LinkedHashMap<>();
        maxAmtPastDueOver60Days.put("past12M", nf12.getMaximunAmountPastDue60Days());
        maxAmtPastDueOver60Days.put("past24M", nf24.getMaximunAmountPastDue60Days());
        sum.put("maxAmtPastDueOver60Days", maxAmtPastDueOver60Days);

        LinkedHashMap<String, Object> totalAmtOfWriteOff = new LinkedHashMap<>();
        totalAmtOfWriteOff.put("past12M", nf12.getTotalWriteOff());
        totalAmtOfWriteOff.put("past24M", nf24.getTotalWriteOff());
        sum.put("totalAmtOfWriteOff", totalAmtOfWriteOff);

        LinkedHashMap<String, Object> totalNoOfAIWriteOff = new LinkedHashMap<>();
        totalNoOfAIWriteOff.put("past12M", nf12.getTotalAIsWriteOff());
        totalNoOfAIWriteOff.put("past24M", nf24.getTotalAIsWriteOff());
        sum.put("totalNoOfAIWriteOff", totalNoOfAIWriteOff);

        LinkedHashMap<String, Object> totalAmtOfRecovery = new LinkedHashMap<>();
        totalAmtOfRecovery.put("past12M", nf12.getTotalRecovery());
        totalAmtOfRecovery.put("past24M", nf24.getTotalRecovery());
        sum.put("totalAmtOfRecovery", totalAmtOfRecovery);

        LinkedHashMap<String, Object> totalNoOfAIRecovery = new LinkedHashMap<>();
        totalNoOfAIRecovery.put("past12M", nf12.getTotalAIsRecovery());
        totalNoOfAIRecovery.put("past24M", nf24.getTotalAIsRecovery());
        sum.put("totalNoOfAIRecovery", totalNoOfAIRecovery);
        
        return sum;
    }
    private LinkedHashMap<String, Object> getNegatovePastDue(SNegativeFinancials nf) throws NullPointerException{
        
        LinkedHashMap<String, Object> pastDue = new LinkedHashMap<>();
        
        Date deliverDate = (Date)rhd.get("reportIssuedDate");
        pastDue.put("period", DateUtil.convertToString(
            deliverDate, Locale.ENGLISH, DateUtil.SHORT_FORMAT));
        
        List<Object> data = new ArrayList<>();
        if(minorReportVersion == 0){
            if (null != nf.getNegativePast12Months() &&
                0 != nf.getNegativePast12Months().length)
                setNegatovePastDueData(data, nf.getNegativePast12Months());
        }
        else if (null != nf.getNegativePast60Months() &&
                0 != nf.getNegativePast60Months().length){
            if(minorReportVersion == 1)
                setNegatovePastDueData(data, nf.getNegativePast60Months());
            else if(minorReportVersion == 2)
                setNegatovePastDueData(data, nf.getNegativePast24Months());
        }
        pastDue.put("data", returnNullIfEmpty(data));
        
        return pastDue;
    }
    
    private void setNegatovePastDueData(List<Object> data, SNegativeFinancial[] negativeFinancialData) throws NullPointerException
    {
        for (SNegativeFinancial negativeFinancial : negativeFinancialData)
        {

            String period = negativeFinancial.getPeriod();
            Calendar c = Calendar.getInstance(Locale.ENGLISH);
            c.set(Calendar.YEAR, Integer.parseInt(period.substring(0, 4)));
            c.set(Calendar.MONTH, Integer.parseInt(period.substring(4, 6)) - 1);
            c.set(Calendar.DATE, 1);

            if (null != negativeFinancial.getMaximunAmountPastDue60Days() &&
                    0 != negativeFinancial.getMaximunAmountPastDue60Days()
                            .intValue())
            {
                LinkedHashMap<String, Object> ele = new LinkedHashMap<>();
                ele.put("reportDate",simpleDateFormatNegativeReport2.format(c.getTime()));
                ele.put("loanType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "positive.label.aggregate"));
                ele.put("pastDueOver60Days", negativeFinancial.getMaximunAmountPastDue60Days());
                ele.put("maxDaysPastDue", negativeFinancial.getMaximumDaysPastDue());
                data.add(ele);
            }

            if (null != negativeFinancial.getTotalRevolvingPastDue() &&
                    0 != negativeFinancial.getTotalRevolvingPastDue()
                            .intValue())
            {
                LinkedHashMap<String, Object> ele = new LinkedHashMap<>();
                ele.put("reportDate",simpleDateFormatNegativeReport2.format(c.getTime()));
                ele.put("loanType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "positive.label.revolving"));
                ele.put("pastDueOver60Days", negativeFinancial.getTotalRevolvingPastDue());
                ele.put("maxDaysPastDue", negativeFinancial.getMaximumDaysRevolving());
                data.add(ele);
            }

            if (null != negativeFinancial.getTotalNonRevolvingPastDue() &&
                    0 != negativeFinancial.getTotalNonRevolvingPastDue()
                            .intValue())
            {
                LinkedHashMap<String, Object> ele = new LinkedHashMap<>();
                ele.put("reportDate",simpleDateFormatNegativeReport2.format(c.getTime()));
                ele.put("loanType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "positive.label.non-revolving"));
                ele.put("pastDueOver60Days", negativeFinancial.getTotalNonRevolvingPastDue());
                ele.put("maxDaysPastDue", negativeFinancial.getMaximumDaysNonRevolving());
                data.add(ele);
            }

            if (null != negativeFinancial.getTotalContigentPastDue() &&
                    0 != negativeFinancial.getTotalContigentPastDue()
                            .intValue())
            {
                LinkedHashMap<String, Object> ele = new LinkedHashMap<>();
                ele.put("reportDate",simpleDateFormatNegativeReport2.format(c.getTime()));
                ele.put("loanType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "positive.label.contigent"));
                ele.put("pastDueOver60Days", negativeFinancial.getTotalContigentPastDue());
                ele.put("maxDaysPastDue", negativeFinancial.getMaximumDaysContigent());
                data.add(ele);
            }

            if (null != negativeFinancial.getTotalHPLeasingPastDue() &&
                    0 != negativeFinancial.getTotalHPLeasingPastDue()
                            .intValue())
            {
                LinkedHashMap<String, Object> ele = new LinkedHashMap<>();
                ele.put("reportDate",simpleDateFormatNegativeReport2.format(c.getTime()));
                ele.put("loanType", ReportResourceJson.getMessage(
                    userPrefLang.getLocale(), "positive.label.hpleasing"));
                ele.put("pastDueOver60Days", negativeFinancial.getTotalHPLeasingPastDue());
                ele.put("maxDaysPastDue", negativeFinancial.getMaximumDaysHPLeasing());
                data.add(ele);
            }
        }
    }
    private LinkedHashMap<String, Object> getWriteOff(SWriteOffFinancials wof)throws NullPointerException {
        SWriteOffFinancial[] swfData = wof.getWriteOffs();
        
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        
        LinkedHashMap<String, Object> writeOff = new LinkedHashMap<>();
        
        writeOff.put("period", DateUtil.convertToString(
            cal.getTime(), Locale.ENGLISH, DateUtil.SHORT_FORMAT));
        
        List<Object> data = new ArrayList<>();
        for (SWriteOffFinancial swf : swfData)
        {
            if ((null == swf.getTotalWriteOff() || 0 == swf.getTotalWriteOff().intValue())
                && (null == swf.getTotalRecovery() || 0 == swf.getTotalRecovery().intValue()))
                continue;
            
            cal.set(
                Integer.parseInt(swf.getPeriod().substring(0,4)), 
                Integer.parseInt(swf.getPeriod().substring(4,6))-1,
                1
            );
            
            LinkedHashMap<String, Object> ele = new LinkedHashMap<>();
            ele.put("reportDate",DateUtil.convertToString(
                cal.getTime(), Locale.ENGLISH, DateUtil.SHORT_FORMAT));
            ele.put("amtWriteOff", swf.getTotalWriteOff());
            ele.put("amtRecovery", swf.getTotalRecovery());
            data.add(ele);      
        }        
        writeOff.put("data", returnNullIfEmpty(data));
        
        return writeOff;
        
    }
    private void handleChineseValueFromOrderLang(Map<String, Object> map, String propName, String val){
        if(orderedLang == OBJECT_TYPE.CHINESE_REPORT || val == null || val.isBlank())
            map.put(propName, val);
        else
            map.put(propName, "Data in Chinese");
    }
    private List<Object> returnNullIfEmpty(List<Object> js){
        return js == null || js.isEmpty() ? null : js;        
    }
}
