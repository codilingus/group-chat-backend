package com.example.chat.groupchatbackend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class ReadStatus {

    @Id
    @GeneratedValue
    private int id;
    private int conversationId;
    private int userId;
    private LocalDateTime lastReadTimeStamp;

    public ReadStatus() {
    }

    public ReadStatus(int id, int conversationId, int userId, LocalDateTime lastReadTimeStamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.userId = userId;
        this.lastReadTimeStamp = lastReadTimeStamp;
    }

    public ReadStatus(int conversationId, int userId, LocalDateTime lastReadTimeStamp) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.lastReadTimeStamp = lastReadTimeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getLastReadTimeStamp() {
        return lastReadTimeStamp;
    }

    public void setLastReadTimeStamp(LocalDateTime lastReadTimeStamp) {
        this.lastReadTimeStamp = lastReadTimeStamp;
    }
}
