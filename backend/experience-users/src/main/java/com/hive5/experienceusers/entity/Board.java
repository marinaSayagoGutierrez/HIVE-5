package com.hive5.experienceusers.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Board(int id, String name) {
    
}
