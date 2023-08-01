package com.example.techversantInfotech.Authservice.Exception;

import com.example.techversantInfotech.Authservice.Dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResponseEntityExceptionHandler extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse>  handleAuthServiceException(UserNotFoundException userNotFoundException){
        return  new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(userNotFoundException.getMessage())
                .errorCode(userNotFoundException.getErrorCode())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegistered.class)
    public ResponseEntity<ErrorResponse> handlesUserRegisteredException(UserAlreadyRegistered userAlreadyRegistered){
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(userAlreadyRegistered.getErrorCode())
                .errorMessage(userAlreadyRegistered.getMessage())
                .build(), HttpStatus.CONFLICT);
    }
}
