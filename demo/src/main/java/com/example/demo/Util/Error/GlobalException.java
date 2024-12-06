package com.example.demo.Util.Error;

import com.example.demo.Domain.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Invalid email or password");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());
        ArrayList<String> listErrors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            listErrors.add(error.getDefaultMessage());
        }
        res.setMessage(listErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

//    @ExceptionHandler(value = IDInvalidException.class)
//    public ResponseEntity<RestResponse<Object>> handleIdException(IDInvalidException idInvalidException) {
//        RestResponse res = new RestResponse();
//        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        res.setError(idInvalidException.getMessage());
//        res.setMessage("Vui lòng kiểm tra lại id");
//        return ResponseEntity.badRequest().body(res);
//    }


}
