package com.example.project_backend.repository;

import com.example.project_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, String> {

    User findByEmail(String email);
}
