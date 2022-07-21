package com.forest.forest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.forest.models.User;
import com.forest.forest.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public User add(User user){
        return userRepository.save(user);
    }
}
