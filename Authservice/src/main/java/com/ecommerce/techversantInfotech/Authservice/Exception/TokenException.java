package com.ecommerce.techversantInfotech.Authservice.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenException extends RuntimeException{
    private String errorCode;
    public TokenException(String errorCode, String message){
        super(message);
        this.errorCode=errorCode;
    }
}
