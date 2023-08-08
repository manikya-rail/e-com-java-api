package com.example.techversantInfotech.Authservice.config;

import com.example.techversantInfotech.Authservice.Exception.UserNotFoundException;
import com.example.techversantInfotech.Authservice.entity.User;
import com.example.techversantInfotech.Authservice.repository.UserCredential;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredential userCredential;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    Optional<User> user= userCredential.findByUsername(username);
        Optional<User> user=userCredential.findByEmail(email);
       return user.map(CustomUserDetails::new).orElseThrow(()-> new UserNotFoundException("USER_NOT_FOUND","User is not found"));

    }
}
