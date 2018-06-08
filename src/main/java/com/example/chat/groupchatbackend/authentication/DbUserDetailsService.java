package com.example.chat.groupchatbackend.authentication;

import com.example.chat.groupchatbackend.User;
import com.example.chat.groupchatbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.StreamSupport;
@Component
public class DbUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<User> userOptional = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .filter(user -> user.getUsername().equals(s))
                .findFirst();
        if(userOptional.isPresent()){
            return new MyUserPrincipal(userOptional.get());
        }
        return null;
    }
}
