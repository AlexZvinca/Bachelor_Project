package com.example.project_backend.controller;


import com.example.project_backend.entities.TransportAuthorizationRequest;
import com.example.project_backend.service.TransportAuthorizationRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "authorizationRequest")
public class TransportAuthorizationRequestController {

    private final TransportAuthorizationRequestService transportAuthorizationRequestService;

    public TransportAuthorizationRequestController(TransportAuthorizationRequestService transportAuthorizationRequestService) {
        this.transportAuthorizationRequestService = transportAuthorizationRequestService;
    }

    @PostMapping
    public void createRequest(@RequestBody TransportAuthorizationRequest authorizationRequest) {
        transportAuthorizationRequestService.addAuthRequest(authorizationRequest);
    }

    @GetMapping("/user/{userId}")
    public List<TransportAuthorizationRequest> getRequestsByUserId(@PathVariable String userId) {
        return transportAuthorizationRequestService.getRequestsByUserId(userId);
    }
}
