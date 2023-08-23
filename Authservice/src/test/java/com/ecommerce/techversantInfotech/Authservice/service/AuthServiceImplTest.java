package com.ecommerce.techversantInfotech.Authservice.service;

import com.ecommerce.techversantInfotech.Authservice.Dto.UserDto;
import com.ecommerce.techversantInfotech.Authservice.Exception.UserAlreadyRegistered;
import com.ecommerce.techversantInfotech.Authservice.entity.User;
import com.ecommerce.techversantInfotech.Authservice.enumDetails.UserRole;
import com.ecommerce.techversantInfotech.Authservice.repository.UserCredential;
import com.ecommerce.techversantInfotech.Authservice.utils.ImageProcessingUtils;
import static org.mockito.ArgumentMatchers.any;
import lombok.Builder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
//    @Value("${app.filename}")
//    private String filePath;

    @Mock
    private UserCredential userCredential;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthService authService=new AuthServiceImpl();


    @Test
    void negative_registerTest(){
        String expectedJsonString = "{" +
                "\"name\":\"jenob\"," +
                "\"username\":\"jeb\"," +
                "\"email\":\"jenob@gmail.com\"," +
                "\"password\":\"12345\"," +
                "\"location\":\"kochi\"," +
                "\"description\":\"Hi this test purpose\"," +
                "\"mobileNumber\":\"9840280239\"," +
                "\"role\":\"SUPER_ADMIN\"" +
                "}";
        MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);

        UserDto userDto=UserDto.builder()
                .name("jenob")
                .username("jeb")
                .email("jenob@gmail.com")
                .Password("12345")
                .location("kochi")
                .Description("Hi this test purpose")
                .mobileNumber("9840280239")
                .role("SUPER_ADMIN").build();

        User newUser=User.builder()
                .name("jenob")
                .username("jeb")
                .password("12345")
                .email("jenob@gmail.com")
                .mobileNumber("9840280239")
                .Description("Hi this test purpose")
                .location("kochi")
                .role(UserRole.valueOf("SUPER_ADMIN"))
                .active(true)
                .delete(false)
                .modifiedOn(null)
                .createOn(new Date())
                .build();


        try(MockedStatic<ImageProcessingUtils> mockedStatic= Mockito.mockStatic(ImageProcessingUtils.class)){
            mockedStatic.when(()->ImageProcessingUtils.convertObject(expectedJsonString)).thenReturn(userDto);

            when(userCredential.findByEmail("jenob@gmail.com")).thenReturn(Optional.of(newUser));
            Assertions.assertThrows(UserAlreadyRegistered.class,()->authService.saveUser(expectedJsonString,file));
        }


    }
    @Test
    void positive_userRegister_case(){
        String expectedJsonString = "{" +
                "\"name\":\"jenob\"," +
                "\"username\":\"jeb\"," +
                "\"email\":\"jenob@gmail.com\"," +
                "\"password\":\"12345\"," +
                "\"location\":\"kochi\"," +
                "\"description\":\"Hi this test purpose\"," +
                "\"mobileNumber\":\"9840280239\"," +
                "\"role\":\"SUPER_ADMIN\"" +
                "}";
        MockMultipartFile file = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new byte[0]);

        UserDto userDto=UserDto.builder()
                .name("jenob")
                .username("jeb")
                .email("jenob@gmail.com")
                .Password("12345")
                .location("kochi")
                .Description("Hi this test purpose")
                .mobileNumber("9840280239")
                .role("SUPER_ADMIN").build();

        User newUser=User.builder()
                .name("jenob")
                .username("jeb")
                .password("12345")
                .email("jenob@gmail.com")
                .mobileNumber("9840280239")
                .Description("Hi this test purpose")
                .location("kochi")
                .role(UserRole.valueOf("SUPER_ADMIN"))
                .active(true)
                .delete(false)
                .modifiedOn(null)
                .createOn(new Date())
                .build();


        try(MockedStatic<ImageProcessingUtils> mockedStatic= Mockito.mockStatic(ImageProcessingUtils.class)){
            mockedStatic.when(()->ImageProcessingUtils.convertObject(expectedJsonString)).thenReturn(userDto);

            when(userCredential.findByEmail("jenob@gmail.com")).thenReturn(Optional.empty());
           authService.saveUser(expectedJsonString,file);
            verify(userCredential).save(any(User.class));

        }


    }

}