package com.example.techversantInfotech.Authservice.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data

public class UserAlreadyRegistered extends RuntimeException{

    private String  errorCode;
    public UserAlreadyRegistered(String message, String errorCode){
        super(message);
       this.errorCode=errorCode;
    }

}
