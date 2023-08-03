package com.example.techversantInfotech.Authservice.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageProcessingException extends Exception{
    private String  errorCode;
    public ImageProcessingException(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
