package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.*;
import com.example.techversantInfotech.Authservice.JWTutils.JwtService;
import com.example.techversantInfotech.Authservice.config.CustomUserDetails;
import com.example.techversantInfotech.Authservice.entity.User;
import com.example.techversantInfotech.Authservice.enumDetails.UserRole;
import com.example.techversantInfotech.Authservice.repository.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public AuthResponse login(AuthRequest authRequest){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));

        //Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
        if (authentication.isAuthenticated()){
            CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();

            JwtDto jwtDto=JwtDto.builder()
                    .id(userDetails.getId())
                    .email(userDetails.getEmail())
                    .username(userDetails.getUsername())
                    .role(userDetails.getRole())
                   .build();
            String token=jwtService.generateToken(jwtDto);

            UserDetails details=UserDetails.builder()
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .role(String.valueOf(userDetails.getRole()))
                    .build();
            AuthResponse authResponse=AuthResponse.builder()
                    .userDetails(details)
                    .token(token)
                    .build();
            return authResponse;
        }else{
            throw new RuntimeException("User is not found");
        }


    }
}
