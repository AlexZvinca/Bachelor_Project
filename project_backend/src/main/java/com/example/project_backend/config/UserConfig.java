package com.example.project_backend.config;


import com.example.project_backend.entities.County;
import com.example.project_backend.entities.TransportAuthorizationRequest;
import com.example.project_backend.entities.UserRole;
import com.example.project_backend.entities.User;
import com.example.project_backend.repository.TransportAuthorizationRequestRepository;
import com.example.project_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class UserConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, TransportAuthorizationRequestRepository transportAuthorizationRequestRepository) {
        return args -> {
            User user1 = new User(
                    "alexandru.zvinca@student.upt.ro",
                    passwordEncoder.encode("password"),
                    "0700000000",
                    "Alexandru - Mihai",
                    "Zvinca",
                    LocalDate.of(2002, 5, 21),
                    County.HD,
                    "Orastie",
                    "Strada yy, nr. xx",
                    "5555555555555",
                    UserRole.REQUESTOR
            );

            User user2 = new User(
                    "alexandru.nicolae@student.upt.ro",
                    passwordEncoder.encode("password2"),
                    "0710000000",
                    "Alexandru - George",
                    "Nicolae",
                    LocalDate.of(2002, 6, 22),
                    County.TM,
                    "Tg Jiu",
                    "Strada yy, nr. xx",
                    "5555555555556",
                    UserRole.REQUESTOR
            );

            userRepository.saveAll(List.of(user1, user2));

            TransportAuthorizationRequest request1 = new TransportAuthorizationRequest(
                    user1,
                    County.HD,
                    "ID Copy Content 1",
                    "HD-1234-ABC",
                    "VIN1234567890",
                    "Request for heavy goods transport in Hunedoara County"
            );

            TransportAuthorizationRequest request2 = new TransportAuthorizationRequest(
                    user1,
                    County.TM,
                    "ID Copy Content 2",
                    "TM-5678-DEF",
                    "VIN0987654321",
                    "Request for light goods transport in Timis County"
            );

            transportAuthorizationRequestRepository.saveAll(List.of(request1, request2));
        };
    }
}
