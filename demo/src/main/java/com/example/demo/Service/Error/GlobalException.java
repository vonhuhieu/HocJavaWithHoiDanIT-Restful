package com.example.demo.Service.Error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IDInvalidException.class)
    public ResponseEntity<String> handleIDException(IDInvalidException idInvalidException){
        return ResponseEntity.badRequest().body(idInvalidException.getMessage());
    }
}
