package com.hive5.experience_boards.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

  public record Role(int id, String name) {}

  public record User(int id, String firstName, String lastName, String email, String password, Role role) {}
  
  public record Status(int id, String name) {}

  public record Card(int id, String title, String description, LocalDate startDate, LocalDate endDate,int priority, Status status){}

  public record Board(int id, String name, List<User> users, List<Card> cards) {}


  public record ResponseGetAll(List<Board> boards) {}


  public record BoardAll(List<Board> board) {
  }

  public record ResponseDSList(List<BoardAll> boards) {
  }

  public record Embedded(List<Board> boards) {
  }

  public record CardsResponse(Embedded _embedded) {
  }

  public record ResponseDS(Board board, String response) {
  }

}
