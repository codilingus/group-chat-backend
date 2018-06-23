package com.example.chat.groupchatbackend.exceptions;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }

}
