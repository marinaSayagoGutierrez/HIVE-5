package com.hive5.ds.repositories.boards;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds.entities.boards.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    
}
