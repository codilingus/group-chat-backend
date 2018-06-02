package com.example.chat.groupchatbackend.repositories;



import org.springframework.data.repository.CrudRepository;
import com.example.chat.groupchatbackend.Message;
import org.springframework.stereotype.Component;

@Component
public interface MessagesRepository extends CrudRepository<Message, Integer> {
}
