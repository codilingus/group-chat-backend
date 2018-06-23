package com.example.chat.groupchatbackend.controllers;

import com.example.chat.groupchatbackend.authentication.UserContext;
import com.example.chat.groupchatbackend.exceptions.BadRequestException;
import com.example.chat.groupchatbackend.exceptions.NotFoundException;
import com.example.chat.groupchatbackend.model.Conversation;
import com.example.chat.groupchatbackend.model.Message;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600L)
@RestController
public class MessagesController {

    @Autowired
    private ConversationsRepository conversationsRepository;
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private UserContext userContext;

    @GetMapping("/messages/channel/{id}")
    public ResponseEntity getAllMessages(@PathVariable int id, @RequestParam(required = false) Long timestamp) {
        LocalDateTime date = LocalDateTime.MIN;
        if (timestamp != null) {
            date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(conversationService.getChannelMessagesByDate(id, date));
    }

    @GetMapping("/messages/private/{conversationId}")
    public List<Message> getPrivateConversationMessagesWithUser(@PathVariable int conversationId, @RequestParam(required = false) Long timestamp) {
        LocalDateTime time = LocalDateTime.MIN;
        if (timestamp != null) {
            time = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        return conversationService.getDirectMessagesByDate(conversationId, time);
    }

    @PostMapping("/messages/channel/{id}")
    public Message postChannelMessage(@PathVariable("id") int channelId, @RequestBody String text) {
        return conversationService.addNewMessageToChannel(channelId, text);
    }

    @PostMapping("/messages/private/{userId}")
    public Message postPrivateMessage(@PathVariable("userId") int userId, @RequestBody String text) {
        return conversationService.addNewDirectMessage(userContext.getCurrentUser().getId(), userId, text);
    }

    private Conversation getConversationById(int conversationId) {
        return conversationsRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation with such name doesn't exist"));
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
            throw new BadRequestException("You can't edit message");
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
        return messagesRepository.findById(messageId).orElseThrow(() -> new NotFoundException("message doesn't exist"));
    }

}
