package com.example.demo.controllers;

import java.util.List;

import com.example.demo.security.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import static com.example.demo.utils.Constants.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	private final Logger logger = LogManager.getLogger(OrderController.class);

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		logger.info("{} : {}", SUBMIT_ORDER, username);
		if(!Authorization.isRightAccount(username)){
			logger.error("{} : {}", SUBMIT_ORDER, FORBIDDEN);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("{} : {}", SUBMIT_ORDER, NOTFOUND);
			return ResponseEntity.notFound().build();
		}
		Cart cart = user.getCart();
		cart.setUser(user);
		UserOrder order = UserOrder.createFromCart(cart);
		orderRepository.save(order);
		logger.info("{} : {}", SUBMIT_ORDER, SUCCESS);
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		logger.info("{} : {}", HISTORY_BY_USER, username);
		if(!Authorization.isRightAccount(username)){
			logger.error("{} : {}", HISTORY_BY_USER, FORBIDDEN);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		User user = userRepository.findByUsername(username);
		if(user == null) {
			logger.error("{} : {}", HISTORY_BY_USER, NOTFOUND);
			return ResponseEntity.notFound().build();
		}
		logger.info("{} : {}", HISTORY_BY_USER, SUCCESS);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
