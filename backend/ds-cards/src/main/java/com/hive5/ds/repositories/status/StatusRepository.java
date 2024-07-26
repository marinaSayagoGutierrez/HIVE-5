package com.hive5.ds.repositories.status;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds.entities.status.Status;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    
}
