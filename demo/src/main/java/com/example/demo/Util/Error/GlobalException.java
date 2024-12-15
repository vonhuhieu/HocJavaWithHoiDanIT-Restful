package com.example.demo.Util.Error;

import com.example.demo.Domain.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleGeneralException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError("URL Error");
        res.setMessage("Something went wrong. Please check URL or your data and try again.");
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

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

    @ExceptionHandler(IDInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleInvalidIDException(IDInvalidException ex){
        RestResponse res = new RestResponse();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid ID");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(ExistsByData.class)
    public ResponseEntity<RestResponse<Object>> handleExistsByData(ExistsByData ex){
        RestResponse res = new RestResponse();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Data already exists");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(StorageException ex){
        RestResponse res = new RestResponse();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Upload File Error");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
