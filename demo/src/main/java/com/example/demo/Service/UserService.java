package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
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

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
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
