package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.AuthRequest;
import com.example.taskmanagementsystem.dto.AuthResponse;
import com.example.taskmanagementsystem.dto.RegisterRequest;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.security.JwtTokenUtil;
import com.example.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Operations pertaining to authentication in the task management system")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate JWT token", responses = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody @Parameter(description = "Authentication request object", required = true) AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Parameter(description = "Register request object", required = true) RegisterRequest registerRequest) {

        if (userService.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User newUser = new User(registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getRole());
        userService.createUser(newUser);

        return ResponseEntity.ok("User registered successfully");
    }
}