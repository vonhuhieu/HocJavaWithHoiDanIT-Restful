package com.example.demo.Controller;

import com.example.demo.Domain.DTO.ResultPaginationDTO;
import com.example.demo.Domain.DTO.UserFormatDataResponseDTO;
import com.example.demo.Domain.RestResponse;
import com.example.demo.Domain.User;
import com.example.demo.Util.Error.GlobalException;
import com.example.demo.Service.UserService;
import com.example.demo.Util.ResponseUtil;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
// config version
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;
    private GlobalException globalException;
    private ResponseUtil responseUtil;

    public UserController(UserService userService, GlobalException globalException, ResponseUtil responseUtil) {
        this.userService = userService;
        this.globalException = globalException;
        this.responseUtil = responseUtil;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<RestResponse<Object>> fetchUserByID(@PathVariable("id") long id) {
        UserFormatDataResponseDTO fetchUser = this.userService.fetchUserByID(id);
        return this.responseUtil.buildSuccessResponse("Get the user successfully", fetchUser);
    }

//    @GetMapping("/users")
//    public ResponseEntity<RestResponse<ResultPaginationDTO>> fetchAllUsers(
//            @Filter Specification specification
////            @RequestParam("current") Optional<String> currentOptional,
////            @RequestParam("pageSize") Optional<String> pageSizeOptional
//
//    ) {
////        String stringCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
////        String stringPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
////        int current = Integer.parseInt(stringCurrent);
////        int pageSize = Integer.parseInt(stringPageSize);
////        Pageable pageable = PageRequest.of(current - 1, pageSize);
//        ResultPaginationDTO listUsers = this.userService.getAllUsers(specification);
//        RestResponse res = new RestResponse();
//        res.setStatusCode(HttpStatus.OK.value());
//        res.setMessage("Get users successfully");
//        res.setData(listUsers);
//        return ResponseEntity.status(HttpStatus.OK).body(res);
//    }

    @GetMapping("/users")
    public ResponseEntity<RestResponse<Object>> fetchAllUsers(
            @Filter Specification<User> specification,
            Pageable pageable
    ) {
        ResultPaginationDTO resultPaginationDTO = this.userService.getAllUsers(specification, pageable);
        return this.responseUtil.buildSuccessResponse("get users successfully", resultPaginationDTO);
    }


    @PostMapping("/users")
    public ResponseEntity<RestResponse<Object>> createNewUser(
            @RequestBody User postManUser) {
        UserFormatDataResponseDTO newUser = this.userService.handleCreateUser(postManUser);
        return this.responseUtil.buildCreateResponse("create a new user successfully", newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<RestResponse<Object>> deleteUser(@PathVariable("id") long id) {
        this.userService.deleteUserByID(id);
        return this.responseUtil.buildSuccessResponse("delete the user successfully", null);
    }

    @PutMapping("/users")
    public ResponseEntity<RestResponse<Object>> updateUser(
            @RequestBody User postManUser) {
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = this.userService.updateUser(postManUser);
        return this.responseUtil.buildSuccessResponse("update a user successfully", newUserFormatDataResponseDTO);
    }

}
