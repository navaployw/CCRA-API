package com.arg.ccra3.dao.test.repo;

import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("for lib document purpose only, don't enable")
@Tag("jparepo")
@SpringBootTest
public class ResultsRepoTest {

    /*private static final Logger logger = LogManager.getLogger(ResultsRepoTest.class);

    //@Autowired private EntityManager entityManager;
    @Autowired private ResultsRepo resultRepo;

    @Test
    void injectedComponentsAreNotNull(){
        //assertThat(entityManager).isNotNull();
        assertThat(resultRepo).isNotNull();
    }

    @Test
    void saveTest(){
        Results res = new Results();
        res.setKeyid(9999l);
        res.setGroupid(9999l);
        res.setNew_publickey("setNew_publickey");
        res.setNew_privatekey("setNew_privatekey");
        res.setEffective_date(new Timestamp(1651821794));
        res.setDeleted(false);
        res.setApplystatus(false);
        resultRepo.save(res);

        assertThat(resultRepo.findById(9999l)).isNotNull();
    }

    @Test
    void updateTest(){
        Results res = new Results();
        res.setKeyid(9999l);
        res.setGroupid(9999l);
        res.setNew_publickey("setNew_publickeysetNew_publickeysetNew_publickeysetNew_publickey");
        res.setNew_privatekey("setNew_privatekeysetNew_privatekeysetNew_privatekeysetNew_privatekey");
        res.setEffective_date(new Timestamp(1651821794));
        res.setDeleted(false);
        res.setApplystatus(true);
        resultRepo.save(res);

        Results ress = resultRepo.findById(9999l).get();

        assertEquals(ress.getNew_publickey(), "setNew_publickeysetNew_publickeysetNew_publickeysetNew_publickey");
        assertEquals(ress.getNew_privatekey(), "setNew_privatekeysetNew_privatekeysetNew_privatekeysetNew_privatekey");
        assertEquals(ress.getApplystatus(), true);
    }

    @Test
    void deleteTest(){
        resultRepo.deleteById(9999l);

        assertThat(resultRepo.findById(9999l)).trim().isEmpty();//Optional.empty
    }

    @Test
    void testQuery(){
        final long gid = 123456789l;
        final String privateKey = "setNew_privatekey";
        final String publicKey = "setNew_publickey";
        Results res = new Results();
        res.setKeyid(99999912l);
        res.setGroupid(gid);
        res.setNew_privatekey(privateKey);
        res.setNew_publickey(publicKey);
        res.setEffective_date(new Timestamp(1651821794));
        res.setDeleted(false);
        res.setApplystatus(false);
        resultRepo.save(res);

        Results ress = resultRepo.FindByNewPrivateKeyAndNewPublicKey(privateKey, publicKey).get(0);
        logger.info("FindByNewPrivateKeyAndNewPublicKey" + ress);
        assertThat(ress.getNew_privatekey(), containsStringIgnoringCase(privateKey));
        assertThat(ress.getNew_publickey(), containsStringIgnoringCase(publicKey));
        ress = null;
        logger.info("FindByNewPrivateKeyAndNewPublicKey null" + ress);

        ress = resultRepo.findByGroupId(gid).get(0);
        logger.info("findByGroupId: " + ress);
        assertThat(ress.getGroupid(), is(gid));
        ress = null;
        logger.info("findByGroupId null: " + ress);

        ress = resultRepo.findByGroupIdFromObj(res).get(0);
        logger.info("findByGroupIdFromObj" + ress);
        assertThat(ress.getGroupid(), is(gid));

        resultRepo.deleteById(99999912l);
        assertThat(resultRepo.findById(99999912l)).trim().isEmpty();
        //assertThat().isNotEmpty();//Optional.empty

        //resultRepo.deleteByNewPrivateKey(key);

        //assertThat(resultRepo.FindByNewPrivateKey(key)).trim().isEmpty();//Optional.empty
    }*/
}
