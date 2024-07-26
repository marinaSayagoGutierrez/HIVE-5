package com.hive5.experienceusers.contoller;

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


import com.hive5.experienceusers.entity.Response.ResponseOSBCreate;
import com.hive5.experienceusers.entity.Response.ResponseOSBGet;
import com.hive5.experienceusers.entity.Response.ResponseOSBGetAll;
import com.hive5.experienceusers.entity.Response.ResponseOSBUpdate;

@RestController

public class UserController {

    private final RestTemplate restTemplate;
    private String urlOSB = "http://localhost:7101/OSB_USERS/";

    @Autowired
    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    
    @GetMapping("/users")
    public ResponseEntity<ResponseOSBGetAll> getUsers() {
        
        String urlOSB = this.urlOSB + "GetAllUser_Experience";

        try {
            ResponseEntity<ResponseOSBGetAll> response = restTemplate.getForEntity(urlOSB, ResponseOSBGetAll.class);
            return response;
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


    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseOSBGet> getUserById(@PathVariable("id") String id) {
        String urlOSB = this.urlOSB + "GetUser_Experience?userId=" + id;

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

    @PostMapping("/users")
    public ResponseEntity<ResponseOSBGet> createUser(@RequestBody ResponseOSBCreate user) {
        String urlOSB = this.urlOSB + "CreateUser_Experience";

        System.out.println(user);

        try {
            ResponseEntity<ResponseOSBGet> response = restTemplate.postForEntity(urlOSB, user, ResponseOSBGet.class);
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

    @PutMapping("/users/{id}")
    public ResponseEntity<ResponseOSBGet> updateUser(@RequestBody ResponseOSBUpdate user) {
        String urlOSB = this.urlOSB + "UpdateUser_Experience";
        try {
            ResponseEntity<ResponseOSBGet> response = restTemplate.exchange(urlOSB, HttpMethod.PUT, new HttpEntity<>(user), ResponseOSBGet.class);
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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
        String urlOSB= this.urlOSB + "DeleteUser_Experience?userId=" + id;
        try {
            restTemplate.delete(urlOSB);
            return ResponseEntity.ok().body("Deleted successfully");
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            switch (statusCode) {
                case 401:
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Delete failed");
                default:
                    return ResponseEntity.status(statusCode).body("Delete failed");
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

}
