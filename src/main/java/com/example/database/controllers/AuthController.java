package com.example.database.controllers;

import com.example.database.dto.AuthenticationRequest;
import com.example.database.dto.AuthenticationResponse;
import com.example.database.dto.RegisterRequest;
import com.example.database.enteties.User;
import com.example.database.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            ResponseCookie cookie = ResponseCookie.from("auth_token", response.getToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofMillis(jwtExpirationMs))
                    .sameSite("Lax")
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString())
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest user) {
        try {

            authenticationService.registerUser(new User(user.getName(), user.getEmail(), user.getPassword(), user.getDistrict(),
                    user.getEducationalInstitution(), user.getPosition(), user.getPhone(), user.getAddress()));
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
