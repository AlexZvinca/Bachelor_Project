package com.example.project_backend.dto;


import com.example.project_backend.entities.TransportAuthorizationRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransportAuthorizationRequestDTO(
          String userId,
          String county,
          String licensePlateNumber,
          String description,
          Double volume,
          LocalDate fromDate,
          LocalDate untilDate){
}