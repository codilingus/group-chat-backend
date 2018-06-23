package com.example.chat.groupchatbackend.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
