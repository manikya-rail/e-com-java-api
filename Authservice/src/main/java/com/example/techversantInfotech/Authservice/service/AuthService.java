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

   // public User getUserById(int id);

    User getUserById(int id);

    List<User> getAllClients();

    byte[] downloadImage(int id);

    String deleteClient(int id);

    String updateClient(String userDto, MultipartFile file,int id);

}
