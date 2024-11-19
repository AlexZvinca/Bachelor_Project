package com.example.project_backend.service;

import com.example.project_backend.entities.TransportAuthorizationRequest;
import com.example.project_backend.repository.TransportAuthorizationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransportAuthorizationRequestService {

    private final TransportAuthorizationRequestRepository transportAuthorizationRequestRepository;

    @Autowired
    public TransportAuthorizationRequestService(TransportAuthorizationRequestRepository transportAuthorizationRequestRepository) {
        this.transportAuthorizationRequestRepository = transportAuthorizationRequestRepository;
    }

    public void addAuthRequest(TransportAuthorizationRequest transportAuthorizationRequest) {

        transportAuthorizationRequestRepository.save(transportAuthorizationRequest);
    }
}
