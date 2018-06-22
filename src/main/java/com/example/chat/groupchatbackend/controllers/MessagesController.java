package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.ConversationType;
import com.example.chat.groupchatbackend.Message;
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

@RestController
public class MessagesController {

    @Autowired
    private ConversationsRepository conversationsRepository;
    @Autowired
    private MessagesRepository messagesRepository;

    @GetMapping("/messages/channel/{id}")
    public ResponseEntity getAllMessages(@PathVariable int id , @RequestParam(required = false) Long timestamp) {
        Conversation conversation = conversationsRepository.findById(id).orElseThrow(() -> new RuntimeException("conversation doesn't exist"));
        if (conversation.getConversationType().equals(ConversationType.CHANNEL)) {
            if (timestamp == null) {
                List<Message> result = getMessagesByDate(conversation, LocalDateTime.MIN);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(result);
            } else {
                LocalDateTime date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
                List<Message> result =  getMessagesByDate(conversation, date);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(result);
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("not channel");
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
