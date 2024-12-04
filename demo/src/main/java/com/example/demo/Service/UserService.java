package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        User newUser = this.userRepository.save(user);
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
            currentUser.setName(updateUser.getName());
            currentUser.setEmail(updateUser.getEmail());
            currentUser.setPassword(updateUser.getPassword());
            this.handleCreateUser(currentUser);
        }
        return currentUser;
    }
}
