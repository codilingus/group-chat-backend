package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.Conversation;
import com.example.chat.groupchatbackend.ConversationType;
import com.example.chat.groupchatbackend.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationsRepository extends CrudRepository<Conversation, Integer> {

    Optional<Conversation> findByName(String name);

    List<Conversation> findAllByConversationType(ConversationType conversationType);

    //TODO: Change query
    //@Query("SELECT c FROM Conversation c JOIN Users u ON (c.users IN :userIds) WHERE c.Conversation_Type = 0")
    //Optional<Conversation> findDirectMessagesByUsers(@Param("userIds") List<User> users);

}
