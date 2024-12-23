package com.example.project_backend.service;

import com.example.project_backend.dto.TransportAuthorizationRequestDTO;
import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.entities.County;
import com.example.project_backend.entities.TransportAuthorizationRequest;
import com.example.project_backend.entities.User;
import com.example.project_backend.repository.TransportAuthorizationRequestRepository;
import com.example.project_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportAuthorizationRequestService {

    private final TransportAuthorizationRequestRepository transportAuthorizationRequestRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransportAuthorizationRequestService(TransportAuthorizationRequestRepository transportAuthorizationRequestRepository, UserRepository userRepository) {
        this.transportAuthorizationRequestRepository = transportAuthorizationRequestRepository;
        this.userRepository = userRepository;
    }

    public TransportAuthorizationRequest addRequest(TransportAuthorizationRequestDTO transportAuthorizationRequest) {

        Optional<User> userOptional = userRepository.findById(transportAuthorizationRequest.userId());
        if (userOptional.isEmpty()) {
            return null;
        }

        TransportAuthorizationRequest newTransportAuthorizationRequest = new TransportAuthorizationRequest(
                userOptional.get(),
                County.valueOf(transportAuthorizationRequest.county()),
                transportAuthorizationRequest.idCopy(),
                transportAuthorizationRequest.licensePlateNumber(),
                transportAuthorizationRequest.vehicleIdentification(),
                transportAuthorizationRequest.description()
        );
        try
        {
            return transportAuthorizationRequestRepository.save(newTransportAuthorizationRequest);
        } catch (Exception e)
        {
            return null;
        }
    }

    public List<TransportAuthorizationRequest> getRequestsByUserId(String userId) {
        return transportAuthorizationRequestRepository.findAllByUserId(userId);
    }

}
