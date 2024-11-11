package com.example.project_backend.service;

import com.example.project_backend.entities.AuthorizationRequest;
import com.example.project_backend.repository.AuthRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthRequestService {

    private final AuthRequestRepository authRequestRepository;

    @Autowired
    public AuthRequestService(AuthRequestRepository authRequestRepository) {
        this.authRequestRepository = authRequestRepository;
    }

    public void addAuthRequest(AuthorizationRequest authorizationRequest) {

        authRequestRepository.save(authorizationRequest);
    }
}
