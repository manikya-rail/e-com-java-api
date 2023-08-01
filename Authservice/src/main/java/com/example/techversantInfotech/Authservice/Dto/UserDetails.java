package com.example.techversantInfotech.Authservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    private int id;
    private String username;
    private String email;
    private String role;
}
