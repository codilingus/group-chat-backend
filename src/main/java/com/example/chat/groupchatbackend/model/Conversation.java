package com.example.chat.groupchatbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Conversation {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    @ElementCollection
    @JsonIgnore
    @OneToMany
    private List<Message> messages;
    private ConversationType conversationType;

    @ManyToMany
    private List<User> users;

    public Conversation(String name, List<Message> messages, List<User> users, ConversationType conversationType) {
        this.name = name;
        this.messages = messages;
        this.users = users;
        this.conversationType = conversationType;
    }

    public Conversation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean checkUserPresenceInConversation(User user) {
        return getUsers().stream().anyMatch(member -> member.getId().equals(user.getId()));
    }

    public List<Integer> getUsersIds() {
        return getUsers().stream()
                .map(user -> user.getId())
                .collect(Collectors.toList());
    }
}
