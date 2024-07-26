package com.hive5.conectivitysecurity.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        theUserDetailsManager.setUsersByUsernameQuery("select email, password, 'true' from user where email=?");

        theUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select id, name from role where id = (select role_id from user where email=?)");

        return theUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.POST, "/login").hasAnyRole("DEVELOPER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/getUsuario").permitAll()
                .requestMatchers(HttpMethod.GET, "/getToken").permitAll() 
                .requestMatchers(HttpMethod.GET, "/generateToken").permitAll() 
                .requestMatchers(HttpMethod.GET, "/regenerateToken").permitAll()
                .requestMatchers(HttpMethod.GET, "/validateToken").permitAll());

        http.httpBasic(Customizer.withDefaults());

        http.exceptionHandling((exceptions) -> exceptions
            .authenticationEntryPoint(new NoWWWAuthenticateEntryPoint()));

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}


class NoWWWAuthenticateEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Bearer");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        String message = "Login error";
        response.getWriter().write(message);
    }
}