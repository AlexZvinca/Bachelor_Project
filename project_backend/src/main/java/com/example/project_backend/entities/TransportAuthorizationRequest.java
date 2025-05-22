package com.example.project_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transport_authorization_request")
public class TransportAuthorizationRequest {
    @Id
    @Column(nullable = false)
    @SequenceGenerator(
            name = "requests_sequence",
            sequenceName = "requests_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "requests_sequence"
    )
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "county", length = 2, nullable = false)
    private County county;

    @Column(name = "license_plate_number", length = 15, nullable = false)
    private String licensePlateNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status_comments", columnDefinition = "TEXT")
    private String statusComments;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "until_date", nullable = false)
    private LocalDate untilDate;

    public TransportAuthorizationRequest(User user, County county, String licensePlateNumber, String description, LocalDate fromDate, LocalDate untilDate) {
        this.user = user;
        this.county = county;
        this.licensePlateNumber = licensePlateNumber;
        this.description = description;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }
}
