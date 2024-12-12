package com.example.demo.Controller;

import com.example.demo.Domain.DTO.LoginDTO;
import com.example.demo.Domain.DTO.UserDataLoginSuccessfullyDTO;
import com.example.demo.Domain.DTO.UserDataResponseLoginSuccessfullyDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;
import com.example.demo.Util.ResponseUtil;
import com.example.demo.Util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.HashMap;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final ResponseUtil responseUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserRepository userRepository, ResponseUtil responseUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.responseUtil = responseUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

//xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create a token
        String access_token = this.securityUtil.createToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String emailUser = authentication.getName();
        long idUser = this.userRepository.findByEmail(emailUser).getId();
        String nameUser = this.userRepository.findByEmail(emailUser).getName();
        UserDataLoginSuccessfullyDTO userDataLoginSuccessfullyDTO = new UserDataLoginSuccessfullyDTO();
        userDataLoginSuccessfullyDTO.setId(idUser);
        userDataLoginSuccessfullyDTO.setEmail(emailUser);
        userDataLoginSuccessfullyDTO.setName(nameUser);
        UserDataResponseLoginSuccessfullyDTO userDataResponseLoginSuccessfullyDTO = new UserDataResponseLoginSuccessfullyDTO();
        userDataResponseLoginSuccessfullyDTO.setAccessToken(access_token);
        userDataResponseLoginSuccessfullyDTO.setUserDataLoginSuccessfullyDTO(userDataLoginSuccessfullyDTO);
        return this.responseUtil.buildSuccessResponse("Login successfully", userDataResponseLoginSuccessfullyDTO);
    }
}
