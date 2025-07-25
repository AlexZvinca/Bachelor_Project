package com.example.project_backend.service;



import com.example.project_backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.project_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserBuilder userBuilder = org.springframework.security.core.userdetails
                .User.withUsername(user.getEmail()).password(user.getPassword());

        return userBuilder.build();

    }
}
