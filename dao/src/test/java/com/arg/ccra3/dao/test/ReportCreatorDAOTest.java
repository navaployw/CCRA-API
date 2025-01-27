package com.arg.ccra3.dao.test;

import ch.qos.logback.classic.Logger;
import com.arg.cb2.inquiry.data.ReportCreatorData;
import com.arg.ccra3.dao.ReportCreatorDAO;
import com.arg.ccra3.dao.repo.MalProductDeliverDataAPIRepo;
import com.arg.ccra3.model.User;
import com.arg.ccra3.model.report.ReportBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Disabled
@Tag("dao")
@SpringBootTest
public class ReportCreatorDAOTest {
    
    private ReportCreatorDAO dao;
    private final Logger logger = (Logger) LoggerFactory.getLogger(ReportCreatorDAOTest.class);
    @Autowired
    private MalProductDeliverDataAPIRepo malDataRepo;
    
    /*@BeforeEach
    void setUpSearchDAO(){
        dao = new ReportCreatorDAO(new JdbcTemplate(), malDataRepo);
    }
    
    @Test
    void test_searchByEnglishName_with_parameters(){
        ReportCreatorData data = new ReportCreatorData();
        User user = new User();
        user.setAddressEng1("setAddressEng1");
        user.setAddressEng2("setAddressEng2");
        user.setAddressEng3("setAddressEng3");
        user.setCountry("setCountry");
        user.setProvinceEnglish("setProvinceEnglish");
        user.setCityEnglish("setCityEnglish");
        user.setAreaCode("setAreaCode");
                
        data.setAIID(1154);
        data.setCBUID(1323917);
        data.setObjectID(50);
        try{
            ReportBody reportBody = dao.createReport(data, user, 0, 0);
            logger.info("reportBody*-----------------------------");
            logger.info(reportBody.toString());
        }
        catch(Exception e){
            logger.error(e.getMessage());
        }
    }*/
}
