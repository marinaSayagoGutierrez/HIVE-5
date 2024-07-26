package com.hive5.experienceusers.entity;

import java.util.List;

import com.hive5.experienceusers.entity.RoleClass.Role;


public class Response {

    public record BoardsId(List<Integer> boardId) {}

    public record Board(int id, String name) {}
    public record Boards(List<Board> board) {}
    public record User(int id, String email, String firstName, String lastName, Role role, Boards boards, String password) {}

    public record ResponseOSBUpdate(int userId, String email, String firstName, String lastName, String password, int roleId, BoardsId boards){}
    public record ResponseOSBCreate(String email, String firstName, String lastName, String password, int roleId, BoardsId boards){}
    public record ResponseOSBGet(User user){}
    public record ResponseOSBGetAll(List<User> user){}
}
