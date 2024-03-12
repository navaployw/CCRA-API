package com.arg.ccra3.config;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.arg.ccra3.models.User;
import com.arg.ccra3.models.api.UserAPI;
import com.arg.ccra3.services.AdminService;
import com.arg.ccra3.services.JWTService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
//@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private final JwtTokenUtil jwtTokenUtil;
	private final JWTService jwtService;
	@Autowired
    private AdminService adminService;
	// private final UserRepo userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
                
       logger.info("##################HEADER###############");
       Enumeration<String> values = request.getHeaderNames();
       while (values.hasMoreElements()) {
       		String nextElement = values.nextElement();
            logger.info(MessageFormat.format("Header Name:value [{0}:{1}]", nextElement, request.getHeader(nextElement)));
       }
       logger.info("##################END##################");
       
       logger.info("METHOD :: " + request.getMethod());
       
       if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
           logger.info("TYPE OPTIONS..... do chain.....");
           chain.doFilter(request, response);
       }
       logger.info("NEXT.......");
       
		String aiCode = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			
			jwtToken = requestTokenHeader.substring(7);
			logger.info("JWT TOKEN ..... "+ jwtToken);
			try {
				aiCode = jwtTokenUtil.getUserIdFromToken(jwtToken);
				logger.info("AI Code ID " + aiCode);
			} catch (IllegalArgumentException e) {
				logger.info("Unable to get JWT Token");
		        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is in valid");
			} catch (ExpiredJwtException e) {
				logger.info("********* Token is in valid *********");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is in valid");
			}
			
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		
		// Once we get the token validate it.
		if (aiCode != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	           logger.info("loadUserByUsername...."+ aiCode);
			    
	           User userDetails = new User();// adminService.findByUid(aiCode);
            //    userDetails.setUserID(jwtToken);
			//    Boolean valid = jwtTokenUtil.validateToken(jwtToken, userDetails);  
			    
			// if token is valid configure Spring Security to manually set
			// authentication
			// if (Boolean.TRUE.equals(valid) && null != userDetails) {
				
	        	// response.setHeader("Authorization", "Bearer " + jwtService.generateToken(userDetails.getuID().longValue(), userDetails.getUserID()));

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                                       new UsernamePasswordAuthenticationToken(userDetails, null, null); //add permission 
                               
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			// }
		}
		
		chain.doFilter(request, response);
	}
       
   private void prepareSession(HttpServletRequest request, UserAPI ud){
       HttpSession s = request.getSession();
//           s.setAttribute("profile", ud.getOrgPermissions());
   }

}