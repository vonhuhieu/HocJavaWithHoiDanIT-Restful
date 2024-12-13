package com.example.demo.Service;

import com.example.demo.Domain.DTO.Meta;
import com.example.demo.Domain.DTO.ResultPaginationDTO;
import com.example.demo.Domain.DTO.UserFormatDataResponseDTO;
import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Util.Error.ExistsByData;
import com.example.demo.Util.Error.IDInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserFormatDataResponseDTO handleCreateUser(User user) {
        User newUser = new User();
        boolean checkEmailExists = this.userRepository.existsByEmail(user.getEmail());
        if (checkEmailExists){
            throw new ExistsByData("Email is already used by other person");
        }
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hashPassword);
        newUser.setAddress(user.getAddress());
        newUser.setAge(user.getAge());
        newUser.setGender(user.getGender());
        newUser.setCreatedAt(user.getCreatedAt());
        newUser.setCreatedBy(user.getCreatedBy());
        this.userRepository.save(newUser);
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
        newUserFormatDataResponseDTO.setId(newUser.getId());
        newUserFormatDataResponseDTO.setEmail(newUser.getEmail());
        newUserFormatDataResponseDTO.setName(newUser.getName());
        newUserFormatDataResponseDTO.setGender(newUser.getGender());
        newUserFormatDataResponseDTO.setAddress(newUser.getAddress());
        newUserFormatDataResponseDTO.setAge(newUser.getAge());
        newUserFormatDataResponseDTO.setCreatedAt(newUser.getCreatedAt());
        newUserFormatDataResponseDTO.setCreatedBy(newUser.getCreatedBy());
        return newUserFormatDataResponseDTO;
    }

    public User getUserByID(long id) {
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (!fetchUserByID.isPresent()){
            throw new IDInvalidException("No exists ID " + id);
        }
        User fetchUser = fetchUserByID.get();
        return fetchUser;
    }

    public UserFormatDataResponseDTO fetchUserByID(long id){
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (!fetchUserByID.isPresent()){
            throw new IDInvalidException("No exists ID " + id);
        }
        User fetchUser = fetchUserByID.get();
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
        newUserFormatDataResponseDTO.setId(fetchUser.getId());
        newUserFormatDataResponseDTO.setName(fetchUser.getName());
        newUserFormatDataResponseDTO.setGender(fetchUser.getGender());
        newUserFormatDataResponseDTO.setAddress(fetchUser.getAddress());
        newUserFormatDataResponseDTO.setAge(fetchUser.getAge());
        newUserFormatDataResponseDTO.setCreatedAt(fetchUser.getCreatedAt());
        newUserFormatDataResponseDTO.setCreatedBy(fetchUser.getCreatedBy());
        newUserFormatDataResponseDTO.setUpdatedAt(fetchUser.getUpdatedAt());
        newUserFormatDataResponseDTO.setUpdatedBy(fetchUser.getUpdatedBy());
        return newUserFormatDataResponseDTO;
    }

    public void deleteUserByID(long id) {
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (!fetchUserByID.isPresent()){
            throw new IDInvalidException("No exists ID " + id);
        }
        this.userRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        // lấy từ pageable vì đây là 2 thông số mà frontend truyền lên
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        result.setMeta(meta);
        List<UserFormatDataResponseDTO> listUsers = new ArrayList<>();
        for (User user : pageUser.getContent()){
            UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
            newUserFormatDataResponseDTO.setId(user.getId());
            newUserFormatDataResponseDTO.setName(user.getName());
            newUserFormatDataResponseDTO.setGender(user.getGender());
            newUserFormatDataResponseDTO.setAddress(user.getAddress());
            newUserFormatDataResponseDTO.setAge(user.getAge());
            newUserFormatDataResponseDTO.setCreatedAt(user.getCreatedAt());
            newUserFormatDataResponseDTO.setCreatedBy(user.getCreatedBy());
            newUserFormatDataResponseDTO.setUpdatedAt(user.getUpdatedAt());
            newUserFormatDataResponseDTO.setUpdatedBy(user.getUpdatedBy());
            listUsers.add(newUserFormatDataResponseDTO);
        }
        result.setResult(listUsers);
        return result;
    }

    public UserFormatDataResponseDTO updateUser(User updateUser){
        User currentUser = this.getUserByID(updateUser.getId());
        if(currentUser == null){
            throw new IDInvalidException("no exists ID " + updateUser.getId());
        }
        currentUser.setName(updateUser.getName());
        currentUser.setGender(updateUser.getGender());
        currentUser.setAge(updateUser.getAge());
        currentUser.setAddress(updateUser.getAddress());
        this.userRepository.save(currentUser);
        UserFormatDataResponseDTO newUserFormatDataResponseDTO = new UserFormatDataResponseDTO();
        newUserFormatDataResponseDTO.setId(currentUser.getId());
        newUserFormatDataResponseDTO.setName(currentUser.getName());
        newUserFormatDataResponseDTO.setGender(currentUser.getGender());
        newUserFormatDataResponseDTO.setAddress(currentUser.getAddress());
        newUserFormatDataResponseDTO.setAge(currentUser.getAge());
        newUserFormatDataResponseDTO.setUpdatedAt(currentUser.getUpdatedAt());
        newUserFormatDataResponseDTO.setUpdatedBy(currentUser.getUpdatedBy());
        return newUserFormatDataResponseDTO;
    }

    public User handleGetUserByUserName(String username){
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email){
        User currentUser = this.handleGetUserByUserName(email);
        if (currentUser != null){
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
}
