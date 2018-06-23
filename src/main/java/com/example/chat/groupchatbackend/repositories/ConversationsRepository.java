package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.model.Conversation;
import com.example.chat.groupchatbackend.model.ConversationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationsRepository extends CrudRepository<Conversation, Integer> {

    Optional<Conversation> findByName(String name);

    Optional<Conversation> findByIdAndConversationType(int id, ConversationType conversationType);

    List<Conversation> findAllByConversationType(ConversationType conversationType);

    @Query(value = "SELECT c FROM Conversation c JOIN c.messages m WHERE m.id = ?1")
    Conversation findByMessageId(int messageId);

    default Optional<Conversation> findDirectConversationByUsers(int user1, int user2) {
        return findAllByConversationType(ConversationType.DIRECT_MESSAGE).stream()
                .filter(conversation -> conversation.getUsersIds().equals(Arrays.asList(user1, user2)))
                .findAny();
    }

}
