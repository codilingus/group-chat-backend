package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.*;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/channels")
    public List<BasicConversation> getAllChannels() {
        return getBasicConversations(ConversationType.CHANNEL);
    }

    @GetMapping("/direct-messages")
    public List<BasicConversation> getAllDirectMessages() {
        return getBasicConversations(ConversationType.DIRECT_MESSAGE);
    }

    private List<BasicConversation> getBasicConversations(ConversationType conversationType) {
        Iterable<Conversation> channelsIterable = conversationsRepository.findAll();
        List<Conversation> channels = (List<Conversation>) channelsIterable;
        return channels.stream()
                .filter(channel -> channel.getConversationType().equals(conversationType))
                .map(conversation -> new BasicConversation(conversation.getId(), conversation.getName()))
                .collect(Collectors.toList());
    }
}

