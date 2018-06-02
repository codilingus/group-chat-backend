package com.example.chat.groupchatbackend.Repositories;

import com.example.chat.groupchatbackend.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
