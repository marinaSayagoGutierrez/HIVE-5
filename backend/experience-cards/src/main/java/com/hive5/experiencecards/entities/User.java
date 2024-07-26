package com.hive5.experiencecards.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hive5.experiencecards.entities.Rolee.Role;

@JsonIgnoreProperties(ignoreUnknown = true)
public record User(int id, String email, String firstName, String lastName, Role role) {
    
}
