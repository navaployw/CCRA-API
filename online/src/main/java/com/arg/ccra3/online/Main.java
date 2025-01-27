package com.arg.ccra3.online;

import ch.qos.logback.classic.Logger;
import com.arg.ccra3.online.util.ResourceExceptionEntryPoint;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableResourceServer
@SpringBootApplication
@EntityScan("com.arg.ccra3.model")
@EnableJpaRepositories("com.arg.ccra3.dao.repo")
@EnableJpaAuditing//https://stackoverflow.com/questions/40370709/createddate-annotation-does-not-work-with-mysql
public class Main{
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
              logger.error(e.getMessage());  
        }
    }
    
}
