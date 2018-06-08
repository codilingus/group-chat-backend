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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
    public List<Message> getAllMessages(@PathVariable String conversationName, @PathVariable Long timestamp){
//       if(timestamp == null){
//            return StreamSupport.stream(conversationsRepository.findByName(conversationName), false)
//                    .filter(conversation -> conversation.getName().equals(conversationName))
//                   .

//       }else{
//           return StreamSupport.stream(messagesRepository.findAll().spliterator(), false)
//                   .filter(message -> message.getTimestamp().isAfter(timestamp))
       return null;
    }

    @PostMapping("/messages/{conversationName}")
    public void postMessageToConversation(
            @PathVariable("conversationName") String conversationName,
            @RequestBody String text) {
        if (conversationsRepository.findByName(conversationName).isPresent()) {
            Message message = new Message(1, text, LocalDateTime.now());
            messagesRepository.save(message);
            Conversation conversation = conversationsRepository.findByName(conversationName).get();
            conversation.getMessages().add(message);
            conversationsRepository.save(conversation);
        } else {
            throw new EntityNotFoundException("Conversation with such name doesn't exists");
        }
    }

}
