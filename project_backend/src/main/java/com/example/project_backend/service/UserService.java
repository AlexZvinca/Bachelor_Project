package com.example.project_backend.service;

import com.example.project_backend.entities.User;
import com.example.project_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{

    private final static String USER_NOT_FOUND_MESSAGE = "User not found - email %s";
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();

    }

    public void addUser(User user) {
        boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (emailExists) {
            throw new IllegalStateException("Email is already in use.");
        }

        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        boolean idExists = userRepository.findById(userId).isPresent();
        if(!idExists) {
            throw new IllegalStateException("User not found.");
        }
        userRepository.deleteById(userId);
    }

    }
}
