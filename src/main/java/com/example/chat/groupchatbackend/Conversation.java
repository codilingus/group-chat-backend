package com.example.chat.groupchatbackend;

import javax.persistence.*;
import java.util.List;

@Entity
public class Conversation {

    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;
    @ElementCollection
    @OneToMany
    private List<Message> messages;
    private ConversationType conversationType;

    @ElementCollection
    @ManyToMany
    private List<User> users;

    public Conversation(List<Message> messages, List<User> users, ConversationType conversationType) {
        this.messages = messages;
        this.users = users;
        this.conversationType = conversationType;
    }

    public Conversation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
