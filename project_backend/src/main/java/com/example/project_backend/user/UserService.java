package com.example.project_backend.user;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    public List<User> getUsers(){
        return List.of(
                new User(
                        "1",
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
                )
        );
    }
}
