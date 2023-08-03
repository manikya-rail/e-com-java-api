package com.example.techversantInfotech.Authservice.Dto;

import com.example.techversantInfotech.Authservice.enumDetails.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtDto {
    private int id;
    private String username;
    private String email;
    private UserRole role;
}
