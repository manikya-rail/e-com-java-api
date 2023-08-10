package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.AuthResponse;
import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.Exception.ImageProcessingException;
import com.example.techversantInfotech.Authservice.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface AuthService {

    public User saveUser(String userDto, MultipartFile file);

    public User clientRegister(String userDto, MultipartFile file);

    public AuthResponse login(AuthRequest authRequest);

    User getUserById(int id);

    List<User> getAllClients();

    byte[] downloadImage(int id);
}
