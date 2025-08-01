package com.example.project_backend.controller;

import com.example.project_backend.dto.UserCreationDTO;
import com.example.project_backend.entities.User;
import com.example.project_backend.service.BucketService;
import com.example.project_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/id-document")
    public ResponseEntity<byte[]> getUserIdDocument(@PathVariable String id) {
        try {
            byte[] fileContent = userService.getUserIdDocument(id);
            if (fileContent == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=id_document");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/jpeg");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        if (newUser == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String userId = newUser.getId();
        bucketService.uploadFile(userId, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}/role")
    public ResponseEntity<Void> changeUserRole(@PathVariable String id,
                                               @RequestBody String role)
    {
        try {
            userService.changeUserRole(id, role);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id)
    {
        try {
            userService.deleteUser(id);
        }catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
