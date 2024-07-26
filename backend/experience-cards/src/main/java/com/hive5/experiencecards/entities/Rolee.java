package com.hive5.experiencecards.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Role
 */
public class Rolee {
    public record Role(int id, String name) {   }
    public record RoleType(int id, String RoleName) {   }
}
