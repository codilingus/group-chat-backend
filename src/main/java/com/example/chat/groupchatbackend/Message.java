package com.example.chat.groupchatbackend;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Entity
public class Message {
    @Id
    @GeneratedValue
    private int id;
    private int senderId;
    private String text;
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(int senderId, String text, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = timestamp;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
