package com.ecommerce.techversantInfotech.Authservice.controller;

import com.ecommerce.techversantInfotech.Authservice.Dto.AuthRequest;
import com.ecommerce.techversantInfotech.Authservice.Dto.AuthResponse;
import com.ecommerce.techversantInfotech.Authservice.JWTutils.JwtService;
import com.ecommerce.techversantInfotech.Authservice.entity.User;
import com.ecommerce.techversantInfotech.Authservice.repository.UserCredential;
import com.ecommerce.techversantInfotech.Authservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


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
    public ResponseEntity<User> register(@RequestParam("user") String userDto,
                                         @RequestPart(value = "file",required = false) MultipartFile file,
                                         @RequestHeader(value = "Authorization", required = false) String authorizationHeader){

       User user=authService.saveUser(userDto,file);
       return new ResponseEntity<>(user,HttpStatus.CREATED);
    }
    @PostMapping(path = "/client/register",consumes ={ MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<User> clientRegister(@RequestParam("user") String userDto,
                                               @RequestPart(value = "file",required = false) MultipartFile file,
                                               @RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        User user=authService.clientRegister(userDto,file);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){

         AuthResponse authResponse=authService.login(authRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(authResponse);

    }
    @GetMapping("/client/{id}")
    public ResponseEntity<User> getById(@PathVariable int id){
        User user=authService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }

    @GetMapping("/client")
    public ResponseEntity<List<User>> getAllClients(){
        List<User> user=authService.getAllClients();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }


    @GetMapping(value="/image/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
        public void downloadImage(@PathVariable int id, HttpServletResponse response) throws IOException {
            InputStream imageData=authService.downloadImage(id);

            if(imageData == null){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return;
            }
           response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imageData,response.getOutputStream());

        }
     @DeleteMapping("/client/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
       String s= authService.deleteClient(id);
       return new ResponseEntity<>(s,HttpStatus.OK);
     }

     @PatchMapping("/client/edit/{id}")
    public String updateClient(@PathVariable int id, @RequestParam("user") String userDto,
                               @RequestPart(value = "file",required = false) MultipartFile file){
         return authService.updateClient(userDto,file,id);
     }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Extract the JWT token from the request headers
        final String token = request.getHeader("Authorization").substring(7);

        if(authService.logout(token)){
            return new ResponseEntity<>("Logout successful",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Please login",HttpStatus.FORBIDDEN);
        }
    }

}
