package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;

import static com.example.demo.utils.Constants.*;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    public void createUserResponse200() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        Assertions.assertEquals(RESPONSE_200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(USER_NAME, Objects.requireNonNull(responseEntity.getBody()).getUsername());
    }

    @Test
    public void createUserResponse400() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(INVALID_PASSWORD);
        createUserRequest.setConfirmPassword(INVALID_PASSWORD);
        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);
        Assertions.assertEquals(RESPONSE_400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findUserByIdResponse200() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        ResponseEntity<User> createdUserResponse = userController.createUser(createUserRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<User> responseEntity = userController.findById(Objects.requireNonNull(createdUserResponse.getBody()).getId());
        Assertions.assertEquals(RESPONSE_200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(USER_NAME, Objects.requireNonNull(responseEntity.getBody()).getUsername());
    }

    @Test
    public void findUserByIdResponse404() {
        ResponseEntity<User> responseEntity = userController.findById(-1L);
        Assertions.assertEquals(RESPONSE_404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findUserByIdResponse403() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        ResponseEntity<User> createdUserResponse = userController.createUser(createUserRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(NOT_EXIST_USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<User> responseEntity = userController.findById(Objects.requireNonNull(createdUserResponse.getBody()).getId());
        Assertions.assertEquals(RESPONSE_403, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findByUsernameResponse200() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<User> responseEntity = userController.findByUserName(USER_NAME);
        Assertions.assertEquals(RESPONSE_200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals(USER_NAME, Objects.requireNonNull(responseEntity.getBody()).getUsername());
    }

    @Test
    public void findByUsernameResponse404() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(NOT_EXIST_USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<User> responseEntity = userController.findByUserName(NOT_EXIST_USER_NAME);
        Assertions.assertEquals(RESPONSE_404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void findByUsernameResponse403() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(NOT_EXIST_USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<User> responseEntity = userController.findByUserName(USER_NAME);
        Assertions.assertEquals(RESPONSE_403, responseEntity.getStatusCodeValue());
    }

}