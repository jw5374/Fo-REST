package com.forest.forest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.User;
import com.forest.forest.service.UserService;

import java.util.List;

@CrossOrigin
@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        if(user.getPassword() == null || user.getUsername()  == null)
            return new ResponseEntity<>("Invalid credentials entered.", HttpStatus.BAD_REQUEST);
        else if(userService.existsByUsername(user.getUsername()))
            return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT); 

        userService.add(user);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

}
