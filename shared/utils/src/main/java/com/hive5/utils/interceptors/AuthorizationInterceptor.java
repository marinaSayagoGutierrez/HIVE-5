package com.hive5.utils.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final RestTemplate restTemplate;

    @Autowired
    public AuthorizationInterceptor(RestTemplate restTemplate) {
        System.out.println("AuthorizationInterceptor created 2");
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Intercepto "+ request.getRequestURI());
        
        String header = request.getHeader("Authorization");
        
        if (header == null || header.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", header);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:8091/validateToken",
                HttpMethod.GET,
                entity,
                String.class
            );
            
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                
                return true;
            } 
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            } else {
                // Log the specific unauthorized request
                System.err.println("Unauthorized request: " + request.getMethod() + " " + request.getRequestURI());
                e.printStackTrace();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        } catch (HttpServerErrorException e) {
            // Log the server error details
            System.err.println("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        } catch (Exception e) {
            // Log all other exceptions
            System.err.println("Exception occurred while processing request: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return false;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

    

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
     
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       
    }

}
