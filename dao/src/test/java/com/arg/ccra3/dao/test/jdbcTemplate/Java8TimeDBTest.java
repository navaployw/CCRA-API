package com.arg.ccra3.dao.test.jdbcTemplate;

import com.arg.ccra3.model.dummy.TEST_TABLE_DONT_USE;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

@Tag("jdbc")
@SpringBootTest
public class Java8TimeDBTest {
    //https://stackoverflow.com/questions/17916666/date-vs-timestamp-vs-calendar
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /*
        can't convert java.sql.Timestamp to newer time framwork
        can't convert java.sql.Timestamp to newer time framwork
        can't convert java.sql.Timestamp to newer time framwork
        can't convert java.sql.Timestamp to newer time framwork
        can't convert java.sql.Timestamp to newer time framwork
    */
    
    
    /*@Test
    void testInstant(){
        Instant time = Instant.now();
        System.out.println("testInstant: " + time);
        
        List<TEST_TABLE_DONT_USE> t = jdbcTemplate.query(
            "select * from TEST_TABLE_DONT_USE",
            BeanPropertyRowMapper.newInstance(TEST_TABLE_DONT_USE.class)
        );
        
        assertEquals(time, t.get(0).getInstant());
    }
    
    @Test
    void testZonedDateTime(){
        ZonedDateTime zdt = ZonedDateTime.now(); 
        
        List<TEST_TABLE_DONT_USE> t = jdbcTemplate.query(
            "select * from TEST_TABLE_DONT_USE",
            BeanPropertyRowMapper.newInstance(TEST_TABLE_DONT_USE.class)
        );
        
        assertEquals(zdt, t.get(0).getZonedDateTime());
    }
    
    @Test
    void testLocalDate(){
        LocalDate date = LocalDate.now();
        System.out.println("testLocalDate: " + date);
        
        List<TEST_TABLE_DONT_USE> t = jdbcTemplate.query(
            "select * from TEST_TABLE_DONT_USE",
            BeanPropertyRowMapper.newInstance(TEST_TABLE_DONT_USE.class)
        );
        
        assertEquals(date, t.get(0).getLocaldate());
    }*/
    
    /*@Test
    void testInstant(){
        Instant time = Instant.now();
        System.out.println("testInstant: " + time);
        
        jdbcTemplate.update(
            "insert into TEST_TABLE_DONT_USE(instant) values(?)",
            new Object[]{time}
        );
        
        List<TEST_TABLE_DONT_USE> t = jdbcTemplate.query(
            "select * from TEST_TABLE_DONT_USE",
            BeanPropertyRowMapper.newInstance(TEST_TABLE_DONT_USE.class)
        );
        
        assertEquals(time, t.get(0).getInstant());
    }
    
    @Test
    void testZonedDateTime(){
        ZonedDateTime zdt = ZonedDateTime.now();        
        jdbcTemplate.update(
            "insert into TEST_TABLE_DONT_USE(zonedDateTime) values(?)",
            new Object[]{zdt}
        );
        
        List<TEST_TABLE_DONT_USE> t = jdbcTemplate.query(
            "select * from TEST_TABLE_DONT_USE",
            BeanPropertyRowMapper.newInstance(TEST_TABLE_DONT_USE.class)
        );
        
        assertEquals(zdt, t.get(0).getZonedDateTime());
    }
    
    @Test
    void testLocalDate(){
        LocalDate date = LocalDate.now();
        System.out.println("testLocalDate: " + date);
        
        jdbcTemplate.update(
            "insert into TEST_TABLE_DONT_USE(localdate) values(?)",
            new Object[]{date}
        );
        
        List<TEST_TABLE_DONT_USE> t = jdbcTemplate.query(
            "select * from TEST_TABLE_DONT_USE",
            BeanPropertyRowMapper.newInstance(TEST_TABLE_DONT_USE.class)
        );
        
        assertEquals(date, t.get(0).getLocaldate());
    }*/
    
    
}
