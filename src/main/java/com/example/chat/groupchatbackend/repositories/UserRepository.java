package com.example.chat.groupchatbackend.repositories;

import com.example.chat.groupchatbackend.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
