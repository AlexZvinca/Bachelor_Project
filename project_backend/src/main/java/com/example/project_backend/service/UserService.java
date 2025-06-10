package com.example.project_backend.service;

import com.amazonaws.services.s3.model.S3Object;
import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.dto.UserPropValuePairDTO;
import com.example.project_backend.entities.County;
import com.example.project_backend.entities.User;
import com.example.project_backend.entities.UserRole;
import com.example.project_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService{

    private final static String USER_NOT_FOUND_MESSAGE = "User not found - email %s";
    private final UserRepository userRepository;
    private final BucketService bucketService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BucketService bucketService) {
        this.userRepository = userRepository;
        this.bucketService = bucketService;
    }

    public List<User> getUsers(){
        return userRepository.findAll();

    }

    public User getUserById(String id)
    {
        return userRepository.findById(id).orElse(null);
    }

    public byte[] getUserIdDocument(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getIdDocument() == null || user.getIdDocument().isEmpty()) {
            return null;
        }

        try {
            S3Object s3Object = bucketService.downloadFile(user.getIdDocument());
            return s3Object.getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from Cloud", e);
        }
    }

    public User addNewUser(UserCreationDTO user)
    {
        User newUser = new User(user.email(), passwordEncoder.encode(user.password()), user.phoneNumber(), user.firstName(), user.lastName(), user.dateOfBirth(), user.county(), user.city(), user.address(), user.cnp());
        try
        {
            return userRepository.save(newUser);
        } catch (Exception e)
        {
            return null;
        }
    }

    public boolean updateUser(String id, UserPropValuePairDTO[] userPropValuePairDTOS)
    {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
        {
            return false;
        }

        for (UserPropValuePairDTO userPropValuePairDTO : userPropValuePairDTOS)
        {
            switch (userPropValuePairDTO.property())
            {
                case "email":
                    user.setEmail(userPropValuePairDTO.value());
                    break;
                case "password":
                    user.setPassword(passwordEncoder.encode(userPropValuePairDTO.value()));
                    break;
                case "phoneNumber":
                    user.setPhoneNumber(userPropValuePairDTO.value());
                    break;
                case "firstName":
                    user.setFirstName(userPropValuePairDTO.value());
                    break;
                case "lastName":
                    user.setLastName(userPropValuePairDTO.value());
                    break;
                case "date_of_birth":
                    user.setDateOfBirth(LocalDate.parse(userPropValuePairDTO.value()));
                    break;
                case "county":
                    try {
                        County county = County.valueOf(userPropValuePairDTO.value().toUpperCase());
                        user.setCounty(county);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                    break;
                case "city":
                    user.setCity(userPropValuePairDTO.value());
                    break;
                case "address":
                    user.setAddress(userPropValuePairDTO.value());
                    break;
                case "cnp":
                    user.setCnp(userPropValuePairDTO.value());
                    break;

                case "role":
                    try {
                         UserRole userRole = UserRole.valueOf(userPropValuePairDTO.value().toUpperCase());
                        user.setUserRole(userRole);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                    break;

                default:
                    return false;
            }
        }

        userRepository.save(user);
        return true;
    }

    public void deleteUser(String userId) {
        boolean idExists = userRepository.findById(userId).isPresent();
        if(!idExists) {
            throw new IllegalStateException("User not found.");
        }
        userRepository.deleteById(userId);
    }

    public void changeUserRole(String id, String role)
    {
        User user = userRepository.findById(id).orElseThrow();

        System.out.println("The role is: " + role);
        role = role.substring(1, role.length() - 1);

        switch (role)
        {
            case "AUTHORITY":
                user.setUserRole(UserRole.AUTHORITY);
                break;
            case "REQUESTOR":
                user.setUserRole(UserRole.REQUESTOR);
                break;
            case "ADMIN":
                user.setUserRole(UserRole.ADMIN);
                break;
            default:
                throw new RuntimeException("Invalid role");
        }

        userRepository.save(user);
    }



}
