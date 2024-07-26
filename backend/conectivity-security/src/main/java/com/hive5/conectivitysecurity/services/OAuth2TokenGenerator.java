package com.hive5.conectivitysecurity.services;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

@Service
public class OAuth2TokenGenerator {
   
    @Autowired
    private DefaultTokenServices tokenServices;

    public String generateToken(String authorization) {

        String[] authParts = authorization.split(" ");
        String credentials = authParts[1];
        System.out.println("------Credentials: " + credentials.toString());

        byte[] decodedBytes = Base64.getDecoder().decode(credentials);
        String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
        System.out.println("-------Decoded credentials: " + decodedCredentials.toString());

        String[] credParts = decodedCredentials.split(":");
        String username = credParts[0];
        String password = credParts[1];

        System.out.println("------- Usuario: " + username + "------ contraseña: " + password + "--------");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        OAuth2Request request = new OAuth2Request(null, username, null, false, null, null, null, null, null);
        OAuth2Authentication authentication = new OAuth2Authentication(request, authToken);

        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) tokenServices.createAccessToken(authentication);
        token.setExpiration(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiration time

        return token.getValue();
    }
}
