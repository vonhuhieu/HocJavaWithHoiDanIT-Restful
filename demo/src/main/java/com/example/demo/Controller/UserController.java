package com.example.demo.Controller;

import com.example.demo.Domain.User;
import com.example.demo.Service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/user/create")
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
}
