package com.example.project_backend.controller;

import com.example.project_backend.dto.StatusCommentsDTO;
import com.example.project_backend.dto.TransportAuthorizationRequestCountyDTO;
import com.example.project_backend.dto.TransportAuthorizationRequestDTO;
import com.example.project_backend.entities.County;
import com.example.project_backend.entities.TransportAuthorizationRequest;
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/county/{county}")
    public ResponseEntity<List<TransportAuthorizationRequestCountyDTO>> getRequestsByCounty(@PathVariable String county) {
        County countyEnum = County.valueOf(county.toUpperCase());
        List<TransportAuthorizationRequestCountyDTO> requests = transportAuthorizationRequestService.getRequestsByCounty(countyEnum);
        if (requests.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(requests);
    }

    @PutMapping(path = "status/{id}")
    public ResponseEntity<Void> changeTransportAuthorizationRequestStatus(@PathVariable Integer id,
                                               @RequestBody String status)
    {

        TransportAuthorizationRequest newTransportAuthorizationRequest = transportAuthorizationRequestService.changeTransportAuthorizationRequestStatus(id, status);
        if(newTransportAuthorizationRequest == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "status-motivation/{id}")
    public ResponseEntity<Void> changeTransportAuthorizationRequestStatusAndComments(@PathVariable Integer id,
                                                                          @RequestBody StatusCommentsDTO status_comments)
    {

        TransportAuthorizationRequest newTransportAuthorizationRequest = transportAuthorizationRequestService.changeTransportAuthorizationRequestStatusAndComments(id, status_comments);
        if(newTransportAuthorizationRequest == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkAuthorization(@RequestParam String plate) {
        boolean authorized = transportAuthorizationRequestService.isPlateAuthorized(plate);
        String body = "{\"authorized\": " + authorized + "}";
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .header("Content-Length", String.valueOf(body.length()))
                .body(body);
    }
}
