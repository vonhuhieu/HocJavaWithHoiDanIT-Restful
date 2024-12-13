package com.example.demo.Config;

import com.example.demo.Service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final UserService userService;

    public UserDetailsCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    // loadUserByUsername => query user trong DB, rồi mới laays ra thong tin
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.demo.Domain.User user = this.userService.handleGetUserByUserName(username);
        if(user == null){
            throw new UsernameNotFoundException("không tìm thấy user");
        }
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
