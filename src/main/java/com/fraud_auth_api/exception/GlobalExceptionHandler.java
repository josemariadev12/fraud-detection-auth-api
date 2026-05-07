package com.fraud_auth_api.exception;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError buildError(Exception ex,HttpStatus httpStatus,HttpServletRequest request){
        ApiError error = new ApiError();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(httpStatus.value());
        error.setError(ex.getMessage());
        error.setPath(request.getRequestURI());
        return error;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handlerIllegalArgument(IllegalArgumentException ex, HttpServletRequest request){
        
        ApiError error = buildError(ex, HttpStatus.BAD_REQUEST, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<ApiError> handlerValidation(MethodArgumentNotValidException ex, HttpServletRequest request){

        //validação ternaria ( ? = if e : = else)
        String message = ex.getBindingResult().getFieldError() != null?
         ex.getBindingResult()
        .getFieldError()
        .getDefaultMessage(): "Validation Error";

        ApiError error = new ApiError();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setError(message);
        error.setPath(request.getRequestURI());
        

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }




}
