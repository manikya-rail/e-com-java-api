package com.ecommerce.techversantInfotech.Authservice.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageProcessingException extends RuntimeException{
    private String  errorCode;
    public ImageProcessingException(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
