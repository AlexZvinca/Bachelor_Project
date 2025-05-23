package com.example.project_backend.dto;

import com.example.project_backend.entities.County;
import com.example.project_backend.entities.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransportAuthorizationRequestCountyDTO (
        Integer id,
        County county,
        String licensePlateNumber,
        String description,
        Double volume,
        Status status,
        LocalDateTime createdAt,
        LocalDate fromDate,
        LocalDate untilDate,
        String userId){
        }

