package com.ecommerce.techversantInfotech.Authservice.repository;

import com.ecommerce.techversantInfotech.Authservice.Entity.Test_User;
import com.ecommerce.techversantInfotech.Authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestRepo extends JpaRepository<Test_User,Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
