package com.ecommerce.techversantInfotech.Authservice.entity;

import com.ecommerce.techversantInfotech.Authservice.enumDetails.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;
//import javax.validation.constraints.*;
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
    @Column(unique = true)
    private String username;
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "mobile number cannot be null")
    @Column(unique = true)
    private String mobileNumber;
    @Lob
    @Column(length = 1000)
    private String image;
    private String location;
    private String Description;
    private boolean active;
    private boolean delete;
    private Date modifiedOn;
    private Date createOn;
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;

}