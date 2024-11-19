package com.example.project_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transport_authorization_request")
public class TransportAuthorizationRequest {
    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "county", length = 2, nullable = false)
    private County county;

    @Column(name = "id_copy", columnDefinition = "TEXT", nullable = false)
    private String idCopy;

    @Column(name = "license_plate_number", length = 15, nullable = false)
    private String licensePlateNumber;

    @Column(name = "vehicle_identification", columnDefinition = "TEXT", nullable = false)
    private String vehicleIdentification;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;  // Assuming Status is an enum representing different statuses

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
