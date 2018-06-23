package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.authentication.UserContext;
import com.example.chat.groupchatbackend.model.*;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600L)
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

    @GetMapping("/channels")
    public List<BasicConversation> getAllChannels() {
        return getBasicConversations(ConversationType.CHANNEL);
    }

    @GetMapping("/direct-messages")
    public List<BasicConversation> getAllDirectMessages() {
        return getBasicConversations(ConversationType.DIRECT_MESSAGE);
    }

    private List<BasicConversation> getBasicConversations(ConversationType conversationType) {
        List<Conversation> channels = conversationsRepository.findAllByConversationType(conversationType);
        return channels.stream()
                .map(conversation -> new BasicConversation(conversation.getId(), conversation.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/channels/{id}/users")
    public ResponseEntity getAllChannelMembers(@PathVariable int id) {
        Conversation conversation = conversationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("conversation doesn't exist"));
        if (conversation.getConversationType().equals(ConversationType.CHANNEL)) {
            List<BasicUser> result = conversation.getUsers().stream()
                    .map(user -> new BasicUser(user.getUsername(), user.getName()))
                    .collect(Collectors.toList());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(result);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("not channel");
    }

    @PutMapping("/channels/{id}/join")
    @Transactional
    public ResponseEntity joinChannel(@PathVariable int id) {
        Conversation conversation = conversationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("conversation doesn't exist"));
        User currentUser = userContext.getCurrentUser();

        if (conversation.getConversationType().equals(ConversationType.CHANNEL)) {
            if (!conversation.checkUserPresenceInConversation(currentUser)) {
                conversation.getUsers().add(currentUser);
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("failed to join");
    }



    @PutMapping("/channels/{id}/leave")
    public ResponseEntity leaveChannel(@PathVariable int id) {
        Conversation conversation = conversationsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation doesn't exist"));

        User currentUser = userContext.getCurrentUser();

        if (conversation.getConversationType().equals(ConversationType.CHANNEL)) {
            if (conversation.checkUserPresenceInConversation(currentUser)) {
                conversation.getUsers().removeIf(user -> user.getId().equals(currentUser.getId()));
                conversationsRepository.save(conversation);
            }
            return new ResponseEntity(HttpStatus.OK);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Failed to leave channel");
    }
}

