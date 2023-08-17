package com.ecommerce.techversantInfotech.Authservice.Dto;

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
   private String image;
   private String token;
}
