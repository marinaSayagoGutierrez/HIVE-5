package com.hive5.experienceusers.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hive5.experienceusers.entity.RoleClass.Role;
import com.hive5.experienceusers.entity.RoleClass.RoleType;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * User
 */
public class UserClass {

    public record User(int id,
                   String email,
                   String firstName,
                   String lastName,
                   String username,
                   Role role
) { }

    public record UserPassword(int id,
        String email,
        String firstName,
        String lastName,
        String username,
        RoleType role,
        String password
        ) { }
}