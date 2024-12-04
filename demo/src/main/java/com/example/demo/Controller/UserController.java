package com.example.demo.Controller;

import com.example.demo.Domain.User;
import com.example.demo.Service.Error.IDInvalidException;
import com.example.demo.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> fetchUserByID(@PathVariable("id") long id){
        User fetchUser = this.userService.getUserByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(
            @RequestBody User postManUser) {
        User user = new User();
        user = this.userService.handleCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IDInvalidException {
        if(id >= 1500){
            throw new IDInvalidException("Id khong lon hon 1500");
        }
        User deleteUser = this.userService.getUserByID(id);
        if (deleteUser != null) {
            this.userService.deleteUserByID(id);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete users successfully");
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(
            @RequestBody User postManUser){
        User updateUser = this.userService.updateUser(postManUser);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

}
