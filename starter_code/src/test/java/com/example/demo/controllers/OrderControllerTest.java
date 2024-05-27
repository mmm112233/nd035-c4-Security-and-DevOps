package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
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
import java.util.List;

import static com.example.demo.utils.Constants.*;
import static com.example.demo.utils.Constants.RESPONSE_200;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private CartController cartController;

    @Autowired
    private UserController userController;

    @Test
    public void submitResponse200() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USER_NAME);
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(10);
        cartController.addToCart(modifyCartRequest);

        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit(USER_NAME);
        Assertions.assertEquals(RESPONSE_200, orderResponseEntity.getStatusCodeValue());
    }

    @Test
    public void submitResponse403() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit(NOT_EXIST_USER_NAME);
        Assertions.assertEquals(RESPONSE_403, orderResponseEntity.getStatusCodeValue());
    }

    @Test
    public void submitResponse404() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit(USER_NAME);
        Assertions.assertEquals(RESPONSE_404, orderResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserResponse200() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USER_NAME);
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(10);
        cartController.addToCart(modifyCartRequest);

        orderController.submit(USER_NAME);
        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser(USER_NAME);
        Assertions.assertEquals(RESPONSE_200, listResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserResponse403() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser(NOT_EXIST_USER_NAME);
        Assertions.assertEquals(RESPONSE_403, listResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserResponse404() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser(USER_NAME);
        Assertions.assertEquals(RESPONSE_404, listResponseEntity.getStatusCodeValue());
    }

}