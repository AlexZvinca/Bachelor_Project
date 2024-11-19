package com.example.project_backend.dto;

import com.example.project_backend.entities.County;

import java.time.LocalDate;

public record UserCreationDTO(String email, String password, String phoneNumber, String firstName, String lastName, LocalDate dateOfBirth, County county, String city, String address, String cnp) {
}
