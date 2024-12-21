package com.example.project_backend.controller;

import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.entities.User;
import com.example.project_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "users")

public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers()
    {
        List<User> users = userService.getUsers();
        if (users.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserCreationDTO user)
    {
        if (user.email() == null || user.password() == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.addNewUser(user);
        if (newUser == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//    @RequestMapping(method = RequestMethod.OPTIONS, path = "/users")
//    public ResponseEntity<Void> handleOptions() {
//        return ResponseEntity.ok()
//                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
//                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
//                .header("Access-Control-Allow-Origin", "http://localhost:5173")
//                .header("Access-Control-Allow-Credentials", "true")
//                .build();
//    }

}
