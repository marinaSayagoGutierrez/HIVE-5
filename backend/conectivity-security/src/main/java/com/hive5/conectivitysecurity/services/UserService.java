package com.hive5.conectivitysecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hive5.conectivitysecurity.entities.users.User;
import com.hive5.conectivitysecurity.repositories.users.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Integer getUserIdByEmail(String email) {
        User user = findByEmail(email);
        if (user != null) {
            return user.getId();
        }
        return null;
    }
}
