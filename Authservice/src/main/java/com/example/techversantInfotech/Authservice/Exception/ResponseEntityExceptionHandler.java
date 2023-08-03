package com.example.techversantInfotech.Authservice.Exception;

import com.example.techversantInfotech.Authservice.Dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class ResponseEntityExceptionHandler {
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


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errorMap.put(field, message);
        }
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ErrorResponse> handleIOException(ImageProcessingException ex) {
        // Custom logic to handle the exception
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
