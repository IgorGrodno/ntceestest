package com.example.webrisetest.controllers;

import com.example.webrisetest.DTOs.UserDTO;
import com.example.webrisetest.models.Subscription;
import com.example.webrisetest.models.User;
import com.example.webrisetest.servises.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        if (createdUser != null) {
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else {
            logger.error("User creation failed for user: {}", user);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{pageNumber}/{itemsCount}")
    public ResponseEntity<Page<UserDTO>> getUsersPageable(@PathVariable int pageNumber, @PathVariable int itemsCount) {
        Page<UserDTO> users = userService.getUsersPageable(pageNumber, itemsCount);
        if (users.hasContent()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            logger.warn("No users found for page number: {} and items count: {}", pageNumber, itemsCount);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<UserDTO>> getAll() {
        Iterable<UserDTO> users = userService.getAllUsers();
        if (users != null && users.iterator().hasNext()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            logger.warn("No users found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<Iterable<Subscription>> getSubscriptionByUserId(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user.getSubscriptions(), HttpStatus.OK);
        } else {
            logger.warn("User not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return updatedUser != null ? new ResponseEntity<>(updatedUser, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{userId}/subscriptions/{subscriptionId}")
    public ResponseEntity<User> removeSubscriptionFromUser(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        User user = userService.removeSubscriptionFromUser(userId, subscriptionId);
        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userId}/subscriptions/{subscriptionId}")
    public ResponseEntity<User> addSubscriptionToUser(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        User user = userService.addSubscriptionToUser(userId, subscriptionId);
        return user != null ? new ResponseEntity<>(user, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
