package com.example.project_backend.repository;

import com.example.project_backend.entities.TransportAuthorizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportAuthorizationRequestRepository extends JpaRepository<TransportAuthorizationRequest, String> {
    List<TransportAuthorizationRequest> findAllByUserId(String userId);
}
