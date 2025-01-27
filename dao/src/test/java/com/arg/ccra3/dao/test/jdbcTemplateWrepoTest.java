package com.arg.ccra3.dao.test;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Disabled("for lib document purpose only, don't enable")
@Tag("jdbcTemplateWrepoTest")
@SpringBootTest
public class jdbcTemplateWrepoTest {
    /*private static final Logger logger = LogManager.getLogger(jdbcTemplateWrepoTest.class);

    @Autowired private ResultsRepo resultRepo;
    private JdbcTemplatePortal jdbcTemplate = new JdbcTemplatePortal();

    @Test
    void testSelectThenUpdate(){
        Results res = new Results();
        res.setKeyid(9999l);
        res.setGroupid(9999l);
        res.setNew_publickey("setNew_publickey");
        res.setNew_privatekey("setNew_privatekey");
        res.setEffective_date(new Timestamp(1651821794));
        res.setDeleted(false);
        res.setApplystatus(false);
        resultRepo.save(res);

        final String sql = "select * from Results where keyid = 9999";
        List<Results> results = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Results.class));

        assertThat(results.size(), lessThanOrEqualTo(1));

        resultRepo.deleteById(9999l);
    }*/
}
