package com.hive5.ds.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds.entities.users.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
