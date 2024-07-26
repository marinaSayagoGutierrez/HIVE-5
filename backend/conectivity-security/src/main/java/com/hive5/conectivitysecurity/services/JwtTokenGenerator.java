package com.hive5.conectivitysecurity.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hive5.conectivitysecurity.config.RSAKeyRecord;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
@SuppressWarnings("deprecation")
public class JwtTokenGenerator {

    private final RSAKeyRecord rsaKeyRecord;

    @Value("${jwt.tokenExpiration}")
    private long tokenExpiration;

    public JwtTokenGenerator(RSAKeyRecord rsaKeyRecord) {
        this.rsaKeyRecord = rsaKeyRecord;
    }


    public String generateJwtToken(String oauthToken) {    
        String token = Jwts.builder()
                .subject(oauthToken)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.RS256, rsaKeyRecord.rsaPrivateKey())
                .compact();

        return token;
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(rsaKeyRecord.rsaPublicKey())
                .build()
                .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
