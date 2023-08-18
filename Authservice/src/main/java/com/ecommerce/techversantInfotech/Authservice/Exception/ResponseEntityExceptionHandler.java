package com.ecommerce.techversantInfotech.Authservice.Exception;

import com.ecommerce.techversantInfotech.Authservice.Dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable rootCause = ex.getRootCause();
        return new ResponseEntity(CustomConstraintException.customConstraintResponse(rootCause),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<ErrorResponse> handleIOException(ImageProcessingException ex) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CompletableFutureException.class)
    public ResponseEntity<ErrorResponse> handleCompletableFuture(CompletableFutureException ex){
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);

    }


}
