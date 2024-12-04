package com.example.demo.Controller;

import com.example.demo.Domain.User;
import com.example.demo.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public User fetchUserByID(@PathVariable("id") long id){
        User fetchUser = this.userService.getUserByID(id);
        return fetchUser;
    }

    @GetMapping("/user")
    public List<User> fetchAllUsers(){
        return this.userService.getAllUsers();
    }

    @PostMapping("/user")
    public User createNewUser(
            @RequestBody User postManUser) {
        User user = new User();
        user = this.userService.handleCreateUser(postManUser);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        User deleteUser = this.userService.getUserByID(id);
        if (deleteUser != null) {
            this.userService.deleteUserByID(id);
        }
        return "xóa thành công";
    }

    @PutMapping("/user")
    public User updateUser(
            @RequestBody User postManUser){
        User updateUser = this.userService.updateUser(postManUser);
        return updateUser;
    }
}
