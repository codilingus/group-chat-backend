package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.model.ReadStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends CrudRepository<ReadStatus, Integer> {
}
