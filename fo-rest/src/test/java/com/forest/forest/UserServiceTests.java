package com.forest.forest;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.forest.forest.models.User;
import com.forest.forest.repository.UserRepository;
import com.forest.forest.service.UserService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private User user;
    private String username = null;
    private String password = null;

    @BeforeEach
    public void init(){
        user = new User("username", "password", "user@gmail.com", "123 Test Avenue");
        username = user.getUsername();
        password = user.getPassword();
    }

    @AfterEach
    public void teardown(){
        user = null;
        username = null;
        password = null;
    }

    @Test
    public void testFindAllReturnsUserList(){
        List<User> users = new ArrayList<User>();

        users.add(new User("alex", "hunter2", "alex@gmail.com", "123 Test Avenue"));
        users.add(new User("alex1", "hunter3", "alex1@gmail.com", "345 Test Avenue"));
        users.add(new User("alex2", "hunter4", "alex2@gmail.com", "567 Test Avenue"));

        when(userRepository.findAll()).thenReturn(users);
        
        assertThat(userService.findAll()).isEqualTo(users);
    }

    @Test
    public void testFindByUsernameReturnsUser(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThat(userService.findByUsername(username)).isEqualTo(user);
    }

    @Test
    public void testFindByUsernameReturnsNullIfNotFound(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(null));

        assertNull(userService.findByUsername(username));
    }

    @Test
    public void testVerifyUserLoginReturnsUser(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, password)).thenReturn(true);

        assertThat(userService.verifyUserLogin(username, password)).isEqualTo(user);
    }

    @Test
    public void testVerifyUserLoginReturnsNullIfUserNotFound(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(null));

        assertNull(userService.verifyUserLogin(username, password));
    }

    @Test
    public void testVerifyUserLoginReturnsNullIfPasswordMismatches(){
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, password)).thenReturn(false);

        assertNull(userService.verifyUserLogin(username, password));
    }

    @Test
    public void testExistsByUsernameReturnsTrue(){
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertTrue(userService.existsByUsername(username));
    }

    @Test
    public void testExistsByUsernameReturnsFalse(){
        when(userRepository.existsByUsername(username)).thenReturn(false);

        assertFalse(userService.existsByUsername(username));
    }

    @Test
    public void testAddUserReturnsUserWithEncodedPassword(){
        String testPassword = "test";
        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setPassword(testPassword);
        newUser.setEmail(user.getEmail());
        newUser.setShippingAddress(user.getShippingAddress());

        when(passwordEncoder.encode(password)).thenReturn(testPassword);
        when(userRepository.save(user)).thenReturn(newUser);

        assertThat(userService.add(user)).isEqualTo(newUser);
    }

    @Test
    public void testUpdateUserAddressReturnsAddress(){
        when(userRepository.updateUserAddress(user.getShippingAddress(), username)).thenReturn(user.getShippingAddress());

        assertThat(userService.updateUserAddress(user.getShippingAddress(), username)).isEqualTo(user.getShippingAddress());
    }
}