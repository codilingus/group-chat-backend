package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends CrudRepository<Message, Integer> {
}
