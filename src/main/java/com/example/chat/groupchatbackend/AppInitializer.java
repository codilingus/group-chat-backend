package com.example.chat.groupchatbackend;

import com.example.chat.groupchatbackend.model.Conversation;
import com.example.chat.groupchatbackend.model.ConversationType;
import com.example.chat.groupchatbackend.model.Message;
import com.example.chat.groupchatbackend.model.User;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        User user2 = new User("antek" , "szalony" , "antkow" , "qwerty" , "antek@antek");
        Message message = new Message(1, "Cześ", LocalDateTime.now());
        Message message1 = new Message(2, "Elo", LocalDateTime.now());
        Message message2 = new Message(3, "Cześ22", LocalDateTime.now());

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);

        messagesRepository.save(message);
        messagesRepository.save(message1);
        messagesRepository.save(message2);

        Conversation conversation = new Conversation("conversation1", Arrays.asList(message), Arrays.asList(user), ConversationType.CHANNEL);
        Conversation conversation1 = new Conversation("conversation2", Arrays.asList(message1), Arrays.asList(user, user1), ConversationType.DIRECT_MESSAGE);

        conversationsRepository.save(conversation);
        conversationsRepository.save(conversation1);

    }
}
