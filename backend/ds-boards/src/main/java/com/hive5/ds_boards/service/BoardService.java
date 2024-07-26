package com.hive5.ds_boards.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hive5.ds_boards.entities.boards.Board;
import com.hive5.ds_boards.repositories.boards.BoardRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Optional<Board> getBoardById(Integer id) {
        return boardRepository.findById(id);
    }

    @Transactional
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteBoard(Integer id) {
        boardRepository.deleteById(id);
    }

    public Board updateBoard(Board board) {
        return boardRepository.save(board);
    }
}
