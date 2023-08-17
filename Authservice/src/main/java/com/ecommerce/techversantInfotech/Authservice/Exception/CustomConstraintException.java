package com.ecommerce.techversantInfotech.Authservice.Exception;

import com.ecommerce.techversantInfotech.Authservice.Dto.ErrorResponse;


public class CustomConstraintException {

    public  static ErrorResponse customConstraintResponse(Throwable throwable){

        if (throwable.getMessage().contains("mobile_number")) {
            return new ErrorResponse().builder()
                    .errorCode("UNIQUE CONSTRAINT")
                    .errorMessage("This mobile number already registered")
                    .build();
        }
        if (throwable.getMessage().contains("username")) {
            return new ErrorResponse().builder()
                    .errorCode("UNIQUE CONSTRAINT")
                    .errorMessage("This username already registered")
                    .build();
        }
        if (throwable.getMessage().contains("email")) {
            return new ErrorResponse().builder()
                    .errorCode("UNIQUE CONSTRAINT")
                    .errorMessage("This email already registered")
                    .build();
        }
        return new ErrorResponse().builder()
                .errorCode("UNIQUE CONSTRAINT")
                .errorMessage("Data integrity violation")
                .build();
    }

}
