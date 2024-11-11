package com.example.project_backend.repository;

import com.example.project_backend.entities.AuthorizationRequest;
import com.example.project_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRequestRepository extends JpaRepository<AuthorizationRequest, String> {

}
