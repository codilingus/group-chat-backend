package com.example.chat.groupchatbackend;

import com.example.chat.groupchatbackend.Message;
import com.example.chat.groupchatbackend.User;

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

    @ElementCollection
    @ManyToMany
    private List<User> users;

    public Conversation(String name, List<Message> messages, List<User> users) {
        this.name = name;
        this.messages = messages;
        this.users = users;
    }

    public Conversation() {
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
