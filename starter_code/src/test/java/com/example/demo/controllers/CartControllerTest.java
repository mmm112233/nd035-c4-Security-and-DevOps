package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;

import java.util.Collections;

import static com.example.demo.utils.Constants.*;

@Transactional
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private CartController cartController;

    @Test
    void addToCartResponse200() {
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
        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);

        Assertions.assertEquals(RESPONSE_200, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void addToCartResponse403() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(NOT_EXIST_USER_NAME);
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);

        Assertions.assertEquals(RESPONSE_403, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void addToCartResponse404NotFoundItem() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        userController.createUser(createUserRequest);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USER_NAME);
        modifyCartRequest.setItemId(-1);
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);

        Assertions.assertEquals(RESPONSE_404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void addToCartResponse404UserNotFound() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(USER_NAME);
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(10);
        ResponseEntity<Cart> cartResponseEntity = cartController.addToCart(modifyCartRequest);

        Assertions.assertEquals(RESPONSE_404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void removeFromCartResponse200() {
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

        ModifyCartRequest modifyCartRequestUpdate = new ModifyCartRequest();
        modifyCartRequestUpdate.setUsername(USER_NAME);
        modifyCartRequestUpdate.setItemId(1);
        modifyCartRequestUpdate.setQuantity(20);
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequestUpdate);

        Assertions.assertEquals(RESPONSE_200, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void removeFromCartResponse403() {
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

        ModifyCartRequest modifyCartRequestUpdate = new ModifyCartRequest();
        modifyCartRequestUpdate.setUsername(NOT_EXIST_USER_NAME);
        modifyCartRequestUpdate.setItemId(1);
        modifyCartRequestUpdate.setQuantity(20);
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequestUpdate);

        Assertions.assertEquals(RESPONSE_403, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void removeFromCartResponse404UserNotFound() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(USER_NAME, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ModifyCartRequest modifyCartRequestUpdate = new ModifyCartRequest();
        modifyCartRequestUpdate.setUsername(USER_NAME);
        modifyCartRequestUpdate.setItemId(1);
        modifyCartRequestUpdate.setQuantity(5);
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequestUpdate);

        Assertions.assertEquals(RESPONSE_404, cartResponseEntity.getStatusCodeValue());
    }

    @Test
    void removeFromCartResponse404ItemNotFound() {
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

        ModifyCartRequest modifyCartRequestUpdate = new ModifyCartRequest();
        modifyCartRequestUpdate.setUsername(USER_NAME);
        modifyCartRequestUpdate.setItemId(-1);
        modifyCartRequestUpdate.setQuantity(20);
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromCart(modifyCartRequestUpdate);

        Assertions.assertEquals(RESPONSE_404, cartResponseEntity.getStatusCodeValue());
    }
}