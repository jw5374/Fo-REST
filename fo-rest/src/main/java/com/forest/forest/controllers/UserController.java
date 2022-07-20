package com.forest.forest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.User;
import com.forest.forest.repository.UserRepository;

import java.util.List;

@RestController
@Validated
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping("/users")
    public List<User> findAll() {
        return repository.findAll();
    }
    /* 
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginDto loginDto){
		
            Authentication authObject = null;
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authObject);
            } catch (BadCredentialsException e) {
                return new ResponseEntity<>("Credentials are invalid.", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("User logged in successfully!", HttpStatus.OK);
    }*/

}
