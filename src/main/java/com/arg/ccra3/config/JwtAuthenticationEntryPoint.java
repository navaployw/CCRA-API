package com.arg.ccra3.config;
import ch.qos.logback.classic.Logger;
import com.arg.ccra3.controllers.CcraapiController;
import java.io.IOException;
import java.io.Serializable;


import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

/**
 * 
 * @author sitthichaim
 *
 */
//@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
private final Logger logger = (Logger) LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	private static final long serialVersionUID = 6328190193958347298L;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
      logger.info("JwtAuthenticationEntryPoint Send Error Unauthorized...... ");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
	
}
