package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.repositories.UserRepository;
import com.example.chat.groupchatbackend.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registration")
    public ResponseEntity register(@RequestBody User user){
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("User already exists");
        } else {
            userRepository.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
