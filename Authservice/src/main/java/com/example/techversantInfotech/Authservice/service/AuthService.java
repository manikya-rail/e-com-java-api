package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.AuthResponse;
import com.example.techversantInfotech.Authservice.Dto.UserDto;


public interface AuthService {

    public String saveUser(UserDto userDto);
    public AuthResponse login(AuthRequest authRequest);
}
