package com.example.chat.groupchatbackend.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ReadStatus {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private Conversation conversation;
    @ManyToOne
    private User user;
    private LocalDateTime lastReadTimeStamp;

    public ReadStatus() {
    }

    public ReadStatus(int id, Conversation conversation, User user, LocalDateTime lastReadTimeStamp) {
        this.id = id;
        this.conversation = conversation;
        this.user = user;
        this.lastReadTimeStamp = lastReadTimeStamp;
    }

    public ReadStatus(Conversation conversation, User user, LocalDateTime lastReadTimeStamp) {
        this.conversation = conversation;
        this.user = user;
        this.lastReadTimeStamp = lastReadTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLastReadTimeStamp() {
        return lastReadTimeStamp;
    }

    public void setLastReadTimeStamp(LocalDateTime lastReadTimeStamp) {
        this.lastReadTimeStamp = lastReadTimeStamp;
    }
}
