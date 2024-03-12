package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.InquiryUtilities.getPeriod;
import com.arg.ccra3.dao.util.ReportResource;
import com.arg.ccra3.models.inquiry.data.Demographic;
import com.arg.cb2.inquiry.DateUtil;
import com.arg.cb2.inquiry.ErrorMessages;
import static com.arg.cb2.inquiry.InquiryConstants.*;
import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.data.ReportCreatorData;
import com.arg.cb2.inquiry.data.ReportData;
import com.arg.cb2.inquiry.data.SComStructureLO;
import com.arg.cb2.inquiry.data.SComStructures;
import com.arg.cb2.inquiry.data.SCourts;
import com.arg.cb2.inquiry.data.SExecutiveSummary;
import com.arg.cb2.inquiry.data.SFacilitiesCancel;
import com.arg.cb2.inquiry.data.SNegativeFinancial;
import com.arg.cb2.inquiry.data.SNegativeFinancials;
import com.arg.cb2.inquiry.data.SNoticeOfConsent;
import com.arg.cb2.inquiry.data.SNoticeOfConsents;
import com.arg.cb2.inquiry.data.SPositiveFinancialWithCancel;
import com.arg.cb2.inquiry.data.SPositiveFinancials;
import com.arg.cb2.inquiry.data.SProfileLO;
import com.arg.cb2.inquiry.data.SProfiles;
import com.arg.cb2.inquiry.data.SWriteOffFinancial;
import com.arg.cb2.inquiry.data.SWriteOffFinancials;
import com.arg.cb2.inquiry.data.SuspectAbnormalData;
import com.arg.cb2.inquiry.util.HistoryRevocationData;
import com.arg.cb2.spm.core.data.ObjectData;
import com.arg.ccra3.dao.repo.MalProductDeliverDataAPIRepo;
import static com.arg.ccra3.dao.util.ReportCommonDaoUtil.*;
import com.arg.ccra3.dao.util.RevocationSQLUtil;
import com.arg.ccra3.models.report.ReportBody;
import com.arg.util.GenericException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;
import com.arg.ccra3.dao.util.ReportJsonConvertor;
import com.arg.ccra3.dao.util.ReportResourceJson;
import com.arg.ccra3.models.User;
import com.arg.ccra3.models.api.DataBlockStream;
import com.arg.ccra3.models.api.MalProductDeliverDataAPI;
import com.arg.ccra3.models.report.Attention;
import com.arg.ccra3.models.report.ReportFinancial;
import com.arg.ccra3.models.report.ReportGeneral;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


public class ReportCreatorDAO{
    
    private static final Logger logger = LogManager.getLogger(ReportCreatorDAO.class);

    final Locale locale = Locale.ENGLISH;
    
    private final JdbcTemplate jdbcTemplate;
    private final MalProductDeliverDataAPIRepo malDataRepo;
    
    String loggerText = "";
    public ReportCreatorDAO(JdbcTemplate jdbcTemplate, MalProductDeliverDataAPIRepo malDataRepo){
        this.jdbcTemplate = jdbcTemplate;
        this.malDataRepo = malDataRepo;
    }

    private static final String REPAID = "1";
    private static final String FACILITIES_CANCEN = "3";
    private static final String CREATE_REPORT_LABEL = "C";
    private static final String SQL_UPDATE_EXPENSEDATA = "update spm_expense set requesttime = ?, responsetime = ?, flagexpense = ?, " +
                "productusage = ? , cbuid = ?, acc_mnger_code = ?, customer_no = ?, brc_no = ?, ci_no = ?, " +
                "loc_branch_id = ?, other_reg_no = ?, place_of_reg = ?, unitcharge = ?, submission_flag = ?, " +
                "revoc_effect_date = ?, flagcharge = 1, disabled = 0 where expenseid = ?";
    private static final String SQL_UPDATE_MAILPRODUCTDELIVER = "update mal_productdeliver set expenseid = ? , objectid = ?, companyname = ?, matchkey = ?,minorVersion = ? where transactionid = ?";
    private static final String TRANS_SUSPECT_ABNORMAL = "insert into API_TRANS_SUSPECT_ABNORMAL(ORDER_DATE, SESSIONID, OBJECTID, COMPTYPE, HKBRC, HKCI, " +
                "OTHER_REG_NO, PLACE_OF_REG, CUSTOMER_NO, " +
                "COMPANY_NAME_ENG, COMPANY_NAME_LO, ORDER_BY, " +
                "GROUPID, CBUID, GROUPAIID, " +
                "GROUPCCRAID, EXPENSEID) " +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? )";
    private static final String UPDATE_TRANS_SUSPECT_ABNORMAL = "update API_TRANS_SUSPECT_ABNORMAL set ORDER_DATE = ? where EXPENSEID = ?";
    private static final String GROUPCCRAID_SPM_USER = "SELECT GROUPCCRAID FROM SPM_USER (nolock) WHERE  SPM_USER.UID = ?" ;

    public ReportBody createReport(ReportCreatorData data, User user, int amountPastDueInYears, int minorVersion, AIOrderDAO aiOrder)throws Exception
    {
        if (null == data.getCBUID())
            throw new GenericException("ICE-A00005",
                ErrorMessages.getString("ICE-A00005"));
        try
        {   
            JSONObject header = getHeader(data.getTransID(), user);
        
            ReportJsonConvertor converter = initReportJsonConvertor(data, user, header, minorVersion);
            Map<String, Object> reports = getAllReportData(data, amountPastDueInYears, aiOrder);            
            ReportGeneral general = converter.toGeneral(reports);
            ReportFinancial financial = converter.toFinancial(reports);
            List<Object> dataBlocks = ((DataBlockStream)reports.get("datablocks")).getDatas();
            
            return createReportBody(
                header,
                general,
                financial,
                dataBlocks
            );
        }
        catch (Exception e)
        {
            logger.error("createReport: " + e.getMessage());
            throw new GenericException("ICE-C00001",
                ErrorMessages.getString("ICE-C00001"));
        }
    }
    
    private ReportBody createReportBody(JSONObject header, ReportGeneral general, ReportFinancial financial, List<Object> datablocks) throws Exception{
        ReportBody reportBody = new ReportBody();
        
        reportBody.setGeneral(general);
        reportBody.setFinancial(financial);
        reportBody.setAlternativeData(datablocks);
        
        reportBody.setNullToPropertiesIfNoValue();
        
        boolean ccraData = get_ccraData_from_NullifiedNoValueProperties(reportBody);
        header.put("ccraData", ccraData ? "Y" : "N");
        reportBody.setHeader(header.toMap());
        
        reportBody.simplfyReportBody();
        
        return reportBody;
    }
    private JSONObject getHeader(Long tranId, User user){
        JSONObject header = new JSONObject();
        
        jdbcTemplate.query(
            "select * from VIEW_GET_RP_HEADER (nolock) where transactionid = ?",
            (ResultSet rst) -> {
                logger.info("ResultSet: " + rst);
                do{
                    header.put("reportName", rst.getInt("objectid"))
                        .put("companyName", rst.getString("ainame"))
                        .put("aiRefCode1", rst.getString("airefcode1"))
                        .put("aiRefCode2", rst.getString("airefcode2"))
                        .put("aiRefCode3", rst.getString("airefcode3"))
                        .put("reportIssuedDate", rst.getTimestamp("responsetime"))
                        .put("reasonCode", rst.getInt("reasoncode"))
                        .put("reportRefNumber", rst.getString("report_ref_no"));
                    logger.info("getHeader: " + header);
                }while (rst.next());
            },
            tranId
        );
        
        PreferLanguage userLang = user.getPreferLanguage();
        header.put("attention", getAttention(user, userLang));
        header.put("reasonCode", getReasonCode(jdbcTemplate, header.optInt("reasonCode"), userLang));        
        header.put("reportName", switch(header.getInt("reportName")){
            case 203 -> ReportResourceJson.getMessage(userLang.getLocale(), "basicreport.label.ccra-report");
            case 204 -> ReportResourceJson.getMessage(userLang.getLocale(), "basicreport.label.ccra-chinese-report");
            default -> null;
        });
        
        return header;
    }
    private Attention getAttention(User user, PreferLanguage userLang){
        Attention attention = new Attention();
        attention.setUserID(user.getUserID())
            .setAddress1(user.getAddressEng1())
            .setAddress2(user.getAddressEng2())
            .setAddress3(user.getAddressEng3())
            .setAreaCode(user.getAreaCode())
            .setCountry(user.getCountry());
        
        if(userLang.equals(PreferLanguage.CHINESE))
            attention.setCity(user.getCityLocal())
                .setProvince(user.getProvinceLocal());
        else
            attention.setCity(user.getCity())
                .setProvince(user.getProvince());
        
        return attention;
    }
    
    private ReportJsonConvertor initReportJsonConvertor(ReportCreatorData data, User user, JSONObject header, int minorVersion){
        return new ReportJsonConvertor().setUserPrefLang(user.getPreferLanguage())
            .setbCreditType(getCreditType(jdbcTemplate))
            .setbTangibleSecurity(getTangibleSecurity(jdbcTemplate))
            .setOrderedLang(data.getObjectID())
            .setRhd(header)
            .setMinorReportVersion(minorVersion);
    }
    
    private Map<String, Object> getAllReportData(ReportCreatorData data, int amountPastDueInYears, AIOrderDAO aiOrder) throws Exception{
        Map<String, Object> reports = new HashMap<>();  
        
        Integer uGroupAIID = data.getAIID();
        Integer cbuid = data.getCBUID();
        Integer objectID = data.getObjectID();
        
        ObjectData objData = this.findObjectDetail(objectID);
        String documentLanguage = objData.getDocumentLanguage();
        reports.put("docLang", documentLanguage);

        SExecutiveSummary execuSummary = getExecutiveSummarySection(uGroupAIID, aiOrder, cbuid, documentLanguage,amountPastDueInYears);
        SPositiveFinancials pFinancials = getPositiveFinanceSection(uGroupAIID,aiOrder, cbuid, documentLanguage,amountPastDueInYears);
        convertPositiveFinancialToExecutiveSummary(execuSummary, pFinancials);
        reports.put("execuSummary", execuSummary);
        reports.put("pFinancials", pFinancials);

        SProfiles profiles = convertDemoToProfiles(
            uGroupAIID, aiOrder, documentLanguage,
            getDemographicSection(cbuid, amountPastDueInYears),
            cbuid
        );
        reports.put("profile", profiles.getProfiles());
        
        SComStructures constructs = convertDemoToComStructures(
            uGroupAIID, aiOrder, documentLanguage,
            getAllDemographicSection(cbuid, amountPastDueInYears), 
            cbuid
        );
        reports.put("groupStructure", constructs.getComStructures());
        
        SNoticeOfConsents consents = getRevocationOfConsentSection(
            uGroupAIID, aiOrder, cbuid, documentLanguage, amountPastDueInYears
        );
        reports.put("revOfConsent", consents.getNoticeOfConsents());
        
        SCourts courts = getCourtSection(uGroupAIID, aiOrder, cbuid,documentLanguage);
        reports.put("court", courts.getCourts());

        SNegativeFinancials nFinancials = getNegativeFinanceSection(uGroupAIID, cbuid, documentLanguage);
        reports.put("nFinancials", nFinancials);
        
        SWriteOffFinancials writeoff = convertNegativeFinancialToWriteOff(documentLanguage,nFinancials,amountPastDueInYears);
        reports.put("writeOff", writeoff);
        
        DataBlockStream datablocks = getDataBlocks(data.getUID(), (String)reports.get("docLang"));
        reports.put("datablocks", datablocks);
        
        Long transactionID = data.getTransID();
        Long expenseID = data.getExpenseID();
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.EXECUTIVE_SUMMARY, execuSummary);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.PROFILE, profiles);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.COMPANY_GROUP_STRUCTURE, constructs);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.COURT, courts);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.REVOCATION_OF_CONSENT, consents);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.POSITIVE_LOAN_DATA, pFinancials);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.NEGATIVE_LOAN_DATA, nFinancials);
        recordReportSection(transactionID, expenseID, PRODUCT_DATA_CONSTANTS.WRITEOFF, writeoff);
        recordReportSection(transactionID, expenseID, 25, datablocks);
        
        return reports;
    }

    private void convertPositiveFinancialToExecutiveSummary(
        SExecutiveSummary sum, SPositiveFinancials pFinancials)
    {
        if (null != pFinancials && null != sum){
            SPositiveFinancialWithCancel[] cancels = pFinancials.getPositiveWithCancel();

            for(SPositiveFinancialWithCancel cancel : cancels){
                sum.addFacilitiesCancel(new SFacilitiesCancel(
                    cancel.getCBUID(), cancel.getSubmittedBy(),
                    cancel.getPeriod(),
                    cancel.getFacilityCancelledDetailsStatus(),
                    cancel.isWriteOff(), cancel.getGroupID(),
                    cancel.isStatusMerge()));
            }
        }
    }
    private SComStructures convertDemoToComStructures(Integer uGroupID,
        AIOrderDAO aiOrder, String docLanguage, Demographic[] datas , Integer cbuid) throws Exception
    {
        SComStructures result = new SComStructures(docLanguage);

        if (datas != null){
            for(Demographic data : datas){
                if (data.getSharedHolderNameEN() != null && !data.getSharedHolderNameEN().isBlank()
                     ||
                     data.getSharedHolderNameLO() != null && !data.getSharedHolderNameLO().isBlank())
                {
                    result.addComStructure(new SComStructureLO(data.getCBUID(),
                        aiOrder.getAIName(uGroupID, data.getGroupID(), docLanguage ,cbuid),
                        data.getSharedHolderNameEN(), data.getSharedHolderNameLO()));
                }
            }
        }

        return result;
    }


    private SProfiles convertDemoToProfiles(Integer uGroupID,
        AIOrderDAO aiOrder, String docLanguage, Demographic[] datas ,Integer cbuid) throws Exception
    {
        SProfiles result = new SProfiles(docLanguage);

        if (datas != null){
            for(Demographic data : datas)
                result.addProfile(new SProfileLO(data.getCBUID(),
                    aiOrder.getAIName(uGroupID, data.getGroupID(), docLanguage ,cbuid),
                    data.getPeriod(), data.getBRCNO(), data.getCINO(),
                    data.getOtherRegNO(), data.getPlaceOfReg(),
                    data.getNameEN(), data.getNameLO(), data.getTradeNameEN(),
                    data.getTradeNameLO(), data.getPrevNameEN(),
                    data.getPrevNameLO(), data.getAddressEN1(),
                    data.getAddressLO1(), data.getAddressEN2(),
                    data.getAddressLO2(), data.getAddressEN3(),
                    data.getAddressLO3(), data.getAreaCode(), data.getCityEN(),
                    data.getCityLO(), data.getProvinceEN(),
                    data.getProvinceLO(), data.getCountry(),
                    data.getPostCode(), data.getPhoneNO()));
        }

        return result;
    }

    public SWriteOffFinancials convertNegativeFinancialToWriteOff(
        String docLanguage, SNegativeFinancials nFinancial,int years)
    {
        SWriteOffFinancials result = new SWriteOffFinancials(docLanguage);

        if(years==5){
            if (null != nFinancial && null != nFinancial.getPast60Months())
            {
                List<SNegativeFinancial> past60Months = nFinancial.getPast60Months();
                for(var data : past60Months){
                    result.addWriteOff(new SWriteOffFinancial(data.getCBUID(),
                        data.getPeriod(), data.getTotalWriteOff(),
                        data.getTotalAIsWriteOff(), data.getTotalRecovery(),
                        data.getTotalAIsRecovery(),data.getReportBy()));
                }
            }
        }
        else if (null != nFinancial && null != nFinancial.getPast24Months())
        {
            List<SNegativeFinancial> past24Months = nFinancial.getPast24Months();
            for(var data : past24Months){
                result.addWriteOff(new SWriteOffFinancial(data.getCBUID(),
                    data.getPeriod(), data.getTotalWriteOff(),
                    data.getTotalAIsWriteOff(), data.getTotalRecovery(),
                    data.getTotalAIsRecovery(),data.getReportBy()));
            }
        }

        return result;
    }


    private ObjectData findObjectDetail(int objectID)
        throws Exception
    {
        final ObjectData obj = new ObjectData();

        jdbcTemplate.query(
            ReportResource.getString("reportcreator.preparedstatement.objectdetail"),
            (ResultSet rs) -> {
                do {
                    int temp_int = rs.getInt("parentobjectid");
                    obj.setParentObjectID(rs.wasNull() ? null : temp_int);
                    double temp_dob = rs.getDouble("costofobject");
                    obj.setCostOfObject(rs.wasNull() ? null : temp_dob);
                    obj.setCreatedBy(rs.getInt("createdby"));
                    obj.setCreatedDate(rs.getDate("createddate"));
                    obj.setDeleted(rs.getBoolean("deleted"));
                    obj.setDocumentLanguage(rs.getString("documentlanguage"));
                    obj.setObjectDescription(rs.getString("objectdesc"));
                    obj.setObjectID(objectID);
                    obj.setObjectLabel(rs.getString("objectlabel"));
                    obj.setObjectType(rs.getString("objecttype"));
                    obj.setUpdatedBy(rs.getInt("updatedby"));
                    obj.setUpdatedDate(rs.getDate("updateddate"));
                  
                }while (rs.next());
            },
            objectID
        );

        return obj;
    }
    private SCourts getCourtSection(
        Integer userGroupID, AIOrderDAO aiDao,
        Integer CBUID, String docLang)
    throws Exception
    {
        try{
            return new CourtDAO(jdbcTemplate).getCourtList(userGroupID, aiDao, CBUID, docLang);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00011",
                ErrorMessages.getString("ICE-A00011"));
        }
    }


    private Demographic[] getDemographicSection(Integer CBUID,int years)
    throws Exception
    {
        try{
            return new DemographicDAO(jdbcTemplate).getProfileDemographicList(CBUID, years);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00010",
                ErrorMessages.getString("ICE-A00010"));
        }
    }


    private Demographic[] getAllDemographicSection(Integer CBUID, int years)
    throws Exception
    {
        try{
            return new DemographicDAO(jdbcTemplate).getAllDemographicList(CBUID, years);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00010",
                ErrorMessages.getString("ICE-A00010"));
        }
    }


    private SExecutiveSummary getExecutiveSummarySection(Integer userGroupID,
        AIOrderDAO aiDao, Integer CBUID, String docLang, int years)
        throws Exception
    {
        try{
            return new ExecutiveSummaryDAO(jdbcTemplate).getExecutiveSummary(userGroupID, aiDao, CBUID, docLang, years);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00009",
                ErrorMessages.getString("ICE-A00009"));
        }
    }


    private SNegativeFinancials getNegativeFinanceSection(Integer userGroupID,
        Integer CBUID, String docLang)
        throws Exception
    {
        try{
            return new NegativeFinancialDAO(jdbcTemplate).getNegativeFinancial(userGroupID, CBUID,docLang);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00014",
                ErrorMessages.getString("ICE-A00014"));
        }
    }


    private SPositiveFinancials getPositiveFinanceSection(Integer userGroupID,
        AIOrderDAO aiDao, Integer CBUID, String docLang, int years)
        throws Exception
    {
        try{
            return new PositiveFinancialDAO(jdbcTemplate).getPositiveFinancials(userGroupID, aiDao, CBUID, docLang,years);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00013",
                ErrorMessages.getString("ICE-A00013"));
        }
    }


    private SNoticeOfConsents getRevocationOfConsentSection(
        Integer userGroupID, AIOrderDAO aiDao, Integer CBUID, String docLang, int years)
        throws Exception
    {
        try{
            return getRevocationOfConsent(userGroupID, aiDao, CBUID,docLang, years);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new GenericException("ICE-A00012",
                ErrorMessages.getString("ICE-A00012"));
        }
    }
    public SNoticeOfConsents getRevocationOfConsent(Integer userGroupID,
        AIOrderDAO aiDao, Integer CBUID, String docLang,int years)
        throws Exception
    {
        SNoticeOfConsents consents = new SNoticeOfConsents(docLang);

        Calendar cal = Calendar.getInstance(locale);
        cal.add(Calendar.YEAR, -years);

        List<HistoryRevocationData> revocationList =
            RevocationSQLUtil.selectHistoryRevocation(jdbcTemplate,
                CBUID, new java.sql.Date(cal.getTimeInMillis()),years);

        for(var tmpData : revocationList){
            int gid = Integer.parseInt(tmpData.getGroupID());

            consents.addNoticeOfConsent(new SNoticeOfConsent(CBUID,
                    aiDao.getAIName(userGroupID, gid, docLang ,CBUID),
                    tmpData.getRevocNoticedDate(),
                    tmpData.getRevocEffectDate()));
        }

        return consents;
    }


    public void insertSuspectedAbnormal(Integer sessionID,
                                        Integer objectID, Integer orderBy,
                                        Integer groupID, Integer groupAIID,
                                        Integer cbuID, Long expenseID,
                                        Date responseTime)
        throws GenericException
    {
        try
        {
            logger.info("insertSuspectedAbnormal");
            SuspectAbnormalData suspData = searchSuspectAbnormal(cbuID, groupAIID);
            if(suspData == null)
                throw new IllegalArgumentException("searchSuspectAbnormal return null");

            Integer groupCCRAID = jdbcTemplate.queryForObject(GROUPCCRAID_SPM_USER, Integer.class, orderBy);

            jdbcTemplate.update(
                TRANS_SUSPECT_ABNORMAL,
                new Object[]{
                    new Timestamp(responseTime.getTime()),
                    sessionID,
                    objectID,
                    suspData.getCompanyType() == null ? 0 : suspData.getCompanyType(),
                    suspData.getHKBRCNO(),
                    suspData.getHKCINO(),
                    suspData.getOtherRegNo(),
                    suspData.getPlaceOfRegis(),
                    suspData.getCustomerNO(),
                    suspData.getCompanyNameEN(),
                    suspData.getCompanyNameLO(),
                    orderBy,
                    groupID,
                    cbuID,
                    groupAIID,
                    groupCCRAID,
                    expenseID
                }
            );
        }
        catch (Exception ce)
        {
            loggerText = String.format("Could not execute query %s",ce);
            logger.error(loggerText);
        }
    }
    
    public void updateSuspectedAbnormal(Integer groupAIID,
                                        Integer cbuID, Long expenseID,
                                        Date responseTime)
        throws GenericException
    {
        try
        {
            logger.info("updateSuspectedAbnormal");
            SuspectAbnormalData suspData = searchSuspectAbnormal(cbuID, groupAIID);
            if(suspData == null)
                throw new IllegalArgumentException("searchSuspectAbnormal return null");

            jdbcTemplate.update(
                UPDATE_TRANS_SUSPECT_ABNORMAL,
                new Object[]{
                    new Timestamp(responseTime.getTime()),
                    expenseID
                }
            );
        }
        catch (Exception ce)
        {
            loggerText = String.format("Could not execute query %s",ce);
            logger.error(loggerText);
        }
    }

    public SuspectAbnormalData searchSuspectAbnormal(int cbuID, int groupID) throws Exception
    {
        final SuspectAbnormalData suspData = new SuspectAbnormalData();
        try
        {
            jdbcTemplate.query(
                ReportResource.getString("search.suspected.abnormal.data"),
                (ResultSet rst) -> {
                    do{
                        suspData.setCBUID(rst.getInt(1));
                        suspData.setGroupID(rst.getInt(2));
                        suspData.setCompanyNameLO(rst.getString(10));
                        suspData.setCompanyNameEN(rst.getString(9));
                        suspData.setCompanyType(rst.getInt(3));
                        suspData.setHKBRCNO(rst.getString(4));
                        suspData.setHKCINO(rst.getString(5));
                        suspData.setOtherRegNo(rst.getString(6));
                        suspData.setPlaceOfRegis(rst.getString(7));
                        suspData.setCustomerNO(rst.getString(8));
                       
                    }while (rst.next());
                },
                new Object[]{groupID, cbuID}
            );
        }
        catch(DataAccessException e){
            String errorLog = String.format("Error : %s", e);
            logger.error(errorLog);
            throw e;
        }
        return suspData;
    }

    private DataBlockStream getDataBlocks(Integer uID, String docLang){
        
        DataBlockStream dataBlockStream = new DataBlockStream(docLang);                
                
        String sql = "SELECT D.BLOCKNAME, D.BLOCKDESC,D.BLOCKURL " +
            "FROM API_MAP_USER_DATABLOCK M (nolock)" +
            "INNER JOIN API_DATABLOCK D ON M.BLOCKID = D.BLOCKID " +
            "WHERE M.UID = ? " +
            "ORDER BY D.BLOCKNAME";
        
        logger.info("getDataBlocks: " + sql + " : " + uID);
        
        jdbcTemplate.query(
            sql,
            (ResultSet rst) -> {
                do{
                    LinkedHashMap<String, Object> block = new LinkedHashMap<>();
                    block.put("name", rst.getString("BLOCKNAME"));
                    block.put("description", rst.getString("BLOCKDESC"));
                    block.put("url", rst.getString("BLOCKURL"));
                    dataBlockStream.add(block);
                }while (rst.next());
            },
            uID
        );
        
        return dataBlockStream;
    }
    
    private void recordReportSection(Long transactionID, Long expenseID,
        int objectType, ReportData obj) throws IOException 
    {
        if (null == obj)
            return;
        
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            String objStream = java.util.Base64.getEncoder().encodeToString(bos.toByteArray());
            
            int i = 1;
            while(objStream.length() > 0){
                int len = objStream.length() > 4000 ? 4000 : objStream.length();
                MalProductDeliverDataAPI malData = new MalProductDeliverDataAPI();
                malData.setTransactionId(transactionID);
                malData.setExpenseId(expenseID);
                malData.setObjectType(objectType);
                malData.setObjectNumber(i++);
                malData.setProductObject(objStream.substring(0, len));
                malDataRepo.save(malData);
                objStream = objStream.substring(len);
            }
        }
    }
    
    private boolean get_ccraData_from_NullifiedNoValueProperties(ReportBody reportBody){
        
        ReportGeneral general = reportBody.getGeneral();
        ReportFinancial financial = reportBody.getFinancial();
                
        boolean hasData = general!= null && (
                isExecSumNoData((LinkedHashMap<String, Object>) general.getExecutiveSummary())
                || general.getCourt() != null
                || general.getGroupStructure() != null
                || general.getProfile() != null
                || general.getRevOfConsent() != null);
        hasData |= financial.getWriteOff() != null;
        var pos = financial.getPositiveLoan();//sry this's an object, can't do recursion
        hasData |= pos.getSubmitterWithFacCancel() != null || pos.getSubmitterWithOutFacCancel() != null;
        var neg = (Map<String, Object>)financial.getNegativeLoan();
        hasData |= ((Map<String, Object>)neg.get("pastDue")).get("data") != null;//period is not null, can't do recursion
        hasData |= isAllpast12N24MNotNull((Map<String, Object>)neg.get("summary"));        
        return hasData;
    }
    private boolean isExecSumNoData(LinkedHashMap<String, Object> execSum){
        boolean hasData = execSum != null;
        
        if(hasData){
            hasData = execSum.get("reSubmission") != null
                || execSum.get("unload") != null
                || execSum.get("updateRecord") != null
                || execSum.get("revOfConsent") != null
                || execSum.get("evidenceOfCourt") != null
                || execSum.get("dispute") != null
                || execSum.get("fac") != null
                || execSum.get("first") != null;

            String[] fields = new String[]{"submitter", "pastDue60", "recovered"};
            for(int i = 0; i < 3 && !hasData; i++){
                LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) execSum.get(fields[i]);
                if(obj != null)
                    hasData |= obj.get("total") != null || obj.get("amount") != null;
            }
        }
        return hasData;
    }
    private boolean isAllpast12N24MNotNull(Map<String, Object> summary){
        boolean hasData = false;
        String[] propNames = new String[]{
            "firstReport",
            "submitter",
            "maxDaysPastDue",
            "maxAmtPastDueOver60Days",
            "totalAmtOfWriteOff",
            "totalNoOfAIWriteOff",
            "totalAmtOfRecovery",
            "totalNoOfAIRecovery"
        };
        for(String str : propNames){
            var map = (Map<String, Object>) summary.get(str);
            hasData |= map.get("past12M") != null || map.get("past24M") != null;
        }
        return hasData;
    }
    
    
}
