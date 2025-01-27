package com.arg.ccra3.dao.test.repo;

import com.arg.ccra3.dao.repo.ViewApiUserRepositories;
import com.arg.ccra3.model.security.ViewApiUser;
import java.sql.Timestamp;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("jparepo")
@SpringBootTest
public class ViewApiUserRepoTest {

    private static final Logger logger = LogManager.getLogger(ResultsRepoTest.class);

    //@Autowired private EntityManager entityManager;
    @Autowired private ViewApiUserRepositories viewUser;

    @Test
    void injectedComponentsAreNotNull(){
        //assertThat(entityManager).isNotNull();
        assertThat(viewUser).isNotNull();
    }

    /*@Test
    void searchTest(){
        List<ViewApiUser> userList = viewUser.getUserByAICode("P1441");
        ViewApiUser user = userList.get(0);
        assertEquals(user.getUserID(), "Admin@P1441");
    }*/

    
}
