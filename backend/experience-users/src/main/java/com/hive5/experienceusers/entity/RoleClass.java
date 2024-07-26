package com.hive5.experienceusers.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleClass {
    public record Role(int id, String name) {   }
    public record RoleType(int id, String RoleName) {   }
}

