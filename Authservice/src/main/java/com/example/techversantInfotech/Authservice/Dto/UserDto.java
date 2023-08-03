package com.example.techversantInfotech.Authservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {
    private String name;
    private String username;
    private String email;
    private String mobileNumber;
    private String Description;
    private String Password;
    private String role;
}
