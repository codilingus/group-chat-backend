package com.example.chat.groupchatbackend.controllers;

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
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @DeleteMapping("/messages/{id}")
    public ResponseEntity deleteMessage(@PathVariable int id){
        Message message = messagesRepository.findById(id).orElseThrow(() -> new RuntimeException("message doesn't exist"));
        messagesRepository.delete(message);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity me(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userRepository.findById(1));
    }
}
