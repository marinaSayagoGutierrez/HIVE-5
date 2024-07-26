package com.hive5.ds_boards.repositories.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds_boards.entities.users.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
