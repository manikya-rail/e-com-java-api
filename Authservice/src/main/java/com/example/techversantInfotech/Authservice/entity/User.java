package com.example.techversantInfotech.Authservice.entity;

import com.example.techversantInfotech.Authservice.enumDetails.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin_details")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "Name cannot be null")
    private String name;
    @NotBlank(message = "username cannot be null")
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "mobile number cannot be null")
    private String mobileNumber;
    @Lob
    @Column(length = 1000)
    private byte[] image;
    private String Description;
    private boolean active;
    private boolean delete;
    private Date modifiedOn;
    private Date createOn;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
