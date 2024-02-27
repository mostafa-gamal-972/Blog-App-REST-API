package com.springboot.blog.controller;

import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Resource Operations")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "Login REST API", description = "Login REST API is used login to the system and have access to the rest of Blog APIs")
    @ApiResponse(responseCode = "200", description = "Http Status 200 OK")
    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login (@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
    }

    @Operation(summary = "Register REST API", description = "Register REST API is used create a new user to the system")
    @ApiResponse(responseCode = "201", description = "Http Status 201 Created")
    @PostMapping("/register")
    public ResponseEntity<String> register (@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
