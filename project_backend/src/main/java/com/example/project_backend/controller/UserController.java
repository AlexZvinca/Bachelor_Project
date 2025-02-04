package com.example.project_backend.controller;

import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.entities.User;
import com.example.project_backend.service.BucketService;
import com.example.project_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;
    private final BucketService bucketService;

    @Autowired
    public UserController(UserService userService, BucketService bucketService) {
        this.userService = userService;
        this.bucketService = bucketService;
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
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> addUser(@Valid @RequestPart("details")  UserCreationDTO user,
                                        @RequestPart("idDocument") MultipartFile file)
    {
        if (user.email() == null || user.password() == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.addNewUser(user);
        String userId = newUser.getId();
        bucketService.uploadFile(userId, file);

        if (newUser == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable String id,
                                               @RequestBody String role)
    {
        userService.changeUserRole(id, role);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id)
    {
        try
        {
            userService.deleteUser(id);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
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
