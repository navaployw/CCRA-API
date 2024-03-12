
package com.arg.ccra3.dao;

import static com.arg.cb2.inquiry.bulk.BulkConstants.*;
import com.arg.cb2.inquiry.data.ReportHeaderData;
import com.arg.cb2.util.XMLUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static com.arg.ccra3.dao.util.ReportCommonDaoUtil.*;
import com.arg.ccra3.dao.util.html.ExportXML;
import com.arg.ccra3.dao.util.html.ReportRequestImpl;
import com.arg.ccra3.models.User;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.jdbc.core.JdbcTemplate;

public class HTMLReportCreatorDAO {
    

    private static final Log log = LogFactory.getLog(HTMLReportCreatorDAO.class);    
    
    private final JdbcTemplate jdbcTemplate;
    
    public HTMLReportCreatorDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;        
    }
     
    public File getHtmlFolder(String dir, long expenseID, User user) throws Exception{
        String htmlDirPath = dir + File.separator + expenseID + "_" + System.nanoTime();
        File htmlDir = new File(htmlDirPath);
        htmlDir.mkdir();
        
        genHtmlFiles(htmlDirPath,expenseID, user);   
        
        return htmlDir;
    }
    
    private void genHtmlFiles(String dir, long expenseID, User user) throws Exception{
        File cssDir = new File(dir + File.separator + CSS_FOLDER);
        cssDir.mkdir();
        File imgDir = new File(dir + File.separator + IMAGE);
        imgDir.mkdir();
        
        inputStreamToFile(
            cssDir.getAbsolutePath() + File.separator + CSS_FILE, 
            this.getClass().getResourceAsStream("/com/arg/cb2/inquiry/css/style.css")
        );
        inputStreamToFile(
            cssDir.getAbsolutePath() + File.separator + CSS_FILE_PRINT, 
            this.getClass().getResourceAsStream("/com/arg/cb2/inquiry/css/styleprint.css")
        );        
        inputStreamToFile(
            imgDir.getAbsolutePath() + File.separator + DNB1, 
            this.getClass().getResourceAsStream("/com/arg/cb2/inquiry/xsl/images/dnb_original.gif")
        );        
        writeHtmlFile(
            dir, 
            getHtml(expenseID, user), 
            expenseID, 
            user.getuID()
        );
    }

    private StringBuffer getHtml(long expenseID, User user) throws Exception{
        ReportRequestImpl reportRequest = new ReportRequestImpl(jdbcTemplate);
        ReportHeaderData rhd = reportRequest.getReportHeader(expenseID);
        log.info(">>>> HTML Report C1 <<<<");
        ExportXML exportXml = new ExportXML(
            getCreditType(jdbcTemplate),
            getReasonCode(jdbcTemplate, rhd.getReasonCode(), user.getPreferLanguage()),
            getTangibleSecurity(jdbcTemplate),
            reportRequest,
            rhd
        );
        
        InputStream isProduct = null;
        StringBuffer htmlBuffer = null;
        
        exportXml.setExpenseID(expenseID);
        log.info(">>>> HTML Report C2 <<<<");
        exportXml.initReport();
        StringBuffer xmlBuffer = exportXml.parseXML();
        log.info(">>>> HTML Report C3 <<<<");
       
        try
        {
            log.info(">>>> HTML Report C4 <<<<");
            if (exportXml.getReportVersion() == 2)
            {
                String htmlFormat;
                htmlFormat = switch (exportXml.getMinorReportVersion()) {
                    case 0 -> "/com/arg/cb2/inquiry/xsl/bulk-request2.xsl";
                    case 1 -> "/com/arg/cb2/inquiry/xsl/bulk-request2-1.xsl";
                    case 2 ->  "/com/arg/cb2/inquiry/xsl/bulk-request2-2.xsl";
                    default -> throw new IllegalArgumentException("Report Version header error: " + exportXml.getReportVersion());
                };
                isProduct = this.getClass().getResourceAsStream(htmlFormat);
                log.info(">>>> HTML Report C4.1 <<<<");
            }
            else
                isProduct = this.getClass().getResourceAsStream("/com/arg/cb2/inquiry/xsl/bulk-request.xsl");

            log.info(">>>> HTML Report C4.2 <<<<");
            log.info(xmlBuffer.toString());
            htmlBuffer = new StringBuffer();
            htmlBuffer.append(XMLUtil.XML2HTML(xmlBuffer.toString(),
                    isProduct));
                    log.info(">>>> HTML Report C4.3 <<<<");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        finally
        {
            if (isProduct != null)
            {
                try
                {
                    isProduct.close();
                }
                catch (IOException x)
                {
                    log.error(x);
                }
                isProduct = null;
            }
        }
        return htmlBuffer;
    }
    private void writeHtmlFile(String dir, StringBuffer htmlBuffer, long expenseID, long uid) throws IOException{
        DownloadOnlineForm dlForm = new DownloadOnlineForm();
        dlForm.setExpenseID(expenseID);
        dlForm.setUID(uid);
        
        String fileName = genHtmlFilePath(dir, dlForm);    
        File htmlFile = new File(fileName);
        
        try (FileWriter fw = new FileWriter(htmlFile, StandardCharsets.UTF_8);
            BufferedWriter writer = new BufferedWriter(fw)) 
        {
            writer.append(htmlBuffer.toString());
        } catch (IOException e) {
            log.error(e);
        }
    }
    private final String genHtmlFilePath(String dir, final DownloadOnlineForm form)
    {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String date = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        long expense_id = form.getExpenseID();
        return dir + File.separator + year + month + date + "-A" + expense_id + "." + HTML_EXTENSION;
    }
    
    private File inputStreamToFile(String filePath, InputStream in) throws IOException{
        File file = new File(filePath);
        try(OutputStream out = new FileOutputStream(file)){
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
        return file;
    }
}

class DownloadOnlineForm
{
    private long UID;    
    private long expenseID;
    public long getExpenseID()
    {
        return expenseID;
    }    
    public long getUID()
    {
        return UID;
    }    
    public void setExpenseID(long expenseID)
    {
        this.expenseID = expenseID;
    }    
    public void setUID(long uid)
    {
        UID = uid;
    }    
}