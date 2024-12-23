package com.example.project_backend.controller;

import com.example.project_backend.dto.TransportAuthorizationRequestDTO;
import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.entities.TransportAuthorizationRequest;
import com.example.project_backend.entities.User;
import com.example.project_backend.service.TransportAuthorizationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "authorizationRequest")
@Validated
public class TransportAuthorizationRequestController {

    private final TransportAuthorizationRequestService transportAuthorizationRequestService;

    public TransportAuthorizationRequestController(TransportAuthorizationRequestService transportAuthorizationRequestService) {
        this.transportAuthorizationRequestService = transportAuthorizationRequestService;
    }


    @PostMapping
    public ResponseEntity<TransportAuthorizationRequest> addRequest(@RequestBody TransportAuthorizationRequestDTO authorizationRequest) {

        TransportAuthorizationRequest newTransportAuthorizationRequest = transportAuthorizationRequestService.addRequest(authorizationRequest);
        if(newTransportAuthorizationRequest == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransportAuthorizationRequest>> getRequestsByUserId(@PathVariable String userId) {
        List<TransportAuthorizationRequest> requests = transportAuthorizationRequestService.getRequestsByUserId(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(requests);
    }
}
