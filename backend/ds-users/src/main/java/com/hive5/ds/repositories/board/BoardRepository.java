package com.hive5.ds.repositories.board;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hive5.ds.entities.boards.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.users WHERE b.id = :boardId")
    Optional<Board> findByIdWithUsers(@Param("boardId") Integer boardId);

}
