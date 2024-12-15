package com.example.demo.Domain.DTO.Response.User;

import org.springframework.stereotype.Component;

@Component
public class UserDataLoginSuccessfullyDTO {
    private long id;
    private String email;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class userGetAccount{
        private UserDataLoginSuccessfullyDTO user;

        public UserDataLoginSuccessfullyDTO getUser() {
            return user;
        }

        public void setUser(UserDataLoginSuccessfullyDTO user) {
            this.user = user;
        }
    }
}
