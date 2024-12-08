package com.example.demo.Util;

import com.example.demo.Domain.DTO.Meta;
import com.example.demo.Domain.DTO.ResultPaginationDTO;
import com.example.demo.Domain.RestResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
    public ResponseEntity<RestResponse<Object>> buildSuccessResponse(String message, Object data){
        RestResponse res = new RestResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage(message);
        res.setData(data);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    public ResponseEntity<RestResponse<Object>> buildCreateResponse(String message, Object data){
        RestResponse res = new RestResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage(message);
        res.setData(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}

