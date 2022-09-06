package com.tokenService.controller;

import com.tokenService.dao.User;
import com.tokenService.dao.UserExistsException;
import com.tokenService.dao.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class UserControllerTest {


    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserController userController;

    private User user;
    private List<User> users;


    @BeforeEach
    public void setup() {
        user = new User("testUser", "testPassword", "testRole");
        users = Collections.singletonList(user);
        ReflectionTestUtils.setField(userController, "passwordEncoder", passwordEncoder);

        when(passwordEncoder.encode(anyString())).thenReturn("testEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
    }


    @Test
    void all() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> actualResult = userController.all();
        assertEquals(actualResult, users);
    }

    @Test
    void newUser() {
        User actualResult = userController.newUser(user);
        assertEquals(actualResult, user);
    }

    @Test
    void newUserExists() {
        User actualResult = null;
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        try {
            actualResult = userController.newUser(user);
            fail();
        } catch (UserExistsException e) {
            assertNull(actualResult);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void one() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        User actualResult = userController.one(1L);
        assertEquals(actualResult, user);
    }

    @Test
    void updateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        User actualResult = userController.updateUser(user, 1L);
        assertEquals(actualResult, user);
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userController.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(anyLong());
    }
}