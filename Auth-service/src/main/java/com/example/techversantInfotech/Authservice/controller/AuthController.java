package com.example.techversantInfotech.Authservice.controller;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.UserRole;
import com.example.techversantInfotech.Authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto){
        return authService.saveUser(userDto);
    }

    @PostMapping("/login")
    public  String login(@RequestBody AuthRequest authRequest){
        UserDto userDto=UserDto.builder()
                .username(authRequest.getUsername())
                .email("")
                .role(String.valueOf(UserRole.SELLER))
                .Password(authRequest.getPassword())
                .build();

        return authService.login(userDto);

    }


}
