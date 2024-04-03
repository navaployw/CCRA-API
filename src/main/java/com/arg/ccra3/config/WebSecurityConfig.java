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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
        // httpSecurity.csrf(AbstractHttpConfigurer::disable)
        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
        // don't authenticate this particular request
        .authorizeHttpRequests(authorize -> authorize                    
                    .requestMatchers("/get_ccra_report"
                    ,"/get_ccra_report/html"
                    ,"/ccraapiauth/**"                     
                    ,"/v3/**"
                    ,"/*"
                    ,"/error"
                    ,"/index.html"
                    , "/assets/**"
                    , "/main.*"
                    , "/polyfills.*"
                    , "/runtime.*"
                    , "/styles.*"
                    , "/favicon.ico"
                    ,"/swagger-ui.html"
                    ,"/swagger-ui/**"
                    ,"/api-docs/**"
            )
            .permitAll()
            .anyRequest().authenticated()  
                      
        )
        .sessionManagement(sessionManagementCustomizer -> sessionManagementCustomizer
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.applyPermitDefaultValues();
            configuration.setAllowedOrigins(Arrays.asList("*"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "lang", "Cache-Control", "Content-Type"));
            configuration.setExposedHeaders(Arrays.asList("Authorization", "lang"));
            return configuration;
        }));
        
        return httpSecurity.build();
    }
    

}
