package com.arg.ccra3.dao.test.jdbcTemplate;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

@Disabled
@Tag("jdbc")
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcTemplateTest {
    //https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/jdbc.html
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    
    private static final Logger logger = LogManager.getLogger(JdbcTemplateTest.class);

    @Test
    @Order(0)
    void testSelect(){
        String sql = "select * from Results";

        /*List<Results> results = jdbcTemplate.query(sql,
            BeanPropertyRowMapper.newInstance(Results.class));

        assertThat(results.size(), greaterThanOrEqualTo(0));*/
    }

    @Test
    @Order(1)
    void testInsert(){
        /*String sql = "INSERT INTO Results (KEYID, " +
            "GROUPID, " +
            "NEW_PRIVATEKEY, " +
            "NEW_PUBLICKEY, " +
            "DELETED, " +
            "APPLYSTATUS " +
            " )VALUES (999999, 555555, 'OMFGHELLOHOWAREYOUDOINGLAD', 'WTFLOOLOLOLOLOLOLL', 0,0)";
        jdbcTemplate.update(sql);

        sql = "select * from Results where NEW_PRIVATEKEY = 'OMFGHELLOHOWAREYOUDOINGLAD' and NEW_PUBLICKEY = 'WTFLOOLOLOLOLOLOLL'";

        List<Results> results = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Results.class));
        assertEquals(results.size(), 1);

        Results res = results.get(0);
        assertEquals(res.getNew_privatekey(), "OMFGHELLOHOWAREYOUDOINGLAD");
        assertEquals(res.getNew_publickey(), "WTFLOOLOLOLOLOLOLL");*/
    }
    
    @Test
    @Order(2)
    void test_queryForObject_countInt(){
        String sql = "select count(*) from Results";
        Integer countRow = jdbcTemplate.queryForObject(sql, Integer.class);
        logger.info("test_queryForObject_countInt: " + countRow);
        assertThat(countRow, greaterThanOrEqualTo(2));
        /*Object[] params = new Object[]{999999, 0};
        Integer countRow = jdbcTemplate.queryForObject(sql, Integer.class, params);
        assertThat(countRow, greaterThanOrEqualTo(0));*/
    }
    
    @Test
    @Order(3)
    void test_RowCallbackHandler_ResultSet() {
        //https://mkyong.com/spring/spring-jdbctemplate-handle-large-resultset/
        String sql = "select * from Results where KEYID = ? and DELETED = ?"; 
        Object[] params = new Object[]{999999, 0};
        final List<Map<String, Object>> results = new ArrayList<>();
        
        RowCallbackHandler rowHandler;
        rowHandler = (ResultSet resultSet) -> {
            //https://stackoverflow.com/questions/71824418/how-to-iterate-through-resultset
            do {
                Map <String, Object> rs = new HashMap<>();
                logger.info(rs);
                rs.put("KEYID",resultSet.getInt("KEYID"));
                rs.put("DELETED",resultSet.getInt("DELETED"));
                results.add(rs);
            }while (resultSet.next());
        };
        jdbcTemplate.query(sql, rowHandler, params);
        
        for(var rs : results){
            assertEquals(rs.get("KEYID"), 999999);
            assertEquals(rs.get("DELETED"), false);
            break;
        }
    }
    
    @Test
    @Order(4)
    void testStoreAPIDatasource(){
        String sql = "exec get_customerByDunsNo null, null, ?";
        Object[] params = new Object[]{9990001};
        
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params);
        for(var it : results){
            logger.info("class name: " + it.getClass().getName());
            assertEquals(((BigDecimal)it.get("CBUID")).intValue(), 1323917);
        }
        
        RowCallbackHandler rowHandler = (ResultSet rs) -> {
            do {
                /*CBUID	numeric
                COMPANY_NAME_ENG varchar
                COMPANY_NAME_LO	varchar*/        
                assertEquals(rs.getInt("CBUID"), 1323917);
                break;
            }while (rs.next()) ;
        };
        
        jdbcTemplate.query(sql, rowHandler, params);
    }
    
    @Test
    @Order(5)
    void test_queryForList_Map(){
        String sql = "select * from Results where KEYID = ? and DELETED = ?";
        Object[] params = new Object[]{999999, 0};
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, params);
        for(var it : results){
            logger.info("class name: " + it.getClass().getName());
            assertEquals(it.get("KEYID"), 999999);
            assertEquals(it.get("DELETED"), false);
        }        
    }   

    @Test
    @Order(6)
    void test_RowCallbackHandler_External_Variable() {
        /*final List<Results> results = new LinkedList<>();
        
        String sql = "select * from Results where KEYID = ? and DELETED = ?"; 
        Object[] params = new Object[]{999999, 0};        
        RowCallbackHandler rowHandler = (ResultSet rs) -> {//new RowCallbackHandler.[void processRow(ResultSet){...}]
            do{
                Results temp = new Results();
                temp.setKeyid(rs.getLong("KEYID"));
                temp.setDeleted(rs.getBoolean("DELETED"));
                results.add(temp);
            }while (rs.next());
        };
        
        jdbcTemplate.query(sql, rowHandler, params);
        
        for(var rs : results){
            assertEquals(rs.getKeyid(), 999999);
            assertEquals(rs.getDeleted(), false);
        }*/
    }
    
    @Test
    @Order(7)
    void testSelectAPIDatasource(){
        String sql = "select * from VIEW_API_USER where AID = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, new Object[]{43});
        assertThat(results.get(0).get("SECRETKEY"), equalTo("ZsKDkxQUGNFebdG6645Vj6bZvOOt6Heh"));
    }
    
    @Test
    @Order(99_999)
    void testUpdate(){
        /*String sql = "update Results set GROUPID = 5000000 where NEW_PRIVATEKEY = 'OMFGHELLOHOWAREYOUDOINGLAD' and NEW_PUBLICKEY = 'WTFLOOLOLOLOLOLOLL'";
        jdbcTemplate.update(sql);

        sql = "select * from Results where NEW_PRIVATEKEY = 'OMFGHELLOHOWAREYOUDOINGLAD' and NEW_PUBLICKEY = 'WTFLOOLOLOLOLOLOLL' and GROUPID = 5000000";
        List<Results> results = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Results.class));

        Results res = results.get(0);
        assertEquals(res.getGroupid(), 5000000);
        assertEquals(res.getNew_privatekey(), "OMFGHELLOHOWAREYOUDOINGLAD");
        assertEquals(res.getNew_publickey(), "WTFLOOLOLOLOLOLOLL");*/
    }

    @Test
    @Order(100_000)
    void testDelete(){
        jdbcTemplate.execute("DELETE FROM Results where NEW_PRIVATEKEY = 'OMFGHELLOHOWAREYOUDOINGLAD' and NEW_PUBLICKEY = 'WTFLOOLOLOLOLOLOLL' and GROUPID = 5000000");
        
        Integer countRow = jdbcTemplate.queryForObject(
            "select count(*) from Results where NEW_PRIVATEKEY = ? and NEW_PUBLICKEY = ? and GROUPID = ?", 
            Integer.class, 
            new Object[]{"OMFGHELLOHOWAREYOUDOINGLAD", "WTFLOOLOLOLOLOLOLL", 5000000}
        );
        assertThat(countRow, greaterThanOrEqualTo(0));

        assertEquals(countRow, 0);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*@Disabled("ERROR: use RowMapper or BeanPropertyRowMapper instead")
    void test_queryForList_Class(){
        String sql = "select * from Results where KEYID = ? and DELETED = ?";
        Object[] params = new Object[]{999999, 0};
        jdbcTemplate.queryForList(sql, Results.class, params);
    }
    
    @Disabled("ERROR: use RowMapper or BeanPropertyRowMapper instead")
    void test_queryForList_Object(){
        String sql = "select * from Results where KEYID = ? and DELETED = ?";
        Object[] params = new Object[]{999999, 0};
        List<Object> results = jdbcTemplate.queryForList(sql, Object.class, params);
        assertThat((Results)results.get(0), notNullValue());        
        for(var it : results){
            Results result = (Results)it;
            assertEquals(result.getKeyid(), 999999);
            assertEquals(result.getDeleted(), false);
        }
    }
    
    @Disabled("ERROR: use RowMapper or BeanPropertyRowMapper instead")
    void test_queryForList_ResultSet() throws SQLException{
        String sql = "select * from Results where KEYID = ? and DELETED = ?";
        Object[] params = new Object[]{999999, 0};
        List<ResultSet> results = jdbcTemplate.queryForList(sql, ResultSet.class, params);
        for(var it : results){
            assertEquals(it.getInt("KEYID"), 999999);
            assertEquals(it.getInt(0), 999999);
            assertEquals(it.getBoolean("DELETED"), false);
        }
    }
    
    @Disabled("ERROR: use RowMapper or BeanPropertyRowMapper instead")
    void test_queryForObject(){
        String sql = "select top 1 * from Results where KEYID = ? and DELETED = ?";
        Object[] params = new Object[]{999999, 0};
        Object obj = jdbcTemplate.queryForObject(sql, Object.class, params);
        assertThat(obj, notNullValue());
        Results result = (Results)obj;
        assertEquals(result.getKeyid(), 999999);
        assertEquals(result.getDeleted(), false);
    }
    
    @Disabled("ERROR: use RowMapper or BeanPropertyRowMapper instead")
    void test_queryForObject_Results(){
        String sql = "select top 1 * from Results where KEYID = ? and DELETED = ?";
        Object[] params = new Object[]{999999, 0};
        Results result = jdbcTemplate.queryForObject(sql, Results.class, params);
        assertEquals(result.getKeyid(), 999999);
        assertEquals(result.getDeleted(), false);
    }*/
}