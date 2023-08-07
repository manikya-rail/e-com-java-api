package com.example.techversantInfotech.Authservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
   UserDetails userDetails;
   private byte[] image;
   private String token;
}
