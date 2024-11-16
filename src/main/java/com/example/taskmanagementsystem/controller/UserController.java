package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Operations pertaining to users in the task management system")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User created successfully")
    })
    public ResponseEntity<User> createUser(@RequestBody @Parameter(description = "User object", required = true) User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}