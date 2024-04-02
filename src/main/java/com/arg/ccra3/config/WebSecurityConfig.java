package com.arg.ccra3.config;


import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

/**
*
* @author sittichaim
* 
*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
//
//	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//	private final JwtRequestFilter jwtRequestFilter;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        // We don't need CSRF for this example
        httpSecurity
        // don't authenticate this particular request
        .authorizeHttpRequests()
        
         .requestMatchers("/get_ccra_report").permitAll()
         .requestMatchers("/get_ccra_report/html").permitAll()
        // .requestMatchers("/api/admin/checkLogin"
        //                             ,"/api/admin/logout"
        //                             ,"/api/admin/getVersionNo"
        // ).permitAll()
        .requestMatchers("/ccraapiauth/**").authenticated() 
        .requestMatchers("/v3/**").permitAll()
        .requestMatchers(
                                    "/*"
                                    ,"/error"
                                    ,"/index.html"
                                    , "/assets/**"
                                    , "/main.*"
                                    , "/polyfills.*"
                                    , "/runtime.*"
                                    , "/styles.*"
                                    , "/favicon.ico"
                                    ).permitAll()
        .requestMatchers("/swagger-ui.html").permitAll()
        .requestMatchers("/swagger-ui/**").permitAll()
        .requestMatchers("/api-docs/**").permitAll()
        
        // all other requests need to be authenticated
        .anyRequest()
        .authenticated()
        .and()
        // make sure we use stateless session; session won't be used to
        // store user's state.
//        .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
//        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        httpSecurity.cors().and().csrf().disable();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS", "PUT", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        
        return httpSecurity.build();
    }
    
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    CorsConfiguration config = new CorsConfiguration();
	    source.registerCorsConfiguration("/**", config.applyPermitDefaultValues());
	    config.addAllowedMethod("PUT");
	    // config.addAllowedMethod("POST");
	    //allow Authorization to be exposed
	    config.setExposedHeaders(Arrays.asList("Authorization"));

	    return source;
	}


}
