
package com.arg.ccra3.online.test;

import com.arg.ccra3.dao.ReportCreatorDAO;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Disabled
@SpringBootTest
public class BeanTest {
    @Autowired
    ApplicationContext ctx;
    
    @Test
    public void a(){
        ReportCreatorDAO dao = ctx.getBean(ReportCreatorDAO.class);
        System.out.println(dao);
        ReportCreatorDAO dao1 = ctx.getBean(ReportCreatorDAO.class);
        System.out.println(dao1);
        ReportCreatorDAO dao2 = ctx.getBean(ReportCreatorDAO.class);
        System.out.println(dao2);
        ReportCreatorDAO dao3 = ctx.getBean(ReportCreatorDAO.class);
        System.out.println(dao3);
    }
}
