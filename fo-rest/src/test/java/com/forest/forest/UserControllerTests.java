package com.forest.forest;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.forest.forest.controllers.AuthController;
import com.forest.forest.models.User;
import com.forest.forest.service.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @InjectMocks
    AuthController userController;

    @MockBean
    private UserService userService;

    @Test
    public void testAddUserReturns200(){
        User alex = new User("alex", "hunter2", "alex@gmail.com", "123 Test Avenue");

        when(userService.existsByUsername(alex.getUsername())).thenReturn(false);
        when(userService.add(alex)).thenReturn(alex);
        
        ResponseEntity<String> responseEntity = userController.registerUser(alex);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
}
