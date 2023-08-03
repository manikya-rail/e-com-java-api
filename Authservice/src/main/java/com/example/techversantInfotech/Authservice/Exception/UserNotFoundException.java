package com.example.techversantInfotech.Authservice.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserNotFoundException extends RuntimeException{
    private String errorCode;
    public UserNotFoundException(String errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }
}
