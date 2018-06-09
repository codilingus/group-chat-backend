package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.*;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
public class ConversationController {

    @Autowired
    private ConversationsRepository conversationsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserContext userContext;

    @PostMapping("/channel")
    public ResponseEntity createChannel(@RequestBody String name) {
        if (name.contains("&") || conversationsRepository.findByName(name).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("incorrect name");
        }

        Conversation conversation = new Conversation(name, new LinkedList<Message>(), new LinkedList<User>(), ConversationType.CHANNEL);
        conversation.getUsers().add(userContext.getCurrentUser());
        conversationsRepository.save(conversation);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversation);
    }
}

