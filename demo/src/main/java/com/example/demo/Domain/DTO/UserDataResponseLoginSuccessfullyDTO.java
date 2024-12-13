package com.example.demo.Domain.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class UserDataResponseLoginSuccessfullyDTO {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("user")
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
