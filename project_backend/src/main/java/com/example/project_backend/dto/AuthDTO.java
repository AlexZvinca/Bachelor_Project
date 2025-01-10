package com.example.project_backend.dto;

import com.example.project_backend.entities.County;
import com.example.project_backend.entities.UserRole;

public record AuthDTO(String userId, String token, UserRole role, County county)
{
}
