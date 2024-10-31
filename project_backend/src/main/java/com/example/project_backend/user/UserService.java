package com.example.project_backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

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
