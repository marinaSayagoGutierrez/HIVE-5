package com.hive5.ds.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.hive5.ds.entities.boards.Board;
import com.hive5.ds.entities.users.User;
import com.hive5.ds.repositories.board.BoardRepository;
import com.hive5.ds.repositories.roles.RoleRepository;
import com.hive5.ds.service.UserService;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/board/{id}")
    public Set<User> getAllUsersByIdBoard(@PathVariable Integer id) {
        Board boardEntity = boardRepository.findById(id).orElse(null);
        return boardEntity.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "firstName":
                        user.setFirstName((String) value);
                        break;
                    case "lastName":
                        user.setLastName((String) value);
                        break;
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "password":
                        user.setPassword((String) value);
                        break;
                    case "role":
                        user.setRole(roleRepository.findById((Integer) value).orElse(null));
                        break;

                }
            });
            return ResponseEntity.ok(userService.updateUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
