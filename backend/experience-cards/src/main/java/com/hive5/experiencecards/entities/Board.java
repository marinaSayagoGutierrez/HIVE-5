package com.hive5.experiencecards.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Board(int id, String name) {
    
}
