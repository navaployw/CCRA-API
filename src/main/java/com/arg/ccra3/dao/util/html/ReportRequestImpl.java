package com.arg.ccra3.dao.util.html;

import com.arg.cb2.inquiry.ErrorMessages;
import com.arg.cb2.inquiry.InquiryConstants;
import com.arg.cb2.inquiry.PreferLanguage;
import com.arg.cb2.inquiry.data.ReportData;
import com.arg.cb2.inquiry.data.ReportHeaderData;
import com.arg.cb2.inquiry.jdbc.util.SQLUtil;
import com.arg.util.GenericException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Base64;

import java.sql.ResultSet;
import org.springframework.jdbc.core.JdbcTemplate;


public class ReportRequestImpl
    extends AbstractReportRequest
{
    Base64.Decoder decoder;
    public ReportRequestImpl(JdbcTemplate jdbcTemplate){
        super(jdbcTemplate);
        decoder = Base64.getDecoder();
    }
    
    
    public ReportHeaderData getReportHeader(final Long expenseID)
        throws Exception
    {
        if (null == expenseID)
        {
            throw new GenericException("INQ-A00008",
                ErrorMessages.getString("INQ-A00008"));
        }

        ReportHeaderData data = new ReportHeaderData();
        
        String sql = "select * from VIEW_GET_RP_HEADER_EX where expenseid = ?";
        
        jdbcTemplate.query(
            sql,
            (ResultSet rs) -> {
                do{
                    data.setAiRefCode1(rs.getString(1));
                    data.setAiRefCode2(rs.getString(2));
                    data.setAiRefCode3(rs.getString(3));
                    data.setReasonCode(SQLUtil.getInteger(rs, 4));
                    data.setObjectID(SQLUtil.getInteger(rs, 5));
                    data.setReportDeliveredDate(rs.getTimestamp(6));

                    data.setCompanyName(rs.getString("companyname"));
                    data.setUserID(rs.getString(8));
                    data.setLoginTime(rs.getTimestamp(9));

                    data.setAIName(rs.getString(10));

                    String tmp = rs.getString(11);
                    data.setAddressEnglish((tmp == null) ? null : tmp.trim());
                    data.setCity(rs.getString(12));
                    data.setProvince(rs.getString(13));
                    data.setCountry(rs.getString(14));
                    data.setCountryCode(rs.getString(15));
                    data.setAreaCode(rs.getString(16));
                    data.setPhoneNo(rs.getString(17));
                    data.setFaxNo(rs.getString(18));
                    data.setPreLang(new PreferLanguage(rs.getString(19)));
                    data.setAIID(rs.getInt(20));
                    data.setExpenseID(expenseID);
                    data.setReportVersion(rs.getInt(21));
                    data.setMinorReportVersion(rs.getInt(22));
                    data.setReportRefNumb(rs.getString(23));
                }while (rs.next());
            },
            expenseID
        );
        return data;
    }

    
    public Serializable getReport(final Long expenseID, final int section)
        throws Exception
    {


        if (null == expenseID)
            throw new GenericException("INQ-A00008",
                ErrorMessages.getString("INQ-A00008"));

        String sql = "select PRODUCTOBJECT from API_PRODUCTDELIVER_DATA where expenseid = ? and objecttype = ? order by objectnumber";
        StringBuffer streams = new StringBuffer();
        jdbcTemplate.query(
            sql,
            (ResultSet rs) -> {
                do{
                    streams.append(rs.getString(1));
                }while (rs.next());
            },
            new Object[]{expenseID, section}
        );
        
        
        java.io.InputStream op = new ByteArrayInputStream(decoder.decode(streams.toString()));
        ObjectInputStream ois = new ObjectInputStream(op);

        // try (ByteArrayInputStream in = new ByteArrayInputStream(decoder.decode(streams.toString()));
        //         ObjectInputStream is = new ObjectInputStream(in)) 
        // {
        //     decodedObj = (Serializable)(new ObjectInputStream(in)).readObject();
        // }
        
        return (Serializable)ois.readObject();


        
    } 
} 
