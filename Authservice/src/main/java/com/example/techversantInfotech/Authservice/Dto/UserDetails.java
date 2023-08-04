package com.example.techversantInfotech.Authservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails {
    private int id;
    private String name;
    private String mobileNumber;
    private byte[] image;
    private String description;
    private boolean active;
    private boolean delete;
    private Date modifiedOn;
    private Date createdOn;
    private String username;
    private String location;
    private String email;
    private String role;
}
