package com.forest.forest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.User;
import com.forest.forest.service.UserService;
import com.forest.forest.utils.JWTUtil;

import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/users")
    public List<User> findAll(HttpServletResponse res, @RequestHeader("Authorization") String authToken) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            JWTUtil.verifyUserToken(token[1]);
            return userService.findAll();
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/users/{username}")
    public User getUser(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("username") String user) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            String uname = JWTUtil.verifyUserToken(token[1]);
            if(!uname.equals(user)) {
                throw new JwtException("Username doesn't match");
            }
            return userService.findByUsername(user);
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return null;
        }
    }

    @GetMapping("/user")
    public ResponseEntity<String> verifyLogin(@RequestHeader(value = "Authorization") String authToken) {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            return new ResponseEntity<>("No Bearer Token", HttpStatus.UNAUTHORIZED);
        }
        try {
            return new ResponseEntity<>(JWTUtil.verifyUserToken(token[1]) + " valid", HttpStatus.OK);
        } catch(JwtException e) {
            return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            User login = userService.verifyUserLogin(user.getUsername(), user.getPassword());
            String userToken = JWTUtil.generateUserToken(login);
            String res = String.format("{ \"username\": \"%s\", \"token\": \"%s\"}", login.getUsername(), userToken);
            
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(String.format("{ \"error\": \"%s\"}", e.toString()), HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/nobearer")
    public ResponseEntity<String> noBearer() {
        return new ResponseEntity<>("No Bearer Token", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/invalid")
    public ResponseEntity<String> invalid() {
        return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
    }


}
