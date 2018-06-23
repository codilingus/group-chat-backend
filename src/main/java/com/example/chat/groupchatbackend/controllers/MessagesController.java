package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.Message;
import com.example.chat.groupchatbackend.UserContext;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600L)
@RestController
public class MessagesController {

    @Autowired
    private ConversationsRepository conversationsRepository;
    @Autowired
    private MessagesRepository messagesRepository;
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

    @PostMapping("/messages/{conversationName}")
    public void postMessageToConversation(
            @PathVariable("conversationName") String conversationName,
            @RequestBody String text) {
        Conversation conversation = getConversation(conversationName);

        Message message = new Message(1, text, LocalDateTime.now());
        messagesRepository.save(message);
        conversation.getMessages().add(message);
        conversationsRepository.save(conversation);
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
        if (userContext.getCurrentUser().getId().equals(message.getSenderId())) {
            message.setText(text);
            return message;
        } else {
            throw new RuntimeException("You can't edit message");
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity deleteMessage(@PathVariable int id) {
        Message message = getMessageById(id);
        Conversation conversation = conversationsRepository.findByMessageId(id);
        conversation.getMessages().removeIf(message1 -> message1.getId() == id);
        conversationsRepository.save(conversation);
        if (userContext.getCurrentUser().getId().equals(message.getSenderId())) {
            messagesRepository.delete(message);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity("You are not sender of message", HttpStatus.BAD_REQUEST);
        }
    }

    private Message getMessageById(int messageId) {
        return messagesRepository.findById(messageId).orElseThrow(() -> new RuntimeException("message doesn't exist"));
    }
}
