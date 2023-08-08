package com.example.techversantInfotech.Authservice.repository;

import com.example.techversantInfotech.Authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredential extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User>findByEmail(String  email);

    Optional<User> findById(int id);
}
