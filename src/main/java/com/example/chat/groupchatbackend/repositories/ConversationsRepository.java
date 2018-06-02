package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.Conversation;
import org.springframework.data.repository.CrudRepository;

public interface ConversationsRepository extends CrudRepository<Conversation, Integer> {
}
