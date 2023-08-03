package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.AuthResponse;
import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.Exception.ImageProcessingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface AuthService {

    public String saveUser(UserDto userDto);
    public AuthResponse login(AuthRequest authRequest);
}
