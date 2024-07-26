package com.hive5.conectivitysecurity.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hive5.conectivitysecurity.config.RSAKeyRecord;
import com.hive5.conectivitysecurity.services.OAuth2TokenGenerator;

@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private OAuth2TokenGenerator tokenServices;

    private final RSAKeyRecord rsaKeyRecord;

    public LoginController(RSAKeyRecord rsaKeyRecord) {
        this.rsaKeyRecord = rsaKeyRecord;
    }

    @CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<String> loginResponse(@RequestHeader("Authorization") String authorization, 
        @Value("${jwt.rsa-public-key}")String publicKey) {
                
        //Generate OauthToken
        String oauthToken = tokenServices.generateToken(authorization);
        System.out.println("-------TOKEN-------");
        System.out.println(oauthToken);
        System.out.println("-------TOKEN-------");

        System.out.println("public key: " + Base64.getEncoder().encodeToString(rsaKeyRecord.rsaPublicKey().getEncoded()));
        //String publicKeyString = Base64.getEncoder().encodeToString(rsaKeyRecord.rsaPublicKey().getEncoded());

        //Add oauthToken & public Key to headers
        HttpHeaders headers = new HttpHeaders();


        headers.add("Set-Cookie", "oauthToken=" + oauthToken + "; SameSite=None; Secure");
        //headers.add("Set-Cookie", "publicKey=" + publicKeyString);
        System.out.println("HEADERS----" + headers.toString());

        ResponseEntity<String> response = new ResponseEntity<>("Login correcto, token oauth2 generado", headers, HttpStatus.OK);
        
        return response;
    }

    
}
