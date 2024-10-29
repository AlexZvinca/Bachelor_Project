package com.example.project_backend.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Entity
@Table(name="user_info")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User{
    @Id
    @GeneratedValue(generator = "county_based_id")
    @GenericGenerator(name = "county_based_id", strategy = "com.example.project_backend.user.IdGenerator")
    private String id;

    private String email;
    private String password;
    private String phone_number;
    private String first_name;
    private String surname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_of_birth;

    private String county;
    private String city;
    private String address;
    private String cnp;

    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;

    public User(String id, String email, String password, String phone_number, String first_name, String surname,
                LocalDate date_of_birth, String county, String city, String address, String cnp, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.surname = surname;
        this.date_of_birth = date_of_birth;
        this.county = county;
        this.city = city;
        this.address = address;
        this.cnp = cnp;
        this.role = role;
    }

    public User(String email, String password, String phone_number, String first_name, String surname, LocalDate date_of_birth, String county, String city, String address, String cnp, Role role) {
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.first_name = first_name;
        this.surname = surname;
        this.date_of_birth = date_of_birth;
        this.county = county;
        this.city = city;
        this.address = address;
        this.cnp = cnp;
        this.role = role;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", first_name='" + first_name + '\'' +
                ", surname='" + surname + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", cnp='" + cnp + '\'' +
                ", role=" + role +
                '}';
    }
}
