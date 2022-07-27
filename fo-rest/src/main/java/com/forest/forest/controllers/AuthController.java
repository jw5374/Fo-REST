package com.forest.forest.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static Logger log = LogManager.getLogger(AuthController.class);
	
    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping("/users")
    public List<User> findAll(HttpServletResponse res, @RequestHeader("Authorization") String authToken) throws IOException {
        log.info("Attempting to Fetch all Users");
    	String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Fetch all Users: Found no beaarer");
            res.sendRedirect("/auth/nobearer");
        }
        else {
            try {
                log.info("Attempting to Fetch all Users: Verifying User token");
                JWTUtil.verifyUserToken(token[1]);
                log.info("Attempting to Fetch all Users: Successfully verified user token");
                log.info("Attempting to Fetch all Users: Returning List of Users");
                return userService.findAll();
            } catch(JwtException | ArrayIndexOutOfBoundsException e) {
                log.warn("Attempting to Fetch all Users: Caught JwtException or ArrayIndexOutofBoundsException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.warn("Attempting to Fetch all Users: Failure, returning null");
        return null;
    }

    @ResponseBody
    @GetMapping("/users/{username}")
    public User getUser(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("username") String user) throws IOException {
        log.info("Attempting to Fetch User by username ("+user+")");
    	String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Fetch User by username ("+user+"): Found no Bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Fetch User by username ("+user+"): Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Fetch User by username ("+user+"): Successfully verified user token as uname");
                if(!uname.equals(user)) {
                    log.info("Fetch User by username ("+user+"): uname != username");
                    throw new JwtException("Username doesn't match");
                }
                log.info("Fetch User by username ("+user+"): Returning User");
                return userService.findByUsername(user);
            } catch(JwtException | ArrayIndexOutOfBoundsException e) {
                log.warn("Fetch User by username ("+user+"): Caught JwtException or ArrayIndexOutofBoundsException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.warn("Fetch User by username ("+user+"): Failure, Returning null");
        return null;
    }

    @GetMapping("/user")
    public ResponseEntity<String> verifyLogin(@RequestHeader(value = "Authorization") String authToken) {
        log.info("Attempting to Verify User");
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Verify User: Found no Bearer");
            return new ResponseEntity<>("No Bearer Token", HttpStatus.UNAUTHORIZED);
        }
        try {
            log.info("Verify User: Return valid");
            return new ResponseEntity<>(JWTUtil.verifyUserToken(token[1]) + " valid", HttpStatus.OK);
        } catch(JwtException e) {
            log.warn("Verify User: Return invalid");
            return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<String> updateAddress(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @RequestBody User user) throws IOException {
        log.info("Attempting to Update Address by User ("+user.getUsername()+")");
    	String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Update Address by User ("+user.getUsername()+"): Found no bearer");
            return new ResponseEntity<>("No Bearer Token", HttpStatus.UNAUTHORIZED);
        }
        try {
            log.info("Update Address by User ("+user.getUsername()+"): Verifying user token");
            String uname = JWTUtil.verifyUserToken(token[1]);
            log.info("Update Address by User ("+user.getUsername()+"): Successfully verified user token as uname");
            if(!uname.equals(user.getUsername())) {
                log.warn("Update Address by User ("+user.getUsername()+"): uname != username");
                throw new JwtException("Username doesn't match");
            }
            log.info("Update Address by User ("+user.getUsername()+"): Return valid");
            return new ResponseEntity<>(userService.updateUserAddress(user.getShippingAddress(), user.getUsername()), HttpStatus.OK);
        } catch(JwtException e) {
            log.warn("Update Address by User ("+user.getUsername()+"): Caught JwTException, Return Token invalid");
            return new ResponseEntity<>("Token invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            log.info("Attempting to Login a User ("+user.getUsername()+")");
            log.info("Login a User ("+user.getUsername()+"): Verifying username and password combination");
            User login = userService.verifyUserLogin(user.getUsername(), user.getPassword());
            log.info("Login a User ("+user.getUsername()+"): Successfully verified login info as login");
            log.info("Login a User ("+user.getUsername()+"): Generating User token");
            String userToken = JWTUtil.generateUserToken(login);
            log.info("Login a User ("+user.getUsername()+"): Generating Response");
            String res = String.format("{ \"username\": \"%s\", \"token\": \"%s\"}", login.getUsername(), userToken);
            log.info("Login a User ("+user.getUsername()+"): Returning Response Entity");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            log.warn("Login a User ("+user.getUsername()+"): Returning Error Response Entity");
            return new ResponseEntity<>(String.format("{ \"error\": \"%s\"}", e.toString()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        log.info("Attempting to Register a User ("+user.getUsername()+")");
        if(user.getPassword() == null || user.getUsername()  == null)
        {
            log.info("Register a User ("+user.getUsername()+"): Failure, Returning invalid, password and/or username is null");
            return new ResponseEntity<>("Invalid credentials entered.", HttpStatus.BAD_REQUEST);
        }
        else if(userService.existsByUsername(user.getUsername()))
        {
            log.info("Register a User ("+user.getUsername()+"): Failure, Returning invlaid, Username already exists");
            return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT); 
        }
        log.info("Register a User ("+user.getUsername()+"): Adding User Entry");
        userService.add(user);
        log.info("Register a User ("+user.getUsername()+"): Returning valid");
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
