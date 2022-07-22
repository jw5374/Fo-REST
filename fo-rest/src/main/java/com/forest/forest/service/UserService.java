package com.forest.forest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.forest.forest.models.User;
import com.forest.forest.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        Optional<User> u = userRepository.findByUsername(username);
        if(u.isPresent()) {
            return u.get();
        }
        return null;
    }

    public User verifyUserLogin(String username, String password) {
        Optional<User> u = userRepository.findByUsername(username);
        if(!u.isPresent()) {
            return null;
        }
        if(passwordEncoder.matches(password, u.get().getPassword())) {
            return u.get();
        }
        return null;
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public User add(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
