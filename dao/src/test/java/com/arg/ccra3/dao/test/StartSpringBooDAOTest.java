package com.arg.ccra3.dao.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StartSpringBooDAOTest {//mimic main class for springboot
    @SpringBootApplication//can't test SpringBoot without starting SpringBoot
    @EntityScan("com.arg.ccra3.model.*")//different package = can't find entity, must tell springboot where all entities are at
    static class TestConfiguration { }//nth, just a static dummy class for @SpringBootApplication
    /*
    to test database we need
        - application.yml for database url to connect
        - [pom.xml].build.plugins.plugin.maven-surefire-plugin
            - <groups>, <excludedGroups> are optional
            - order of testing? nested class, but not flexible
    */
}
