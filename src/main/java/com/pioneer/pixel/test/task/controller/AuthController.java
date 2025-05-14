package com.pioneer.pixel.test.task.controller;

import com.pioneer.pixel.test.task.config.JwtUtil;
import com.pioneer.pixel.test.task.dto.JwtResponse;
import com.pioneer.pixel.test.task.dto.LoginRequest;
import com.pioneer.pixel.test.task.dto.RegistrationRequest;
import com.pioneer.pixel.test.task.dto.UserDto;
import com.pioneer.pixel.test.task.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@Tag(name = "Authentication", description = "Operations for user login and registration")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user using email or phone and password, returns JWT token.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Login payload with username (email or phone) and password", required = true, content = @Content(schema = @Schema(implementation = LoginRequest.class))))
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());
        String token = jwtUtil.generateToken(userId);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new user and immediately authenticate, returning a JWT token.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Registration details including name, birth date, password, initial balance, email, phone", required = true, content = @Content(schema = @Schema(implementation = RegistrationRequest.class))))
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegistrationRequest request) {
        UserDto user = userService.register(request);
        String token = jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}