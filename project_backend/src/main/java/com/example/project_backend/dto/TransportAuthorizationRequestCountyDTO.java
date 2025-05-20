package com.example.project_backend.dto;

import com.example.project_backend.entities.County;
import com.example.project_backend.entities.Status;

import java.time.LocalDateTime;

public record TransportAuthorizationRequestCountyDTO (
        Integer id,
        County county,
        String licensePlateNumber,
        String description,
        Status status,
        LocalDateTime createdAt,
        String userId){
        }

