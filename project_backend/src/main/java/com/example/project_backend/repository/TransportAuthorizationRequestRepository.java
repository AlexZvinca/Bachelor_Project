package com.example.project_backend.repository;

import com.example.project_backend.entities.County;
import com.example.project_backend.entities.TransportAuthorizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportAuthorizationRequestRepository extends JpaRepository<TransportAuthorizationRequest, Integer> {
    List<TransportAuthorizationRequest> findAllByUserId(String userId);
    List<TransportAuthorizationRequest> findAllByCounty(County county);

}
