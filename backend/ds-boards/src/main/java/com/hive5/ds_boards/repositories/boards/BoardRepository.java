package com.hive5.ds_boards.repositories.boards;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds_boards.entities.boards.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    
}
