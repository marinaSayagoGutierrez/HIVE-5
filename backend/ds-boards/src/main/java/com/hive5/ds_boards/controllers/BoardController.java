package com.hive5.ds_boards.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hive5.ds_boards.entities.boards.Board;
import com.hive5.ds_boards.service.BoardService;

@RestController
@RequestMapping("/boards")
public class BoardController {

  @Autowired
  private BoardService boardService;

  @GetMapping
  public Object getAllBoards() {
    return new Object() {
        public List<Board> boards = boardService.getAllBoards();
    };
}
  @GetMapping("/{id}")
  public ResponseEntity<Board> getBoardById(@PathVariable Integer id) {
    Optional<Board> board = boardService.getBoardById(id);
    return board.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public Board createBoard(@RequestBody Board board) {
    System.out.println(board.getName());
    return boardService.saveBoard(board);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBoard(@PathVariable Integer id) {
    boardService.deleteBoard(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Board> updateBoard(@PathVariable Integer id, @RequestBody Board boardDetails) {
    Optional<Board> boardOptional = boardService.getBoardById(id);
    if (boardOptional.isPresent()) {
      Board board = boardOptional.get();
      board.setName(boardDetails.getName());
      board.setCards(boardDetails.getCards());
      board.setUsers(boardDetails.getUsers());
      return ResponseEntity.ok(boardService.updateBoard(board));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
