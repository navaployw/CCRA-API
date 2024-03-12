package com.arg.ccra3.dao;

import static com.arg.ccra3.dao.util.InquiryUtilities.*;
import static com.arg.ccra3.dao.util.ReportResource.*;
import com.arg.ccra3.models.inquiry.data.Demographic;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class DemographicDAO {
    private static final Logger logger = LogManager.getLogger(DemographicDAO.class);
    private final JdbcTemplate jdbcTemplate;
    public DemographicDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Demographic[] getProfileDemographicList(Integer CBUID, int years)
        
    {
        return getDemographicList("reportcreator.preparedstatement.demographic", CBUID, years);
    }
    
    public Demographic[] getAllDemographicList(Integer CBUID, int years)
        
    {
        return getDemographicList("reportcreator.preparedstatement.demographic.all", CBUID, years);
    }
    
    private Demographic[] getDemographicList(String query, Integer CBUID, int years)
     
    {
        List<Demographic> ret = new LinkedList<>();
        jdbcTemplate.query(
            getString(query),
            (ResultSet rst) -> { 
                do{
                    Demographic data = new Demographic();
                    data.setAddressEN1(rst.getString("address_1"));
                    data.setAddressEN2(rst.getString("address_2"));
                    data.setAddressEN3(rst.getString("address_3"));
                    data.setAddressLO1(rst.getString("address_lo_1"));
                    data.setAddressLO2(rst.getString("address_lo_2"));
                    data.setAddressLO3(rst.getString("address_lo_3"));
                    data.setAreaCode(rst.getString("area_code"));
                    data.setBRCNO(rst.getString("brc_no"));
                    data.setCBUID(CBUID);
                    data.setCINO(rst.getString("ci_no"));
                    data.setCityEN(rst.getString("city"));
                    data.setCityLO(rst.getString("city_lo"));
                    data.setCompanyType(rst.getInt("com_type"));
                    data.setCountry(rst.getString("country"));
                    data.setCountryCode(rst.getString("country_code"));
                    data.setCreatedBy(rst.getInt("createdby"));
                    data.setCreatedDate(rst.getDate("createddate"));
                    data.setCustomerNO(rst.getString("customer_no"));
                    data.setDunsNO(rst.getString("dunsno"));
                    data.setFlagResend(rst.getBoolean("flagresend"));

                    int gid = rst.getInt("groupid");
                    data.setGroupID((rst.wasNull() ? null : gid));

                    data.setNameEN(rst.getString("name_en"));
                    data.setNameLO(rst.getString("name_lo"));
                    data.setOtherRegNO(rst.getString("other_reg_no"));
                    data.setPeriod(rst.getString("period"));
                    data.setPhoneNO(rst.getString("phone_no"));
                    data.setPlaceOfReg(rst.getString("place_of_reg"));
                    data.setPostCode(rst.getString("post_code"));
                    data.setPrevNameEN(rst.getString("prev_name_en"));
                    data.setPrevNameLO(rst.getString("prev_name_lo"));
                    data.setProvinceEN(rst.getString("province"));
                    data.setProvinceLO(rst.getString("province_lo"));
                    data.setRegistrationDataID(rst.getInt("registrationdataid"));
                    data.setRevocationEffectedDate(rst.getDate("revoc_effect_date"));
                    data.setRevocationNoticedDate(rst.getDate("revoc_noticed_date"));
                    data.setRevocationUpdated(rst.getBoolean("revoc_update"));
                    data.setSharedHolderNameEN(rst.getString("shareholder_name_en"));
                    data.setSharedHolderNameLO(rst.getString("shareholder_name_lo"));
                    data.setTradeNameEN(rst.getString("trade_name_en"));
                    data.setTradeNameLO(rst.getString("trade_name_lo"));
                    data.setUpdatedBy(rst.getInt("updatedby"));
                    data.setUpdatedDate(rst.getDate("updateddate"));
                    data.setWithdrawRevocation(rst.getString("withdraw_revoc"));

                    ret.add(data);
                }while (rst.next());
            },
            new Object[]{CBUID, getPeriod(years)}
        );
        logger.info(query + ": " + ret.size());
        return ret.toArray(Demographic[]::new);
    }
}
