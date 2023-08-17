package com.ecommerce.techversantInfotech.Authservice.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletableFutureException extends RuntimeException{
    private String  errorCode;
    public CompletableFutureException(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }

}
