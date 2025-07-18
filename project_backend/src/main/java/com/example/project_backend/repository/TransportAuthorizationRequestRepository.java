package com.example.project_backend.repository;

import com.example.project_backend.entities.County;
import com.example.project_backend.entities.Status;
import com.example.project_backend.entities.TransportAuthorizationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransportAuthorizationRequestRepository extends JpaRepository<TransportAuthorizationRequest, Integer> {
    List<TransportAuthorizationRequest> findAllByUserId(String userId);
    List<TransportAuthorizationRequest> findAllByCounty(County county);
    List<TransportAuthorizationRequest> findByLicensePlateNumber(String licensePlateNumber);
    List<TransportAuthorizationRequest> findByStatusAndUntilDateBefore(Status status, LocalDate untilDate);

}
