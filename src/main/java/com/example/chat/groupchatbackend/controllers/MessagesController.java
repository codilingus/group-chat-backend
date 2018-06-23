package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.*;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600L)
@RestController
public class MessagesController {

    @Autowired
    private ConversationsRepository conversationsRepository;
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserContext userContext;

    @GetMapping("/messages/{conversationName}")
    public List<Message> getAllMessages(@PathVariable String conversationName, @RequestParam(required = false) Long timestamp) {
        Conversation conversation = getConversation(conversationName);

        if (timestamp == null) {
            return getMessagesByDate(conversation, LocalDateTime.MIN);
        } else {
            LocalDateTime date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            return getMessagesByDate(conversation, date);
        }
    }

    private List<Message> getMessagesByDate(Conversation conversation, LocalDateTime date) {
        return conversation.getMessages().stream()
                .filter(message -> message.getTimestamp().isAfter(date))
                .collect(Collectors.toList());
    }

    @PostMapping("/messages/channel/{id}")
    public ResponseEntity postChannelMessage(@PathVariable("id") int channelId, @RequestBody String text) {
        Message message = new Message(userContext.getCurrentUser().getId(), text, LocalDateTime.now());
        messagesRepository.save(message);

        Optional<Conversation> conversation = conversationsRepository.findById(channelId);

        if (conversation.isPresent() && conversation.get().getConversationType().equals(ConversationType.CHANNEL)) {
            conversation.get().getMessages().add(message);
            conversationsRepository.save(conversation.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(conversation);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Channel with such id doesn't exist");
    }

    @PostMapping("/messages/private/{userId}")
    public ResponseEntity postPrivateMessage(@PathVariable("userId") int userId, @RequestBody String text) {
        User loggedUser = userContext.getCurrentUser();
        List<Integer> userIds = Arrays.asList(loggedUser.getId(), userId);

        Message message = new Message(loggedUser.getId(), text, LocalDateTime.now());
        messagesRepository.save(message);

        List<Conversation> privateConversations = conversationsRepository
                .findAllByConversationType(ConversationType.DIRECT_MESSAGE);

        Optional<Conversation> conversation = privateConversations.stream()
                .filter(conversation1 -> conversation1.getUsers()
                        .stream()
                        .map(user -> user.getId())
                        .collect(Collectors.toList())
                        .equals(userIds))
                .findAny();

        if (conversation.isPresent()) {
            conversation.get().getMessages().add(message);
            conversationsRepository.save(conversation.get());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(conversation);
        } else {
            String newPrivateConversationName = userContext.getCurrentUser().getName() + "-" + userRepository.findById(userId).get().getName();
            Conversation newConversation = new Conversation(newPrivateConversationName, Arrays.asList(message), Arrays.asList(loggedUser, userRepository.findById(userId).get()), ConversationType.DIRECT_MESSAGE);
            conversationsRepository.save(newConversation);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(newConversation);
        }
    }

    private Conversation getConversation(String conversationName) {
        return conversationsRepository.findByName(conversationName).orElseThrow(() -> new RuntimeException("Conversation with such name doesn't exist"));
    }

    @GetMapping("/messages")
    public Iterable<Message> getAllMessages() {
        return messagesRepository.findAll();
    }

    @PutMapping("/messages/{id}")
    @Transactional
    public Message editMessage(@PathVariable int id, @RequestBody String text) {
        Message message = getMessageById(id);
        message.setText(text);
        return message;
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity deleteMessage(@PathVariable int id) {
        Message message = getMessageById(id);
        messagesRepository.delete(message);
        return new ResponseEntity(HttpStatus.OK);
    }

    private Message getMessageById(int messageId) {
        return messagesRepository.findById(messageId).orElseThrow(() -> new RuntimeException("message doesn't exist"));
    }

}
