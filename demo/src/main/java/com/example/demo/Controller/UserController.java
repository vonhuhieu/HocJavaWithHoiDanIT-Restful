package com.example.demo.Controller;

import com.example.demo.Domain.DTO.ResultPaginationDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.User;
import com.example.demo.Util.Error.GlobalException;
import com.example.demo.Service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private UserService userService;
    private GlobalException globalException;

    public UserController(UserService userService, GlobalException globalException) {
        this.userService = userService;
        this.globalException = globalException;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<RestResponse<Object>> fetchUserByID(@PathVariable("id") long id) {
        User fetchUser = this.userService.getUserByID(id);
        RestResponse res = new RestResponse();
        if (fetchUser != null) {
            HashMap<String, String> dataUser = new HashMap<>();
            dataUser.put("id", Long.toString(fetchUser.getId()));
            dataUser.put("email", fetchUser.getEmail());
            dataUser.put("name", fetchUser.getName());
            res.setStatusCode(HttpStatus.OK.value());
            res.setData(dataUser);
            res.setMessage("Tìm user thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid ID");
        res.setMessage("ID không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @GetMapping("/users")
    public ResponseEntity<RestResponse<ResultPaginationDTO>> fetchAllUsers(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional

    ) {
        String stringCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String stringPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        int current = Integer.parseInt(stringCurrent);
        int pageSize = Integer.parseInt(stringPageSize);
        Pageable pageable = PageRequest.of(current - 1, pageSize);
        ResultPaginationDTO listUsers = this.userService.getAllUsers(pageable);
        RestResponse res = new RestResponse();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Get users successfully");
        res.setData(listUsers);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @PostMapping("/users")
    public ResponseEntity<RestResponse<Object>> createNewUser(
            @RequestBody User postManUser) {
        User user = new User();
        user = this.userService.handleCreateUser(postManUser);
        RestResponse res = new RestResponse();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Create a user succesfully");
        HashMap<String, String> dataUser = new HashMap<>();
        dataUser.put("id", Long.toString(user.getId()));
        dataUser.put("email", user.getEmail());
        dataUser.put("name", user.getName());
        res.setData(dataUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
        User deleteUser = this.userService.getUserByID(id);
        RestResponse res = new RestResponse();
        if (deleteUser != null) {
            this.userService.deleteUserByID(id);
            res.setStatusCode(HttpStatus.OK.value());
            res.setMessage("Xoa nguoi dung thanh cong");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid ID");
        res.setMessage("ID không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @PutMapping("/users")
    public ResponseEntity<RestResponse<Object>> updateUser(
            @RequestBody User postManUser) {
        RestResponse res = new RestResponse();
        User updateUser = this.userService.updateUser(postManUser);
        if (updateUser != null) {
            HashMap<String, String> dataUser = new HashMap<>();
            dataUser.put("id", Long.toString(updateUser.getId()));
            dataUser.put("email", updateUser.getEmail());
            dataUser.put("name", updateUser.getName());
            res.setStatusCode(HttpStatus.OK.value());
            res.setData(dataUser);
            res.setMessage("Tìm user thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Invalid ID");
        res.setMessage("ID không tồn tại");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}
