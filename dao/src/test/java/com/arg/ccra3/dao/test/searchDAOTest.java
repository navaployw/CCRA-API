/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.dao.test;

import com.arg.cb2.inquiry.data.ReportCreatorData;
import com.arg.ccra3.model.inquiry.data.SearchByNameData;
import com.arg.ccra3.dao.SearchDAO;
import java.util.List;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@Disabled
@Tag("dao")
@SpringBootTest
public class searchDAOTest {

    private SearchDAO search;

    @BeforeEach
    void setUpSearchDAO(){
        search = new SearchDAO(new JdbcTemplate());
    }

    @Test
    @DisplayName("test searchByEnglishName with parameter size and company_anem")
    void test_searchByEnglishName_with_parameters(){
        String comName = "a";//given
        int page = 5;

        List<SearchByNameData> list = search.searchByEnglishName(comName, page);//when

        //assertEquals(true ,5 >= list.size());
        assertThat(list.size(), lessThanOrEqualTo(page));//then
        list.forEach(data ->{
            assertThat(data.getCompany_name_en(), containsStringIgnoringCase(comName));//containsString
        });
    }
    
    @Test
    void test_setLookupInfoOfUser(){
        //given
        ReportCreatorData dat = new ReportCreatorData();
        String userID = "Admin@P1441";        
        String companyId = "9990001";

        //when
        //search.setLookupInfoOfUser(dat, userID, companyId);
        
        //then
        assertEquals(dat.getAIID(), 1154);
        assertEquals(dat.getUID(), 2957);
        assertEquals(dat.getGroupID(), 1154);        
        assertEquals(dat.getCBUID(), 1323917);
        assertEquals(dat.getCompanyName(), "Testing Co Ltd 1");
    }
    

    @Disabled("test_searchByLocalhName_with_parameters: and local_name >= N'?' error?")
    @Test
    @DisplayName("test searchByLocalName with parameter size and company_anem")
    void test_searchByLocalhName_with_parameters(){
        String comName = "隆路版";//given
        int page = 5;

        List<SearchByNameData> list = search.searchByLocalName(comName, page);//when

        //assertEquals(true ,5 >= list.size());
        assertThat(list.size(), lessThanOrEqualTo(page));//then
        list.forEach(data ->{
            assertThat(data.getCompany_name_lo(), containsString(comName));
        });
    }
}
