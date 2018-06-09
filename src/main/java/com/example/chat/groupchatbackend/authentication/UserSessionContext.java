package com.example.chat.groupchatbackend.authentication;

import com.example.chat.groupchatbackend.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserSessionContext {

    @Autowired
    private SessionRegistry sessionRegistry;

    public List<User> getUsersFromSessionRegistry() {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(principal -> (MyUserPrincipal) principal)
                .map(MyUserPrincipal::getUser)
                .collect(Collectors.toList());
    }
}
