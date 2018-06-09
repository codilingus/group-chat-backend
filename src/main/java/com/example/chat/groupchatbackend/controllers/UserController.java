package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.Message;
import com.example.chat.groupchatbackend.User;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationsRepository conversationsRepository;
    @Autowired
    private MessagesRepository messagesRepository;

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

    @GetMapping("/messages/{conversationName}?newerThan={timestamp}")
    public List<Message> getAllMessages(@PathVariable String conversationName, @PathVariable Long timestamp) {
        if (timestamp == null) {
            Conversation conversation = getConversation(conversationName);
            return conversation.getMessages();
        } else {
            Conversation conversation = getConversation(conversationName);
            LocalDateTime date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            return conversation.getMessages().stream()
                    .filter(message -> message.getTimestamp().isAfter(date))
                    .collect(Collectors.toList());
        }
    }

    private Conversation getConversation(@PathVariable String conversationName) {
        return conversationsRepository.findByName(conversationName).orElseThrow(() -> new RuntimeException());
    }

    @GetMapping("/messages")
    public Iterable<Message> getAllMessages(){
        return messagesRepository.findAll();
    }

    @PutMapping("/messages/{id}")
    @Transactional
    public Message editMessage(@PathVariable int id, @RequestBody String text){
        Message message = messagesRepository.findById(id).orElseThrow(() -> new RuntimeException("message doesn't exist"));
        message.setText(text);
        return message;
    }
}
