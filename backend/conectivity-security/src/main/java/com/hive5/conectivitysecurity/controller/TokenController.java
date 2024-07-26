package com.hive5.conectivitysecurity.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hive5.conectivitysecurity.config.RSAKeyRecord;
import com.hive5.conectivitysecurity.services.JwtTokenGenerator;
import com.hive5.conectivitysecurity.services.TokenValidatorOAuth2;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
public class TokenController {

  @Autowired
  private JwtTokenGenerator jwtTokenGenerator;

  @Autowired
  private TokenValidatorOAuth2 tokenValidatorOAuth2;

  private final RSAKeyRecord rsaKeyRecord;

  public TokenController(RSAKeyRecord rsaKeyRecord) {
    this.rsaKeyRecord = rsaKeyRecord;
  }

  @CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
  @GetMapping("/getToken")
  public ResponseEntity<Map<String, String>> getToken(@CookieValue("oauthToken") String oauthToken,
      @CookieValue("publicKey") String publicKeyFromRequest) {

    String publicKeyString = Base64.getEncoder().encodeToString(rsaKeyRecord.rsaPublicKey().getEncoded());

    if (oauthToken != null && publicKeyFromRequest != null) {
      if (validatePublicKey(publicKeyString, publicKeyFromRequest) && tokenValidatorOAuth2.validateToken(oauthToken)) {

        String jwtToken = jwtTokenGenerator.generateJwtToken(oauthToken);
        // System.out.println("OAuth: " + oauthToken);
        System.out.println("JwtToken: " + jwtToken);

        // Extraer el ID del usuario del oauthToken
        String user = tokenValidatorOAuth2.extractUserIdFromToken(oauthToken);

        // Token no longer sent through headers
        // HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Bearer " + jwtToken);

        // Token sent through body
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("Authorization", "Bearer " + jwtToken);
        responseBody.put("user", user);

        // Token no longer sent through headers
        // ResponseEntity<String> response = new ResponseEntity<>("JwtToken successfully
        // generated", headers,
        // HttpStatus.OK);

        return ResponseEntity.ok(responseBody);
      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                Map.of("Error", "Error generating JwtToken, invalid or non existent oauth2Token & publicKey provided"));
      }

    } else {
      // Useless, managed in SecurityConfig
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Error generating JwtToken, invalid or non existent oauth2Token & publicKey provided"));
    }
  }

  public boolean validatePublicKey(String publicKey, String publicKeyFromRequest) {
    if (publicKey.equals(publicKeyFromRequest)) {
      return true;
    } else
      return false;
  }

  @CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
  @GetMapping("/regenerateToken")
  public ResponseEntity<Map<String, String>> regenerateToken(@CookieValue("oauthToken") String oauthToken,
      HttpServletResponse response,
      @CookieValue("publicKey") String publicKeyFromRequest) {
    String publicKeyString = Base64.getEncoder().encodeToString(rsaKeyRecord.rsaPublicKey().getEncoded());

    if (oauthToken != null && publicKeyFromRequest != null) {
      if (validatePublicKey(publicKeyString, publicKeyFromRequest) && tokenValidatorOAuth2.validateToken(oauthToken)) {

        String newJwtToken = jwtTokenGenerator.generateJwtToken(oauthToken);
        System.out.println("New JwtToken: " + newJwtToken);

        // Token no longer sent through headers
        // HttpHeaders headers = new HttpHeaders();
        // headers.add("Authorization", "Bearer " + newJwtToken);

        // Token sent through body
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("jwtToken", newJwtToken);

        return ResponseEntity.ok(responseBody);

        // return ResponseEntity.ok().headers(headers).body("New JWT token generated and
        // added to headers");

      } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Error while generating new JWT token"));
      }

    } else {
      // Useless, managed in SecurityConfig
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Error generating JwtToken, invalid or non existent oauth2Token & publicKey provided"));
    }
  }

  @CrossOrigin(origins = "http://localhost:8000", allowCredentials = "true")
  @GetMapping("/validateToken")
  public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authorizationHeader,
      HttpServletResponse response) {

    System.out.println("Authorization Header: " + authorizationHeader);
    if (authorizationHeader != null) {

      String jwtToken = authorizationHeader.replace("Bearer ", "");

      if (jwtTokenGenerator.validateToken(jwtToken)) {
        return ResponseEntity.ok("Valid JwtToken");
      }

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JwtToken");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body("Error, JwtToken non existent");
  }

}
