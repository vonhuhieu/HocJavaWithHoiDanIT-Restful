package com.example.demo.Service;

import com.example.demo.Domain.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user){
        User newUser = this.userRepository.save(user);
        return newUser;
    }

    public User getUserByID(long id){
        User getUser = this.userRepository.getReferenceById(id);
        return getUser;
    }
    public void deleteUserByID(long id){
        this.userRepository.deleteById(id);
    }
}
