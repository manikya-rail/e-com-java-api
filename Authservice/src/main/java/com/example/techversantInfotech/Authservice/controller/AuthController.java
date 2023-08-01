package com.example.techversantInfotech.Authservice.controller;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.AuthResponse;
import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto){
        return authService.saveUser(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){

         AuthResponse authResponse=authService.login(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }


}
