package com.example.project_backend.controller;

import com.example.project_backend.dto.AuthDTO;
import com.example.project_backend.dto.UserAuthenticationDTO;
import com.example.project_backend.entities.User;
import com.example.project_backend.repository.UserRepository;
import com.example.project_backend.service.CustomUserDetailsService;
import com.example.project_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/token")
    public AuthDTO loginUser(@RequestBody UserAuthenticationDTO authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.email());
        User user = userRepository.findByEmail(authenticationRequest.email());

        //return jwtUtil.generateToken(userDetails);
        return new AuthDTO(user.getId(), jwtUtil.generateToken(userDetails));
    }

}
