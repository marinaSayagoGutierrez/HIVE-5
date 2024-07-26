package com.hive5.experience_boards.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.hive5.experience_boards.entities.Response.Board;
import com.hive5.experience_boards.entities.Response.ResponseGetAll;


@RestController
public class BoardController {

  private final RestTemplate restTemplate;
  private String urlDS = "http://localhost:82/boards";

  @Autowired
  public BoardController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @GetMapping("/boards")
  public ResponseEntity<ResponseGetAll> getBoards() {
    try {
        ResponseEntity<ResponseGetAll> response = restTemplate.getForEntity(urlDS, ResponseGetAll.class);
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

  @GetMapping("/boards/{id}")
  public ResponseEntity<Board> getBoardById(@PathVariable("id") String id) {
    String urlDS = this.urlDS +"/" + id;

    try {
      ResponseEntity<Board> board = restTemplate.getForEntity(urlDS, Board.class);

      return ResponseEntity.ok(board.getBody());
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
      System.err.println("Server Error: " + e.getStatusCode() + " - " +
          e.getResponseBodyAsString());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    } catch (Exception e) {
      // Log all other exceptions
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/boards")
  public ResponseEntity<Board> createBoard(@RequestBody Board board) {
    System.out.println(board);

    try {
      ResponseEntity<Board> response = restTemplate.postForEntity(urlDS, board,
          Board.class);
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

  @PutMapping("/boards/{id}")
  public ResponseEntity<Board> updateBoard(@PathVariable("id") String id, @RequestBody Board board) {
    try {
      ResponseEntity<Board> response = restTemplate.exchange(urlDS + "/" +
          board.id(), HttpMethod.PUT, new HttpEntity<>(board), Board.class);
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

  @DeleteMapping("/boards/{id}")
  public ResponseEntity<String> deleteBoard(@PathVariable("id") String id) {
    String urlDS = this.urlDS +"/" + id;
    try {
      restTemplate.delete(urlDS, String.class);
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
