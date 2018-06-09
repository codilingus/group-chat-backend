package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.ConversationType;
import com.example.chat.groupchatbackend.Message;
import com.example.chat.groupchatbackend.User;
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

    @PostMapping("/channel")
    public ResponseEntity createChannel(@RequestBody String name) {
        if (name.contains("&") || conversationsRepository.findByName(name).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("incorrect name");
        }

        Conversation conversation = new Conversation(name, new LinkedList<Message>(), new LinkedList<User>(), ConversationType.CHANNEL);
        conversation.getUsers().add(userRepository.findById(1).get());
        conversationsRepository.save(conversation);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversation);
    }
}

