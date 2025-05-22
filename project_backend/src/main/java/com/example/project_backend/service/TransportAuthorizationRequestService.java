package com.example.project_backend.service;

import com.example.project_backend.dto.StatusCommentsDTO;
import com.example.project_backend.dto.TransportAuthorizationRequestCountyDTO;
import com.example.project_backend.dto.TransportAuthorizationRequestDTO;
import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.entities.*;
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
                transportAuthorizationRequest.licensePlateNumber(),
                transportAuthorizationRequest.description(),
                transportAuthorizationRequest.fromDate(),
                transportAuthorizationRequest.untilDate()
        );
        System.out.println(newTransportAuthorizationRequest);
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

    public List<TransportAuthorizationRequestCountyDTO> getRequestsByCounty(County county) {
        List<TransportAuthorizationRequest> transportAuthorizationRequests = transportAuthorizationRequestRepository.findAllByCounty(county);

        return transportAuthorizationRequests.stream()
                .map(transportAuthorizationRequest -> new TransportAuthorizationRequestCountyDTO(
                        transportAuthorizationRequest.getId(),
                        transportAuthorizationRequest.getCounty(),
                        transportAuthorizationRequest.getLicensePlateNumber(),
                        transportAuthorizationRequest.getDescription(),
                        transportAuthorizationRequest.getStatus(),
                        transportAuthorizationRequest.getCreatedAt(),
                        transportAuthorizationRequest.getFromDate(),
                        transportAuthorizationRequest.getUntilDate(),
                        transportAuthorizationRequest.getUser().getId())
                ).toList();
    }

    public void changeTransportAuthorizationRequestStatus(Integer id, String status)
    {
        TransportAuthorizationRequest transportAuthorizationRequest = transportAuthorizationRequestRepository.findById(id).orElseThrow();
        System.out.println(status);
        status = status.substring(1, status.length() - 1);

        switch (status)
        {
            case "GRANTED":
                transportAuthorizationRequest.setStatus(Status.GRANTED);
                break;
            case "NOT_GRANTED":
                transportAuthorizationRequest.setStatus(Status.NOT_GRANTED);
                break;
            default:
                throw new RuntimeException("Invalid status");
        }

        transportAuthorizationRequestRepository.save(transportAuthorizationRequest);
    }

    public void changeTransportAuthorizationRequestStatusAndComments(Integer id, StatusCommentsDTO status_comments)
    {
        TransportAuthorizationRequest transportAuthorizationRequest = transportAuthorizationRequestRepository.findById(id).orElseThrow();

        String status;
        status = status_comments.status();

        System.out.println(status);

        //status = status.substring(1, status.length() - 1);
        switch (status)
        {
            case "GRANTED":
                transportAuthorizationRequest.setStatus(Status.GRANTED);
                break;
            case "NOT_GRANTED":
                transportAuthorizationRequest.setStatus(Status.NOT_GRANTED);
                break;
            default:
                throw new RuntimeException("Invalid status");
        }

        transportAuthorizationRequest.setStatusComments(status_comments.comments());

        transportAuthorizationRequestRepository.save(transportAuthorizationRequest);
    }

    public boolean isPlateAuthorized(String licensePlateNumber) {
        List<TransportAuthorizationRequest> requests = transportAuthorizationRequestRepository.findByLicensePlateNumber(licensePlateNumber);
        return requests.stream().anyMatch(r -> r.getStatus() == Status.GRANTED);
    }
}
