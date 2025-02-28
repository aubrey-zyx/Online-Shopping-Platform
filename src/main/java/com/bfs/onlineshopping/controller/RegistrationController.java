package com.bfs.onlineshopping.controller;

import com.bfs.onlineshopping.domain.User;
import com.bfs.onlineshopping.domain.request.RegisterRequest;
import com.bfs.onlineshopping.domain.response.RegisterResponse;
import com.bfs.onlineshopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Check if username or email already exists
        if (userService.existsByUsername(request.getUsername()) || userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Username or email is already taken."));
        }

        // Hash password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Create and save user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(0); // Default role as User

        userService.saveUser(user);

        return ResponseEntity.ok(new RegisterResponse("User registered successfully."));
    }
}
