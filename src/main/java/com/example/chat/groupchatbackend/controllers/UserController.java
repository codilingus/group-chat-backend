package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.model.User;
import com.example.chat.groupchatbackend.authentication.UserContext;
import com.example.chat.groupchatbackend.authentication.UserSessionContext;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600L)
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserContext userContext;

    @Autowired
    private UserSessionContext userSessionContext;

    @PostMapping("/registration")
    public ResponseEntity register(@RequestBody User user) {
        if (user.getId() == null) {
            try {
                userRepository.save(user);
                return new ResponseEntity(HttpStatus.OK);
            } catch (DataIntegrityViolationException e) {
                //empty intentionally
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("User already exists");
    }

    @GetMapping("/me")
    public ResponseEntity me() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userContext.getCurrentUser());
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/active")
    public List<Integer> getActiveUsers() {
        List<User> usersFromSessionRegistry = userSessionContext.getUsersFromSessionRegistry();
        return usersFromSessionRegistry.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
