package com.arg.ccra3.online.service;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.http.HttpServletResponse;

import com.arg.cb2.inquiry.data.ReportCreatorData;
import com.arg.ccra3.common.FileManagerUtil;
import org.springframework.stereotype.Service;
import static com.arg.ccra3.common.InquiryConstants.*;
import com.arg.ccra3.dao.AIOrderDAO;
import com.arg.ccra3.dao.ExpenseAPIDAO;
import com.arg.ccra3.dao.HTMLReportCreatorDAO;
import com.arg.ccra3.dao.MalProductDeliverAPIDAO;
import com.arg.ccra3.dao.ReportCreatorDAO;
import com.arg.ccra3.dao.SearchDAO;
import com.arg.ccra3.dao.TransactionAPIDAO;
import com.arg.ccra3.dao.UserDAO;
import com.arg.ccra3.dao.util.ValidateReportRule;
import com.arg.ccra3.models.ResponseModel;
import com.arg.ccra3.models.TrnJson;
import com.arg.ccra3.models.User;
import com.arg.ccra3.models.report.ReportBody;
import com.arg.ccra3.online.form.ReportAPIRequest;
import com.arg.util.GenericException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.zeroturnaround.zip.ZipUtil;

@Service
public class ReportService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    Environment env;
    
    @Autowired
    HTMLReportCreatorDAO htmlDao;
    @Autowired
    UserDAO userDAO;
    @Autowired
    TransactionAPIDAO transactionAPIDAO;
    @Autowired
    ExpenseAPIDAO expenseDAO;
    @Autowired
    ReportCreatorDAO reportCreatorDao;
    @Autowired
    MalProductDeliverAPIDAO malDao;
    @Autowired
    SearchDAO searchDao;
    @Autowired
    ValidateReportRule validateRule;
    @Autowired
    AIOrderDAO aiOrder;
    
    private final Logger logger = (Logger) LoggerFactory.getLogger(ReportService.class);
    private String loggerText = "";
    
    public Map<String, Object> creatReport(ReportAPIRequest uForm,TrnJson trnJsonObjResponse) throws Exception{
        logger.info(">>>>CreateReport<<<<");
        loggerText = String.format(">>>>matchTranId::  %s",uForm.getMatchTranId());
        logger.info(loggerText);
        loggerText = String.format(">>>>uForm::  %s",uForm);
        logger.info(loggerText);
        try{
            return getReportBody(uForm,trnJsonObjResponse);
        }
        catch(IllegalArgumentException e){
            loggerText = String.format("IllegalArgumentException  %s",e);
            logger.error(loggerText);
            throw e;
        }
        catch(Exception e){
            loggerText = String.format("Exception  %s",e);
            logger.error(loggerText);
            throw e;
        }
    }
    private Map<String, Object> createReportJson(ReportAPIRequest uForm, ReportBody report) throws Exception{        
        LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("inquiry", setInquiry(uForm));
        
        String ccraData = (String)((Map<String, Object>)report.getHeader()).get("ccraData");
        switch(ccraData){
            case "Y"->{ 
                response.put("report", report);
            }
            case "N"->{
                LinkedHashMap<String, Object> reportBody = new LinkedHashMap<>();
                reportBody.put("header", report.getHeader());                
                reportBody.put("alternativeData", report.getAlternativeData());
                response.put("report", reportBody);
            }
        }

        entries.put("entries", response);  
        
        return entries;
    }
    private Map<String, Object> setInquiry(ReportAPIRequest uForm){
        JSONObject inquiry = new JSONObject();
        inquiry.put("aiCode", uForm.getAiCode())
            .put("companyID", Long.parseLong(uForm.getCompanyID()))
            .put("reportType", Integer.parseInt(uForm.getReportType()))
            .put("reasonCode", Integer.parseInt(uForm.getReasonCode()))
            .put("aiRefCode1", uForm.getAiRefCode1())
            .put("aiRefCode2", uForm.getAiRefCode2())
            .put("aiRefCode3", uForm.getAiRefCode3())
            .put("reorderIn7Days", uForm.getReorderIn7Days());        
        return inquiry.toMap();
    }
    
    public void generateReportHtml(HttpServletResponse response, ReportAPIRequest uForm, long expenseId){
        logger.info(">>>>creatReportHTML 1<<<<");
        try{
            String dirPath = env.getProperty("html_report_dir_path");

            FileManagerUtil.createDirIfNotExist(dirPath);
            User user = getUserForReport(uForm);
            logger.info(">>>>creatReportHTML 2<<<<");
            File htmlFolder = htmlDao.getHtmlFolder(dirPath, expenseId, user);
            logger.info(">>>>creatReportHTML 3<<<<");  
            
            String urlPath = htmlFolder.getAbsolutePath();
            urlPath = urlPath.replace("%2e", ".");
            urlPath = urlPath.replace("%2f", "/");
            urlPath = urlPath.replace("%5c", "/");

            if(urlPath != null && urlPath.contains(".."))
            {
                throw new Exception("path file not support.");
            }
            
            zipToResponse(response, htmlFolder);
            
            FileManagerUtil.deleteDirectory(htmlFolder);
        }
        catch(Exception e){
            logger.error(e.getMessage(),e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public void zipErrorResponseNclean(HttpServletResponse response, ResponseModel error) throws Exception{
        String dirPath = env.getProperty("html_report_dir_path");        
        String errorDirPath = dirPath + File.separator + "error" + System.nanoTime();

        String urlPath = errorDirPath;

        urlPath = urlPath.replace("%2e", ".");
        urlPath = urlPath.replace("%2f", "/");
        urlPath = urlPath.replace("%5c", "/");

        if(urlPath.contains(".."))
        {
            throw new Exception("Path not support.");
        }

        File errorDir = FileManagerUtil.createDirIfNotExist(errorDirPath);
        
        String jsonPath = errorDir + File.separator + "response.json";
        loggerText = String.format("jsonPath:  %s",jsonPath);
        logger.info(loggerText);
        try (Writer writer = new FileWriter(jsonPath)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(error, writer);
        }
        
        zipToResponse(response, errorDir);
        
        FileManagerUtil.deleteDirectory(errorDir);
    }
//    private void zipToResponse(HttpServletResponse response, File dir) throws Exception{        
//        LocalDateTime ldt = LocalDateTime.now();
//        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS", Locale.ENGLISH);
//        String zipName = formmat1.format(ldt) +".zip";
//        
//        response.setContentType("application/zip");
//        response.setHeader("Content-Disposition", "attachment; filename=" + zipName); 
//        ZipUtil.pack(
//            dir,
//            response.getOutputStream()
//        );
//    }
    
    private void zipToResponse(HttpServletResponse response, File dir) throws Exception {
    LocalDateTime ldt = LocalDateTime.now();
    DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS", Locale.ENGLISH);
    String zipName = formmat1.format(ldt) + ".zip";

    response.setContentType("application/zip");
    response.setHeader("Content-Disposition", "attachment; filename=" + zipName);

    ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
    zipDirectory(dir, zipStream, "");
    zipStream.close();
}
private void zipDirectory(File directory, ZipOutputStream zipStream, String parent) throws Exception {
    byte[] buffer = new byte[1024];
    for (File file : directory.listFiles()) {
        if (file.isDirectory()) {
            StringBuilder s = new StringBuilder();
            s.append(parent);
            s.append(file.getName());
            s.append("/");
            zipDirectory(file, zipStream, s.toString());
            continue;
        }
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        String entryName = parent + file.getName();
        ZipEntry entry = new ZipEntry(entryName);
        zipStream.putNextEntry(entry);
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            zipStream.write(buffer, 0, bytesRead);
        }
        bis.close();
        fis.close();
    }
}
    private User getUserForReport(ReportAPIRequest uForm){
        User user = userDAO.getUserFromUserID(uForm.getUserID());
        user.setComapnyID(Long.parseLong(uForm.getCompanyID()));
        return user;
    }
    private ReportCreatorData buildReportCreatorData(ReportAPIRequest uForm, User user){
        ReportCreatorData dat = new ReportCreatorData();
        dat.setRequestType(REQUESTTYPE.API);
        dat.setMatchKey(uForm.getMatchTranId());
        dat.setUID(user.getuID());
        dat.setAIID(user.getAIID());
        dat.setObjectID(
            switch(Integer.parseInt(uForm.getReportType())){
                case 1 -> OBJECT_TYPE.BASIC_REPORT;
                case 2 -> OBJECT_TYPE.CHINESE_REPORT;
                default -> OBJECT_TYPE.SEARCH_API;
            }
        );
        
        Map<String, Object> lookupInfo = searchDao.getLookupInfoOfUser(uForm.getUserID(), uForm.getCompanyID());
        dat.setUID((Integer)lookupInfo.get("UID"));
        dat.setAIID((Integer)lookupInfo.get("AIID"));
        dat.setGroupID((Integer)lookupInfo.get("GroupID"));
        dat.setCBUID((Integer)lookupInfo.get("CBUID"));
        dat.setCompanyName((String)lookupInfo.get("CompanyName"));
        
        dat.setTransID(
            transactionAPIDAO.saveTransactionAPIReport(
                dat.getUID(),//check the parameters to see which value must be set before calling this function
                dat.getGroupID(),
                dat.getAIID(),
                dat.getObjectID(),
                uForm.getTokenId()
            )
        );
        
        return dat;
    }
    
    protected Map<String, Object> getReportBody(ReportAPIRequest uForm,TrnJson trnJsonObjResponse) throws GenericException, Exception
    {
        User user = getUserForReport(uForm);
        logger.info("Tac.......1");
        ReportCreatorData dat = buildReportCreatorData(uForm, user);
        logger.info("Tac.......2");
        int amountPastDueInYears = validateRule.findAmountPastDueInYears(dat.getCBUID());  
        logger.info("Tac.......3");
        int minorVersion = switch(amountPastDueInYears){
            case 2 -> 2;
            case 5 -> 1;
            default -> 0;
        };
        logger.info("Tac.......4");
        aiOrder.setCacheAIOrders(dat.getCBUID(), amountPastDueInYears);   

        logger.info("Tac.......5");
        Date requestTime = trnJsonObjResponse.getRequestTime();
        Date responseTime = trnJsonObjResponse.getResponseTime();
        logger.info("RequestTime:::"+requestTime);
        logger.info("ResponseTime:::"+responseTime);
        dat.setExpenseID(
            expenseDAO.saveReportExpense(
                dat.getUID(),//check the parameters to see which value must be set before calling this function
                dat.getGroupID(),
                dat.getAIID(),
                dat.getCBUID(),
                dat.getObjectID(),
                uForm.getTokenId(),
                requestTime,
                responseTime,
                aiOrder
            )
        );
        
        
        insertMalProductDeliverAPI(uForm, user, dat, minorVersion);
        
        ReportBody report = reportCreatorDao.createReport(dat, user, amountPastDueInYears, minorVersion, aiOrder);     
        
        insertSuspectedAbnormal(reportCreatorDao, uForm, dat,responseTime);
        
        Map<String, Object> ret = new HashMap<>();
        ret.put("report", createReportJson(uForm, report));
        ret.put("expenseId", dat.getExpenseID());//for restcontroller, save expenseid outside this service...............yeah
        
        return ret;
    }
    private void insertMalProductDeliverAPI(ReportAPIRequest uForm, User user, ReportCreatorData dat, int minorVersion){
        Map<String, Object> header = new HashMap<>();
        header.put("reasonCode", Integer.parseInt(uForm.getReasonCode()));
        header.put("aiRefCode1", uForm.getAiRefCode1());
        header.put("aiRefCode2", uForm.getAiRefCode2());
        header.put("aiRefCode3", uForm.getAiRefCode3());
        header.put("companyName", dat.getCompanyName());
        header.put("minorVersion", minorVersion);
        malDao.saveMalProdAPI(user, dat, header, uForm.getReorderIn7Days());
    }
    private void insertSuspectedAbnormal(ReportCreatorDAO reportCreatorDao, ReportAPIRequest uForm, ReportCreatorData dat,Date responseTime) throws GenericException{
        int objectID = switch(dat.getObjectID()){
            case OBJECT_TYPE.CHINESE_REPORT -> OBJECT_TYPE.CHINESE_REPORT_API;
            case OBJECT_TYPE.BASIC_REPORT -> OBJECT_TYPE.BASIC_REPORT_API;
            default -> 0;
        };
        Long expenseID = dat.getExpenseID();
        Integer uID = dat.getUID();
        Integer uGroupAIID = dat.getAIID();
        Integer uGroupID = dat.getGroupID();
        Integer cbuid = dat.getCBUID();
        reportCreatorDao.insertSuspectedAbnormal(uForm.getTokenId().intValue(), objectID, uID, uGroupID,
            uGroupAIID, cbuid, expenseID, responseTime);
    }
}
