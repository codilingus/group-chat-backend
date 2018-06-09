package com.example.chat.groupchatbackend;

import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class AppInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private ConversationsRepository conversationsRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("alek" , "kotowicz" , "alekot" , "qwerty" , "alek@alek");
        User user1 = new User("jan" , "kowalski" , "janko" , "qwerty" , "jan@jan");
        Message message = new Message(1, "Cześ", LocalDateTime.now());
        Message message1 = new Message(2, "Elo", LocalDateTime.now());
        Conversation conversation = new Conversation("conversation1", Arrays.asList(message), Arrays.asList(user), ConversationType.CHANNEL);
        Conversation conversation1 = new Conversation("conversation2", Arrays.asList(message1), Arrays.asList(user1), ConversationType.DIRECT_MESSAGE);
        userRepository.save(user);
        userRepository.save(user1);
        messagesRepository.save(message);
        messagesRepository.save(message1);
        conversationsRepository.save(conversation);
        conversationsRepository.save(conversation1);


    }
}
