package com.ecommerce.techversantInfotech.Authservice.service;

import com.ecommerce.techversantInfotech.Authservice.Dto.AuthRequest;
import com.ecommerce.techversantInfotech.Authservice.Dto.AuthResponse;
import com.ecommerce.techversantInfotech.Authservice.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public interface AuthService {

    public User saveUser(String userDto, MultipartFile file);

    public User clientRegister(String userDto, MultipartFile file);

    public AuthResponse login(AuthRequest authRequest);

    User getUserById(int id);

    List<User> getAllClients();

    InputStream downloadImage(int id) throws FileNotFoundException;

    String deleteClient(int id);

    String updateClient(String userDto, MultipartFile file,int id);
}
