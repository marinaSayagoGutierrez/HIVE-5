package com.hive5.experiencecards.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Card(int id,
                   String title,
                   String description,
                   LocalDate startDate,
                   LocalDate endDate,
                   int priority,
                   Board board,
                   Status status,
                   User user) {}
