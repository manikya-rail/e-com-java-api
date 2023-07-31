package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.JWTutils.JwtService;
import com.example.techversantInfotech.Authservice.UserRole;
import com.example.techversantInfotech.Authservice.config.CustomUserDetails;
import com.example.techversantInfotech.Authservice.entity.User;
import com.example.techversantInfotech.Authservice.repository.UserCredential;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
   private UserCredential userCredential;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    //For adding new user
    public String saveUser(UserDto userDto) {
        Optional<User> user=userCredential.findByUsername(userDto.getUsername());
        if(!user.isEmpty()){
            throw new RuntimeException("User is already registered");
        }

        try {
            User newUser=User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .role(UserRole.valueOf(userDto.getRole()))
                    .build();
            userCredential.save(newUser);
            return "New user has added";

        } catch (Exception e){
            throw  new RuntimeException("Something went wrong please check it later");
        }
    }

    @Override
    public String login(UserDto userDto){
        Authentication authentication;
        try{
             authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
        }catch (BadCredentialsException e){
            throw new RuntimeException("Password is incorrect");
        }
        //Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
        if (authentication.isAuthenticated()){

            CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();
            System.out.println(userDetails.getUsername());
            User user=User.builder()
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .role(userDetails.getRole())
                    .build();
            return jwtService.generateToken(user);
        }else{
            throw new RuntimeException("User is not found");
        }


    }
}
