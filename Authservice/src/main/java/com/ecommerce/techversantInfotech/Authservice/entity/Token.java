package com.ecommerce.techversantInfotech.Authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token_details")
public class Token {
    @Id
    @GeneratedValue
    private  Integer id;

    private String token;

    private boolean expired;

    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
