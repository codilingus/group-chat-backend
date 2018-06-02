package com.example.chat.groupchatbackend.repositories;

import org.springframework.data.repository.CrudRepository;
import com.example.chat.groupchatbackend.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends CrudRepository<Message, Integer> {
}
