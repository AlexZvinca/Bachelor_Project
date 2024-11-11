package com.example.project_backend.controller;


import com.example.project_backend.entities.AuthorizationRequest;
import com.example.project_backend.entities.User;
import com.example.project_backend.service.AuthRequestService;
import com.example.project_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/authorizationRequest")
public class AuthRequestController {

    private final AuthRequestService authRequestService;

    public AuthRequestController(AuthRequestService authRequestService) {
        this.authRequestService = authRequestService;
    }

    @PostMapping
    public void createRequest(@RequestBody AuthorizationRequest authorizationRequest) {
        authRequestService.addAuthRequest(authorizationRequest);
    }
}
