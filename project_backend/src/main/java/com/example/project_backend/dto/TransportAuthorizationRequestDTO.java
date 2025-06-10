package com.example.project_backend.dto;

import java.time.LocalDate;

public record TransportAuthorizationRequestDTO(
          String userId,
          String county,
          String licensePlateNumber,
          String description,
          Double volume,
          LocalDate fromDate,
          LocalDate untilDate){
}