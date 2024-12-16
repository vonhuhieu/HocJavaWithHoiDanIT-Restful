package com.example.demo.Controller;

import com.example.demo.Domain.DTO.Request.RequestLoginDTO;
import com.example.demo.Domain.DTO.Response.User.UserDataLoginSuccessfullyDTO;
import com.example.demo.Domain.DTO.Response.User.UserDataResponseLoginSuccessfullyDTO;
import com.example.demo.Domain.DTO.Response.User.UserFormatJWTCreateDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.Role;
import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.RoleService;
import com.example.demo.Service.UserService;
import com.example.demo.Util.Error.IDInvalidException;
import com.example.demo.Util.ResponseUtil;
import com.example.demo.Util.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final ResponseUtil responseUtil;
    private final UserService userService;
    private final RoleService roleService;
    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserRepository userRepository, ResponseUtil responseUtil, UserService userService, RoleService roleService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.responseUtil = responseUtil;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<RestResponse<Object>> login(@Valid @RequestBody RequestLoginDTO loginDTO) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

//xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create a token

        // set thong tin nguoi dung login vao context (co the su dung sau nay)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String emailUser = authentication.getName();
        User userInfo = this.userRepository.findByEmail(emailUser);
        UserDataLoginSuccessfullyDTO userDataLoginSuccessfullyDTO = new UserDataLoginSuccessfullyDTO();
        userDataLoginSuccessfullyDTO.setId(userInfo.getId());
        userDataLoginSuccessfullyDTO.setEmail(userInfo.getEmail());
        userDataLoginSuccessfullyDTO.setName(userInfo.getName());
        userDataLoginSuccessfullyDTO.setRole(userInfo.getRole());
        UserDataResponseLoginSuccessfullyDTO userDataResponseLoginSuccessfullyDTO = new UserDataResponseLoginSuccessfullyDTO();
        UserFormatJWTCreateDTO userFormatJWTCreateDTO = new UserFormatJWTCreateDTO();
        userFormatJWTCreateDTO.setId(userInfo.getId());
        userFormatJWTCreateDTO.setEmail(userInfo.getEmail());
        userFormatJWTCreateDTO.setName(userInfo.getName());
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), userFormatJWTCreateDTO);
        userDataResponseLoginSuccessfullyDTO.setAccessToken(access_token);
        userDataResponseLoginSuccessfullyDTO.setUserDataLoginSuccessfullyDTO(userDataLoginSuccessfullyDTO);

        // create refreshToken
        String refreshToken = this.securityUtil.createRefreshToken(emailUser, userFormatJWTCreateDTO);

        // update user
        this.userService.updateUserToken(refreshToken, emailUser);

        // set cookies
        ResponseCookie responseCookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        // set lai response
        RestResponse newRes = new RestResponse();
        newRes.setStatusCode(HttpStatus.OK.value());
        newRes.setMessage("login successfully");
        newRes.setData(userDataResponseLoginSuccessfullyDTO);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(newRes);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<RestResponse<Object>>  getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userRepository.findByEmail(email);
        UserDataLoginSuccessfullyDTO userDataLoginSuccessfullyDTO = new UserDataLoginSuccessfullyDTO();
        userDataLoginSuccessfullyDTO.setId(currentUser.getId());
        userDataLoginSuccessfullyDTO.setEmail(currentUser.getEmail());
        userDataLoginSuccessfullyDTO.setName(currentUser.getName());
        UserDataLoginSuccessfullyDTO.userGetAccount userLogin = new UserDataLoginSuccessfullyDTO.userGetAccount();
        userLogin.setUser(userDataLoginSuccessfullyDTO);
        return this.responseUtil.buildSuccessResponse("get account successfully", userLogin);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<RestResponse<Object>> getRefreshToken(
            @CookieValue(name = "refreshToken", defaultValue = "errorToken") String refreshToken
    ){
        if (refreshToken.equals("errorToken")){

            throw new IDInvalidException("No refreshToken in cookie");
        }
        // check valid
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null){
            throw new IDInvalidException("Refresh Token khong hop le");
        }
        UserDataLoginSuccessfullyDTO userDataLoginSuccessfullyDTO = new UserDataLoginSuccessfullyDTO();
        userDataLoginSuccessfullyDTO.setId(currentUser.getId());
        userDataLoginSuccessfullyDTO.setEmail(currentUser.getEmail());
        userDataLoginSuccessfullyDTO.setName(currentUser.getName());
        UserFormatJWTCreateDTO userFormatJWTCreateDTO = new UserFormatJWTCreateDTO();
        userFormatJWTCreateDTO.setId(currentUser.getId());
        userFormatJWTCreateDTO.setName(currentUser.getName());
        userFormatJWTCreateDTO.setEmail(currentUser.getEmail());
        String access_token = this.securityUtil.createAccessToken(email, userFormatJWTCreateDTO);
        // create refreshToken
        String newRefreshToken = this.securityUtil.createRefreshToken(email, userFormatJWTCreateDTO);

        // update user
        this.userService.updateUserToken(newRefreshToken, email);

        // set cookies
        ResponseCookie responseCookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        // set lai response
        RestResponse newRes = new RestResponse();
        newRes.setStatusCode(HttpStatus.OK.value());
        newRes.setMessage("Get user by refreshToken successfully");
        UserDataLoginSuccessfullyDTO.userGetAccount userLogin = new UserDataLoginSuccessfullyDTO.userGetAccount();
        userLogin.setUser(userDataLoginSuccessfullyDTO);
        UserDataResponseLoginSuccessfullyDTO userDataResponseLoginSuccessfullyDTO = new UserDataResponseLoginSuccessfullyDTO();
        userDataResponseLoginSuccessfullyDTO.setAccessToken(access_token);
        userDataResponseLoginSuccessfullyDTO.setUserDataLoginSuccessfullyDTO(userDataLoginSuccessfullyDTO);
        newRes.setData(userDataResponseLoginSuccessfullyDTO);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(newRes);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<RestResponse<Object>> userLogOut(HttpServletResponse response){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUser = this.userRepository.findByEmail(email);
        if (currentUser == null){
            throw new IDInvalidException("No user exists");
        }
        this.userService.updateUserToken(null, currentUser.getEmail());
        Cookie deleteServletCookie = new Cookie("refreshToken", null);
        deleteServletCookie.setMaxAge(0);
        deleteServletCookie.setPath("/");
        deleteServletCookie.setHttpOnly(true);
        deleteServletCookie.setSecure(true);
        response.addCookie(deleteServletCookie);
        return this.responseUtil.buildSuccessResponse("logout successfully", null);
    }
}
