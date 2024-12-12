package com.example.demo.Domain.DTO;

import org.springframework.stereotype.Component;

@Component
public class UserDataResponseLoginSuccessfullyDTO {
    private String accessToken;
    private UserDataLoginSuccessfullyDTO userDataLoginSuccessfullyDTO;

    public UserDataLoginSuccessfullyDTO getUserDataLoginSuccessfullyDTO() {
        return userDataLoginSuccessfullyDTO;
    }

    public void setUserDataLoginSuccessfullyDTO(UserDataLoginSuccessfullyDTO userDataLoginSuccessfullyDTO) {
        this.userDataLoginSuccessfullyDTO = userDataLoginSuccessfullyDTO;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
