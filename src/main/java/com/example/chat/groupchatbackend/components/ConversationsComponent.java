package com.example.chat.groupchatbackend.components;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.Message;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ConversationsComponent {

    @Autowired
    private ConversationsRepository conversationsRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    public void addConversation(Conversation conversation) {
        conversationsRepository.save(conversation);
    }

    public void addMessageToConversation(int conversationId, Message message) {
        if (conversationsRepository.existsById(conversationId)) {
            Conversation conversation = conversationsRepository.findById(conversationId).get();
            conversation.getMessages().add(message);
            conversationsRepository.save(conversation);
        } else {
            System.out.println("Conversation with such name doesn't exist");
        }
    }

    public List<Message> getMessagesAllOrByDate(int conversationId, LocalDateTime date) {
        if (conversationsRepository.existsById(conversationId)) {
            if (date == null) {
                return StreamSupport.stream(conversationsRepository.findAll().spliterator(), false)
                        .filter(conversation -> conversation.getName().equals(conversationId))
                        .flatMap(conversation -> conversation.getMessages().stream())
                        .collect(Collectors.toList());
            } else {
                return StreamSupport.stream(conversationsRepository.findAll().spliterator(), false)
                        .filter(conversation -> conversation.getName().equals(conversationId))
                        .flatMap(conversation -> conversation.getMessages().stream())
                        .filter(message -> message.getTimestamp().isAfter(date))
                        .collect(Collectors.toList());
            }
        }
        System.out.println("Conversation with such name doesn't exist");
        return null;
    }

}
