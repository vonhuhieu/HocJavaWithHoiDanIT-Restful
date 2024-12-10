package com.example.demo.Service;

import com.example.demo.Domain.DTO.Meta;
import com.example.demo.Domain.DTO.ResultPaginationDTO;
import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User handleCreateUser(User user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        newUser.setPassword(hashPassword);
        this.userRepository.save(newUser);
        return newUser;
    }

    public User getUserByID(long id) {
        Optional<User> fetchUserByID = this.userRepository.findById(id);
        if (fetchUserByID.isPresent()) {
            return fetchUserByID.get();
        }
        return null;
    }

    public void deleteUserByID(long id) {
        this.userRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        result.setMeta(meta);
        result.setResult(pageUser.getContent());
        return result;
    }

    public User updateUser(User updateUser){
        User currentUser = this.getUserByID(updateUser.getId());
        if (currentUser != null){
            String hashPassword = this.passwordEncoder.encode(updateUser.getPassword());
            currentUser.setName(updateUser.getName());
            currentUser.setEmail(updateUser.getEmail());
            currentUser.setPassword(hashPassword);
            this.userRepository.save(currentUser);
            return currentUser;
        }
        return null;
    }

    public User handleGetUserByUserName(String username){
        return this.userRepository.findByEmail(username);
    }
}
