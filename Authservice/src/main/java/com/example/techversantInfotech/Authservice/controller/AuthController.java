package com.example.techversantInfotech.Authservice.controller;

import com.example.techversantInfotech.Authservice.Dto.AuthRequest;
import com.example.techversantInfotech.Authservice.Dto.AuthResponse;
import com.example.techversantInfotech.Authservice.JWTutils.JwtService;
import com.example.techversantInfotech.Authservice.entity.User;
import com.example.techversantInfotech.Authservice.repository.UserCredential;
import com.example.techversantInfotech.Authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//import javax.transaction.Transactional;
//import javax.validation.Valid;
//import java.io.IOException;
//import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    UserCredential userCredential;

    @Autowired
    JwtService jwtService;


    @PostMapping(path = "/register",consumes ={ MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<User> register(@RequestPart("user") String userDto,
                                           @RequestPart(value = "file",required = false) MultipartFile file,
                                           @RequestHeader(value = "Authorization", required = false) String authorizationHeader){

       User user=authService.saveUser(userDto,file);
       return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){

         AuthResponse authResponse=authService.login(authRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(authResponse);

    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id){
        User user=authService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }

    @GetMapping("/client")
    public ResponseEntity<List<User>> getAllClients(){
        List<User> user=authService.getAllClients();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
    @PostMapping(path = "/client/register",consumes ={ MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<User> clientRegister(@RequestPart("user") String userDto,
                                         @RequestPart(value = "file",required = false) MultipartFile file,
                                         @RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        User user=authService.clientRegister(userDto,file);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @GetMapping("/image/{id}")
        public ResponseEntity<?> downloadImage(@PathVariable int id){
            byte[] imageData=authService.downloadImage(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);

        }
     @DeleteMapping("/client/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
       String s= authService.deleteClient(id);
       return new ResponseEntity<>(s,HttpStatus.OK);
     }

     @PatchMapping("/client/{id}")
    public String updateClient(@PathVariable int id, @RequestPart("user") String userDto,
                               @RequestPart("file") MultipartFile file){
         return authService.updateClient(userDto,file,id);
     }

}
