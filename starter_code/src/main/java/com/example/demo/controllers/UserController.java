package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.demo.utils.Constants.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        logger.info("{} : {}", GET_USER_BY_ID, id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            logger.error("{} : {}", GET_USER_BY_ID, NOTFOUND);
            return ResponseEntity.notFound().build();
        }
        if (!Authorization.isRightAccount(user.get().getUsername())) {
            logger.error("{} : {}", GET_USER_BY_ID, FORBIDDEN);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        logger.info("{} : {}", GET_USER_BY_ID, SUCCESS);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        logger.info("{} : {}", GET_USER_BY_USERNAME, username);
        if (!Authorization.isRightAccount(username)) {
            logger.error("{} : {}", GET_USER_BY_USERNAME, username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findByUsername(username);
        if (user == null) logger.error("{} : {}", GET_USER_BY_USERNAME, NOTFOUND);
        else logger.info("{} : {}", GET_USER_BY_USERNAME, SUCCESS);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        logger.info("{} : {}", CREATE_USER, createUserRequest.toString());
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        if (createUserRequest.getPassword().length() < 7 ||
                !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
            //System.out.println("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create ",
            //		createUserRequest.getUsername());
            logger.error("{} : {}", CREATE_USER, "Invalid password");
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        logger.info("{} : {}", CREATE_USER, SUCCESS);
        return ResponseEntity.ok(user);
    }

}
