package com.forest.forest.controllers;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import com.forest.forest.models.User;
import com.forest.forest.service.UserService;
import com.forest.forest.utils.JWTUtil;

import io.jsonwebtoken.JwtException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @InjectMocks
    private AuthController authController;

    @MockBean
    private UserService userService;

    private MockHttpServletResponse mockHttpServletResponse;
    private User user;

    private String noBearerUrl = "/auth/nobearer";
    private String invalidAuthUrl = "/auth/invalid";
    private String validToken;

    @BeforeEach
    public void init(){
        mockHttpServletResponse = new MockHttpServletResponse();
        user = new User("username", "password", "user@gmail.com", "123 Test Avenue");
        validToken = "Bearer " + JWTUtil.generateUserToken(user);
    }

    @AfterEach
    public void teardown(){
        mockHttpServletResponse = null;
        user = null;
        validToken = null;
    }

    @Test
    public void testFindAllVerifiesUserToken(){
        List<User> users = new ArrayList<User>();

        users.add(user);
        users.add(user);
        users.add(user);

        when(userService.findAll()).thenReturn(users);

        try{
            assertThat(authController.findAll(mockHttpServletResponse, validToken)).isEqualTo(users);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserVerifiesUserToken(){
        User u = null;
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        try{
            u = authController.getUser(mockHttpServletResponse, validToken, user.getUsername());
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(u).isEqualTo(user);
    }

    @Test
    public void testVerifyLoginReturnsOK(){
        ResponseEntity<String> responseEntity = authController.verifyLogin(validToken);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testLoginReturnsOK(){
        when(userService.verifyUserLogin(user.getUsername(), user.getPassword())).thenReturn(user);

        ResponseEntity<String> responseEntity = authController.verifyLogin(validToken);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testFindAllWithoutBearerRedirects(){
        try{
            authController.findAll(mockHttpServletResponse, "test string");
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testFindAllInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            authController.findAll(mockHttpServletResponse, badToken);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testGetUserWithoutBearerRedirects(){
        try{
            authController.getUser(mockHttpServletResponse, "test string", user.getUsername());
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testGetUserInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            authController.getUser(mockHttpServletResponse, badToken, user.getUsername());
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testVerifyLoginWithoutBearerReturnsUnauthorized(){
        ResponseEntity<String> responseEntity = authController.verifyLogin("test string");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testVerifyLoginInvalidTokenReturnsUnauthorized(){
        ResponseEntity<String> responseEntity = authController.verifyLogin("Bearer string");
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testUpdateAddressWithoutBearerReturnsUnauthorized(){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity = authController.updateAddress(mockHttpServletResponse, "test string", user);
        } catch (IOException e){
            e.printStackTrace();
        }
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testUpdateAddressInvalidTokenReturnsUnauthorized(){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity = authController.updateAddress(mockHttpServletResponse, "Bearer string", user);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
        }
    }

    @Test
    public void testLoginUserReturnsOk(){
        when(userService.verifyUserLogin(user.getUsername(), user.getPassword())).thenReturn(user);
        ResponseEntity<String> responseEntity = authController.loginUser(user);
        

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testLoginUserReturnsBadRequest(){
        when(userService.verifyUserLogin(user.getUsername(), user.getPassword())).thenReturn(null);
        ResponseEntity<String> responseEntity = authController.loginUser(user);
        

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testRegisterUserReturnsOk(){
        when(userService.existsByUsername(user.getUsername())).thenReturn(false);
        when(userService.add(user)).thenReturn(user);
        
        ResponseEntity<String> responseEntity = authController.registerUser(user);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testRegisterUserWithoutUsernameReturnsBadRequest(){
        user.setUsername(null);
        
        ResponseEntity<String> responseEntity = authController.registerUser(user);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testRegisterUserWithoutPasswordReturnsBadRequest(){
        user.setPassword(null);
        
        ResponseEntity<String> responseEntity = authController.registerUser(user);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void testRegisterUserReturnsConflict(){
        when(userService.existsByUsername(user.getUsername())).thenReturn(true);
        
        ResponseEntity<String> responseEntity = authController.registerUser(user);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(409);
    }

    @Test
    public void testNoBearerReturnsUnauthorized(){
        ResponseEntity<String> responseEntity = authController.noBearer();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void testInvalidReturnsUnauthorized(){
        ResponseEntity<String> responseEntity = authController.invalid();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
    }
}
