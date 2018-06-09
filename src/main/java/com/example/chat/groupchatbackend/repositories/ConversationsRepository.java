package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.ConversationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationsRepository extends CrudRepository<Conversation, Integer> {

    Optional<Conversation> findByName(String name);

    List<Conversation> findAllByConversationType(ConversationType conversationType);
}
