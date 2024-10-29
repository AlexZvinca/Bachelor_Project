package com.example.project_backend.user;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            User user1 = new User(
                    "alexandru.zvinca@student.upt.ro",
                    "password",
                    "0700000000",
                    "Alexandru - Mihai",
                    "Zvinca",
                    LocalDate.of(2002, 5, 21),
                    "HD",
                    "Orastie",
                    "Strada yy, nr. xx",
                    "5555555555555",
                    Role.REQUESTOR
            );

            User user2 = new User(
                    "alexandru.nicolae@student.upt.ro",
                    "password2",
                    "0710000000",
                    "Alexandru - George",
                    "Nicolae",
                    LocalDate.of(2002, 6, 22),
                    "TM",
                    "Tg Jiu",
                    "Strada yy, nr. xx",
                    "5555555555556",
                    Role.REQUESTOR
            );

            userRepository.saveAll(List.of(user1, user2));
        };
    }
}
