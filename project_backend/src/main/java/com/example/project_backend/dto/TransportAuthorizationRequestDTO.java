package com.example.project_backend.dto;


import com.example.project_backend.entities.TransportAuthorizationRequest;

import java.time.LocalDateTime;

public record TransportAuthorizationRequestDTO(String userId,
          String county,
          String idCopy,
          String licensePlateNumber,
          String vehicleIdentification,
          String description ){
}