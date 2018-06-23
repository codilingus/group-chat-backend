package com.example.chat.groupchatbackend.service;

import com.example.chat.groupchatbackend.authentication.UserContext;
import com.example.chat.groupchatbackend.model.*;
import com.example.chat.groupchatbackend.repositories.ConversationsRepository;
import com.example.chat.groupchatbackend.repositories.MessagesRepository;
import com.example.chat.groupchatbackend.repositories.ReadStatusRepository;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConversationService {

    @Autowired
    private ConversationsRepository conversationsRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserContext userContext;

    public List<Message> getChannelMessagesByDate(int conversationId, LocalDateTime time) {
        Conversation conversation = getConversationByIdAndType(conversationId, ConversationType.CHANNEL);
        updateReadStatus(conversation);
        return getMessagesByDate(conversation, time);
    }

    public List<Message> getDirectMessagesByDate(int conversationId, LocalDateTime time) {
        Conversation conversation = getConversationByIdAndType(conversationId, ConversationType.DIRECT_MESSAGE);
        User currentUser = userContext.getCurrentUser();

        if (conversation.checkUserPresenceInConversation(currentUser)) {
            updateReadStatus(conversation);
            return getMessagesByDate(conversation, time);
        } else {
            throw new RuntimeException("User is not present in this conversation");
        }
    }

    public Message addNewMessageToChannel(int channelId, String text) {
        Message message = persistNewMessage(userContext.getCurrentUser().getId(), text);
        addMessageToChannel(channelId, message);
        return message;
    }

    private void addMessageToChannel(int channelId, Message message) {
        Conversation conversation = getConversationByIdAndType(channelId, ConversationType.CHANNEL);
        addMessageToConversation(conversation, message);
    }

    private void addMessageToConversation(Conversation conversation, Message message) {
        conversation.getMessages().add(message);
        conversationsRepository.save(conversation);
    }

    private Message persistNewMessage(int senderId, String text) {
        Message message = new Message(senderId, text, LocalDateTime.now());
        messagesRepository.save(message);
        return message;
    }

    private void updateReadStatus(Conversation conversation) {
        ReadStatus readStatus = new ReadStatus(conversation, userContext.getCurrentUser(), LocalDateTime.now());
        readStatusRepository.save(readStatus);
    }

    public Message addNewDirectMessage(int senderId, int receiverId, String text) {
        Optional<Conversation> conversation = conversationsRepository.findDirectConversationByUsers(senderId, receiverId);

        Message message = persistNewMessage(senderId, text);
        if (conversation.isPresent()) {
            addMessageToConversation(conversation.get(), message);
        } else {
            createNewDirectConversation(findUser(senderId), findUser(receiverId), message);
        }
        return message;
    }

    private User findUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    private void createNewDirectConversation(User sender, User receiver, Message messages) {
        String newPrivateConversationName = sender + "-" + receiver;
        Conversation newConversation = new Conversation(newPrivateConversationName, Arrays.asList(messages), Arrays.asList(sender, receiver), ConversationType.DIRECT_MESSAGE);
        conversationsRepository.save(newConversation);
    }

    public List<Message> getMessagesByDate(Conversation conversation, LocalDateTime date) {
        return conversation.getMessages().stream()
                .filter(message -> message.getTimestamp().isAfter(date))
                .collect(Collectors.toList());
    }

    public Conversation getConversationByIdAndType(int conversationId, ConversationType type) {
        return conversationsRepository.findByIdAndConversationType(conversationId, type)
                .orElseThrow(() -> new RuntimeException("Conversation with such name doesn't exist"));
    }

    public void addConversation(Conversation conversation) {
        conversationsRepository.save(conversation);
    }

    public void addMessageToConversation(int conversationId, Message message) {
        Conversation conversation = conversationsRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation with such name doesn't exist"));

        addMessageToConversation(conversation, message);
    }

    public List<Message> getMessagesAllOrByDate(int conversationId, LocalDateTime date) {
        LocalDateTime startTime = Optional.ofNullable(date).orElse(LocalDateTime.MIN);
        Conversation conversation = conversationsRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation with such name doesn't exist"));

        return conversation.getMessages().stream()
                .filter(message -> message.getTimestamp().isAfter(startTime))
                .collect(Collectors.toList());
    }

}
