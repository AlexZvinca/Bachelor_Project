package com.example.project_backend.service;

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
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService{

    private final static String USER_NOT_FOUND_MESSAGE = "User not found - email %s";
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();

    }

    public User getUserById(String id)
    {
        return userRepository.findById(id).orElse(null);
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
                    user.setPassword(userPropValuePairDTO.value());
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



}
