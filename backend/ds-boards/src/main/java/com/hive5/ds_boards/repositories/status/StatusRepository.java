package com.hive5.ds_boards.repositories.status;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds_boards.entities.status.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    
}
