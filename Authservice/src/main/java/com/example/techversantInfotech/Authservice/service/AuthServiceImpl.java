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
import com.example.techversantInfotech.Authservice.utils.ImageProcessingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
    @Transactional
    public User saveUser(String user,MultipartFile file){

       UserDto userDto= ImageProcessingUtils.convertObject(user);

       Optional<User> admin=userCredential.findByEmail(userDto.getEmail());
        if(admin.isPresent()){
            throw new UserAlreadyRegistered("User is already registered","USER_REGISTERED" );
        }


        byte[] image=null;
        if (!file.isEmpty()){
            try {
                image = ImageProcessingUtils.compressImageFuture(file.getBytes());
            } catch (IOException e){
                throw  new ImageProcessingException("IMAGE_NOT_PROCESS","Image is not uploaded successfully");
            }
        }



            User newUser=User.builder()
                    .name(userDto.getName())
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .mobileNumber(userDto.getMobileNumber())
                    .Description(userDto.getDescription())
                    .location(userDto.getLocation())
                    .role(UserRole.valueOf(userDto.getRole()))
                    .image(image)
                    .active(true)
                    .delete(false)
                    .modifiedOn(null)
                    .createOn(new Date())
                    .build();
            return userCredential.save(newUser);


    }

    @Override
    @Transactional
    public User clientRegister(String user, MultipartFile file) {
        UserDto userDto= ImageProcessingUtils.convertObject(user);

        Optional<User> admin=userCredential.findByEmail(userDto.getEmail());
        if(admin.isPresent()){
            throw new UserAlreadyRegistered("User is already registered","USER_REGISTERED" );
        }

        byte[] image=null;
        if (!file.isEmpty()){
            try {
                image = ImageProcessingUtils.compressImageFuture(file.getBytes());
            } catch (IOException e){
                throw  new ImageProcessingException("IMAGE_NOT_PROCESS","Image is not uploaded successfully");
            }
        }

        User newUser=User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .mobileNumber(userDto.getMobileNumber())
                .Description(userDto.getDescription())
                .location(userDto.getLocation())
                .role(UserRole.ADMIN)
                .image(image)
                .active(true)
                .delete(false)
                .modifiedOn(null)
                .createOn(new Date())
                .build();
        return userCredential.save(newUser);

    }

    @Override
    public AuthResponse login(AuthRequest authRequest){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
        if (authentication.isAuthenticated()){
            CustomUserDetails userDetails= (CustomUserDetails) authentication.getPrincipal();

            JwtDto jwtDto=JwtDto.builder()
                    .id(userDetails.getId())
                    .email(userDetails.getEmail())
                    .username(userDetails.getUsername())
                    .role(userDetails.getRole())
                   .build();
            String token=jwtService.generateToken(jwtDto);

            byte[] imageByte=null;

            if(userDetails.getImage()!= null && userDetails.getImage().length!=0) {
                imageByte = ImageProcessingUtils.decompressImage(userDetails.getImage());
            }

            UserDetails details=UserDetails.builder()
                    .name(userDetails.getName())
                    .description(userDetails.getDescription())
                    .active(userDetails.isActive())
                    .delete(userDetails.isDelete())
                    .createdOn(userDetails.getCreatedOn())
                    .location(userDetails.getLocation())
                    .modifiedOn(userDetails.getModifiedOn())
                    .mobileNumber(userDetails.getMobileNumber())
                    .id(userDetails.getId())
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .role(String.valueOf(userDetails.getRole()))
                    .build();


           return AuthResponse.builder()
                    .userDetails(details)
                    .image(imageByte)
                    .token(token)
                    .build();
        }else{
            throw new UserNotFoundException("USER_NOT_FOUND","User is not found");
        }

    }
    @Override
    public User getUserById(int id) {
        Optional<User> user=userCredential.findById(id);
        if(user.isEmpty()){
            throw new UserNotFoundException("USER_NOT_FOUND","User is not found");
        }
        return user.get();
    }

    @Override
    public List<User> getAllClients() {
        List<User> users=userCredential.findAll();
        if(users.isEmpty()){
           throw new UserNotFoundException("USER_NOT_FOUND","Users is not available");
        }
       return users.stream().filter(user -> user.getRole()!=UserRole.SUPER_ADMIN).toList();
    }

    @Override
    public byte[] downloadImage(int id) {
        User user=userCredential.findById(id).orElseThrow(()->new UserNotFoundException("USER_NOT_FOUND","User is not found"));
        return ImageProcessingUtils.decompressImageFuture(user.getImage());
    }

    @Override
    public String deleteClient(int id) {
        if(userCredential.existsById(id)){
            userCredential.deleteById(id);
            return "Delete client successfully";
        } else {
            throw new UserNotFoundException("USER_NOT_FOUND","User is not found");
        }
    }

    @Override
    public String updateClient(String updateUser, MultipartFile file,int id) {

        User user=userCredential.findById(id).orElseThrow(()->new UserNotFoundException("USER_NOT_FOUND","User is not found"));

        UserDto userDto=ImageProcessingUtils.convertObject(updateUser);
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setDescription(userDto.getDescription());
        user.setLocation(userDto.getLocation());

        byte[] image=null;
        if (!file.isEmpty()){
            try {
                image = ImageProcessingUtils.compressImageFuture(file.getBytes());
            } catch (IOException e){
                throw  new ImageProcessingException("IMAGE_NOT_PROCESS","Image is not uploaded successfully");
            }
        }
        user.setImage(image);

        userCredential.save(user);
        return"updated successfully";

    }
}
