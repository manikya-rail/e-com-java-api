package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.UserDto;
import org.springframework.stereotype.Service;


public interface AuthService {

    public String saveUser(UserDto userDto);
    public String login(UserDto userDto);
}
