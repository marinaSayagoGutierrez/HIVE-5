package com.hive5.experiencecards.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.hive5.experiencecards.entities.Response.ResponseOSBCreate;
import com.hive5.experiencecards.entities.Response.ResponseOSBUpdate;
import com.hive5.experiencecards.entities.Response.ResponseOSBGet;
import com.hive5.experiencecards.entities.Response.ResponseOSBGetAll;



@RestController
public class CardController {

    private final RestTemplate restTemplate;
    private final String urlOSB = "http://localhost:7101/OSB_CARDS/";

    @Autowired
    public CardController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/cards")
    public ResponseEntity<Object> getCards() {
        
        String urlOSB = this.urlOSB + "GetAllCard_Experience";

        try {
            ResponseEntity<ResponseOSBGetAll> response = restTemplate.getForEntity(urlOSB, ResponseOSBGetAll.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            switch (statusCode) {
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                default:
                    return ResponseEntity.status(statusCode).body(null);
            }
        } catch (HttpServerErrorException e) {
            // Log the server error details
            System.err.println("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            // Log all other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/cards/{id}")
    public ResponseEntity<ResponseOSBGet> getCardById(@PathVariable("id") String id) {
        String urlOSB = this.urlOSB + "GetCard_Experience/?cardId=" + id;

        try {
            ResponseEntity<ResponseOSBGet> user = restTemplate.getForEntity(urlOSB, ResponseOSBGet.class);

            return user;
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            switch (statusCode) {
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                default:
                    return ResponseEntity.status(statusCode).body(null);
            }
        } catch (HttpServerErrorException e) {
            // Log the server error details
            System.err.println("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            // Log all other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/cards")
    public ResponseEntity<ResponseOSBGet> createCard(@RequestBody ResponseOSBCreate card) {
        String urlOSB = this.urlOSB + "CreateCard_Experience";
        try {
            System.out.println(card);
            ResponseEntity<ResponseOSBGet> response = restTemplate.postForEntity(urlOSB, card, ResponseOSBGet.class);

            System.out.println(response);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            switch (statusCode) {
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                default:
                    return ResponseEntity.status(statusCode).body(null);
            }
        }catch (HttpServerErrorException e) {
            // Log the server error details
            System.err.println("Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            // Log all other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/cards/{id}")
    public ResponseEntity<ResponseOSBGet> updateCard(@RequestBody ResponseOSBUpdate card) {
        String urlOSB = this.urlOSB + "UpdateCard_Experience";
        try {
            System.out.println("UPDATE CARD:" + card);
            ResponseEntity<ResponseOSBGet> response = restTemplate.exchange(urlOSB, HttpMethod.PUT, new HttpEntity<>(card), ResponseOSBGet.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            switch (statusCode) {
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
                default:
                    return ResponseEntity.status(statusCode).body(null);
            }
        }
    }


    @DeleteMapping("/cards/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable("id") String id) {
        String urlOSB = this.urlOSB + "DeleteCard_Experience/?cardId=" + id;
        try {
            restTemplate.delete(urlOSB, String.class);
            return ResponseEntity.ok().body("Deleted successfully");
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            switch (statusCode) {
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Delete failed");
                default:
                    return ResponseEntity.status(statusCode).body("Delete failed");
            }
        }
    }

   
}

