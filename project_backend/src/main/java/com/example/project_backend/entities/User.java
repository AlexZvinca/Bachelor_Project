package com.example.project_backend.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Entity
@Table(name="user_info")
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class User{
    @Id
    @GeneratedValue(generator = "county_based_id")
    @GenericGenerator(name = "county_based_id", strategy = "com.example.project_backend.entities.IdGenerator")
    @Column(updatable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(nullable = false, length = 2)
    private County county;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true, length = 13)
    private String cnp;

    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TransportAuthorizationRequest> transportAuthorizationRequests;

    public User(String id, String email, String password, String phoneNumber, String firstName, String lastName, LocalDate dateOfBirth, County county, String city, String address, String cnp, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.county = county;
        this.city = city;
        this.address = address;
        this.cnp = cnp;
        this.userRole = userRole;
    }

    public User(String email, String password, String phoneNumber, String firstName, String lastName, LocalDate dateOfBirth, County county, String city, String address, String cnp, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.county = county;
        this.city = city;
        this.address = address;
        this.cnp = cnp;
        this.userRole = userRole;
    }

    public User(String email, String password, String phoneNumber, String firstName, String lastName, LocalDate dateOfBirth, County county, String city, String address, String cnp) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.county = county;
        this.city = city;
        this.address = address;
        this.cnp = cnp;
        this.userRole = UserRole.REQUESTOR;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", county=" + county +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", cnp='" + cnp + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
