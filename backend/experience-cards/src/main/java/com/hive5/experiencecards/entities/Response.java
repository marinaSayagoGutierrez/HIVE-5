package com.hive5.experiencecards.entities;

import java.time.LocalDate;
import java.util.List;

import com.hive5.experiencecards.entities.Rolee.Role;
import com.hive5.experiencecards.entities.Rolee.RoleType;

public class Response {
    public record ResponseOSB(Card card) {}
    public record ResponseOSBErrorHandling(Card card, String response) {}
    public record CardAll(List<Card> card) {}
    public record ResponseOSBList(List<CardAll> cards) {}
    public record Embedded(List<Card> cards) {}
    public record CardsResponse(Embedded _embedded) {}



    public record UserNoCardRoleType(int id, String email, String firstName, String lastName, String username, Role roleType) {}
    public record UserNoCardRole(int id, String email, String firstName, String lastName, String username, RoleType role) {}

    public record UserGetAll(List<UserNoCardRoleType> users) {}

    public record ResponseOSBUser(UserNoCardRole user) {}

    public record ResponseOSBErrorHandlingUser(UserNoCardRole user, String response) {}

    public record ResponseOSBListUser(List<UserNoCardRoleType> userType) {}

    public record ResponseJet(List<User> user) {}


    public record Card(int id, String title, String description, LocalDate startDate, LocalDate endDate, int priority, Board board, Status status, User user) {}

    public record ResponseOSBUpdate(int cardId, String title, String description, LocalDate startDate, LocalDate endDate, int priority, int boardId, int statusId, int userId) {}
    public record ResponseOSBCreate(String title, String description, LocalDate startDate, LocalDate endDate, int priority, int boardId, int statusId, int userId){}
    public record ResponseOSBGet(Card card) {}
    public record ResponseOSBGetAll(List<Card> card) {}
}
