package com.example.chat.groupchatbackend;

import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AppInitializer implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessagesRepository messagesRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = new User("alek" , "kotowicz" , "alekot" , "qwerty" , "alek@alek");
        Message message = new Message(1, "Cześ", LocalDateTime.now());
        userRepository.save(user);
        messagesRepository.save(message);
    }
}
