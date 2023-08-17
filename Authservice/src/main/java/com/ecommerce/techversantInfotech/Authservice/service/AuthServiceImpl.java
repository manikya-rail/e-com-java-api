package com.ecommerce.techversantInfotech.Authservice.service;

import com.ecommerce.techversantInfotech.Authservice.Dto.*;
import com.ecommerce.techversantInfotech.Authservice.Exception.ImageProcessingException;
import com.ecommerce.techversantInfotech.Authservice.Exception.UserAlreadyRegistered;
import com.ecommerce.techversantInfotech.Authservice.Exception.UserNotFoundException;
import com.ecommerce.techversantInfotech.Authservice.JWTutils.JwtService;
import com.ecommerce.techversantInfotech.Authservice.config.CustomUserDetails;
import com.ecommerce.techversantInfotech.Authservice.entity.User;
import com.ecommerce.techversantInfotech.Authservice.enumDetails.UserRole;
import com.ecommerce.techversantInfotech.Authservice.repository.UserCredential;
import com.ecommerce.techversantInfotech.Authservice.utils.ImageProcessingUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
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
    public User saveUser(String user, MultipartFile file){
        final String filePath="/Users/jenobpj/Documents/Files/";
       UserDto userDto= ImageProcessingUtils.convertObject(user);
       validateUserNotRegistered(userDto.getEmail());
          User newUser=User.builder()
                    .name(userDto.getName())
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .email(userDto.getEmail())
                    .mobileNumber(userDto.getMobileNumber())
                    .Description(userDto.getDescription())
                    .location(userDto.getLocation())
                    .role(UserRole.valueOf(userDto.getRole()))
                    .image(filePath+file.getOriginalFilename())
                    .active(true)
                    .delete(false)
                    .modifiedOn(null)
                    .createOn(new Date())
                    .build();

        try {
            file.transferTo(new File(newUser.getImage()));
        } catch (IOException e) {
            throw new ImageProcessingException("NOT_PROCESSED", "Image is not processed successfully");
        }
        return userCredential.save(newUser);


    }

    @Override
    @Transactional
    public User clientRegister(String user, MultipartFile file) {
        final String filePath="/Users/jenobpj/Documents/images";
        UserDto userDto= ImageProcessingUtils.convertObject(user);
        validateUserNotRegistered(userDto.getEmail());
        //byte[] image=imageProcess(file);
        User newUser=User.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .mobileNumber(userDto.getMobileNumber())
                .Description(userDto.getDescription())
                .location(userDto.getLocation())
                .role(UserRole.ADMIN)
                .image(filePath+file.getName())
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
                    .image(userDetails.getImage())
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
    public InputStream downloadImage(int id) throws FileNotFoundException {
        User user=userCredential.findById(id).orElseThrow(()->new UserNotFoundException("USER_NOT_FOUND","User is not found"));

        return new FileInputStream(user.getImage());
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
        final String filePath="/Users/jenobpj/Documents/images";
        User user=userCredential.findById(id).orElseThrow(()->new UserNotFoundException("USER_NOT_FOUND","User is not found"));

        UserDto userDto=ImageProcessingUtils.convertObject(updateUser);
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setDescription(userDto.getDescription());
        user.setLocation(userDto.getLocation());
        user.setModifiedOn(new Date());

        //byte[] image=imageProcess(file);
        user.setImage(filePath+file.getName());

        userCredential.save(user);
        return"updated successfully";

    }

    private void validateUserNotRegistered(String email){
        Optional<User> admin=userCredential.findByEmail(email);
        if(admin.isPresent()){
            throw new UserAlreadyRegistered("User is already registered","USER_REGISTERED" );
        }
    }




}
