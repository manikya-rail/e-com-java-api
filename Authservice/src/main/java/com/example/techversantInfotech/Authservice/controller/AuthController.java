package com.example.techversantInfotech.Authservice.controller;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.AuthResponse;
import com.example.techversantInfotech.Authservice.Dto.UserDetails;
import com.example.techversantInfotech.Authservice.Dto.UserDto;
import com.example.techversantInfotech.Authservice.Exception.ImageProcessingException;
import com.example.techversantInfotech.Authservice.entity.User;
import com.example.techversantInfotech.Authservice.repository.UserCredential;
import com.example.techversantInfotech.Authservice.service.AuthService;
import com.example.techversantInfotech.Authservice.utils.ImageProcessingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    UserCredential userCredential;


    @PostMapping(path = "/register",consumes ={ MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> register(@RequestPart("user") String userDto, @RequestPart("file") MultipartFile file){
       String s=authService.saveUser(userDto,file);
       return new ResponseEntity<>(s,HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){

         AuthResponse authResponse=authService.login(authRequest);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(authResponse);

    }
//    @GetMapping("/photo")
//    @Transactional
//     public ResponseEntity<?> dowload(){
//        Optional<User> user= userCredential.findByEmail("jen@gmail.com");
//        if(user.isEmpty()){
//            System.out.println("empty");
//        }
//        byte[] image= ImageProcessingUtils.decompressImage(user.get().getImage());
//        //byte[] image= user.get().getImage();
//        return  ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(image);
//}


}
