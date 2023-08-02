package com.example.techversantInfotech.Authservice.service;

import com.example.techversantInfotech.Authservice.Dto.*;
import com.example.techversantInfotech.Authservice.Exception.ImageProcessingException;
import com.example.techversantInfotech.Authservice.Exception.UserAlreadyRegistered;
import com.example.techversantInfotech.Authservice.Exception.UserNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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
        Optional<User> user=userCredential.findByEmail(userDto.getEmail());
        if(!user.isEmpty()){
            throw new UserAlreadyRegistered("User is already registered","USER_REGISTERED" );
        }
           User newUser=User.builder()
                   .name(userDto.getName())
                   .username(userDto.getUsername())
                   .password(passwordEncoder.encode(userDto.getPassword()))
                   .email(userDto.getEmail())
                   .mobileNumber(userDto.getMobileNumber())
                   .Description(userDto.getDescription())
                   .active(true)
                   .delete(false)
                   .modifiedOn(null)
                   .createOn(new Date())
                   .role(UserRole.valueOf(userDto.getRole()))
                   .build();
           userCredential.save(newUser);
           return "New super admin has added";



    }

    @Override
    public AuthResponse login(AuthRequest authRequest){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));

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
            throw new UserNotFoundException("USER_NOT_FOUND","User is not found");
        }


    }
}
