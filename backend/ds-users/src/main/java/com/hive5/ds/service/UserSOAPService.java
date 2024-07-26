package com.hive5.ds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hive5.ds.entities.users.User;
import com.hive5.ds.repositories.users.UserRepository;

@Service
public class UserSOAPService {
     @Autowired
    private UserRepository userRepository;
    
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

}
