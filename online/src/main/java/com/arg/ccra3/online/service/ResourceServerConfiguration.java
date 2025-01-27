/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arg.ccra3.online.service;

import ch.qos.logback.classic.Logger;

import com.arg.ccra3.online.util.ResourceExceptionEntryPoint;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 *
 * @author navaployw
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http.exceptionHandling()
//            .authenticationEntryPoint(new ResourceExceptionEntryPoint())
//            .and()    
        http   
            .authorizeRequests()
            .antMatchers("/ccraapiauth/api/admin/**","**/html").permitAll()   
            .antMatchers("/ccraapiauth/get_ccra_report")
            .authenticated();

               
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        // format message
        resources.authenticationEntryPoint(new ResourceExceptionEntryPoint());
        
    }
    
}
