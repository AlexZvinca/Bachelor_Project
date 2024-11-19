package com.example.project_backend.controller;


import com.example.project_backend.entities.TransportAuthorizationRequest;
import com.example.project_backend.service.TransportAuthorizationRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/authorizationRequest")
public class TransportAuthorizationRequestController {

    private final TransportAuthorizationRequestService transportAuthorizationRequestService;

    public TransportAuthorizationRequestController(TransportAuthorizationRequestService transportAuthorizationRequestService) {
        this.transportAuthorizationRequestService = transportAuthorizationRequestService;
    }

    @PostMapping
    public void createRequest(@RequestBody TransportAuthorizationRequest authorizationRequest) {
        transportAuthorizationRequestService.addAuthRequest(authorizationRequest);
    }
}
