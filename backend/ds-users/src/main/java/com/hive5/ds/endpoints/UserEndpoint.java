package com.hive5.ds.endpoints;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.hive5.ds.elements.BoardType;
import com.hive5.ds.elements.BoardsType;
import com.hive5.ds.elements.CreateUserRequest;
import com.hive5.ds.elements.CreateUserResponse;
import com.hive5.ds.elements.DeleteUserRequest;
import com.hive5.ds.elements.DeleteUserResponse;
import com.hive5.ds.elements.GetAllUserByBoardIdRequest;
import com.hive5.ds.elements.GetAllUserByBoardIdResponse;
import com.hive5.ds.elements.GetAllUserRequest;
import com.hive5.ds.elements.GetAllUserResponse;
import com.hive5.ds.elements.GetUserRequest;
import com.hive5.ds.elements.GetUserResponse;
import com.hive5.ds.elements.RoleType;
import com.hive5.ds.elements.UpdateUserRequest;
import com.hive5.ds.elements.UpdateUserResponse;
import com.hive5.ds.elements.User;
import com.hive5.ds.entities.boards.Board;
import com.hive5.ds.entities.role.Role;
import com.hive5.ds.repositories.board.BoardRepository;
import com.hive5.ds.repositories.roles.RoleRepository;
import com.hive5.ds.repositories.users.UserRepository;

@Endpoint
public class UserEndpoint {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BoardRepository boardRepository;

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "getUserRequest" )
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUserRequest request) {
        GetUserResponse response = new GetUserResponse();

        Integer userId = request.getUserId();

        
        com.hive5.ds.entities.users.User userEntity = userRepository.findById(userId).orElse(null);

        User generatedUser = convertToGeneratedUser(userEntity);

        response.setUser(generatedUser);

        return response;
    }

    public User convertToGeneratedUser(com.hive5.ds.entities.users.User userEntity) {
        User generatedUser = new User();
        generatedUser.setId(userEntity.getId());
        generatedUser.setFirstName(userEntity.getFirstName());
        generatedUser.setLastName(userEntity.getLastName());
        generatedUser.setEmail(userEntity.getEmail());
        generatedUser.setPassword(userEntity.getPassword());

        // Convertir rol
        Role roleEntity = userEntity.getRole();
        RoleType generatedRole = new RoleType();
        generatedRole.setId(roleEntity.getId());
        generatedRole.setName(roleEntity.getName());
        generatedUser.setRole(generatedRole);

        // Convertir tableros
        Set<Board> boards = userEntity.getBoards();
        if (boards != null && !boards.isEmpty()) {
            BoardsType boardsType = new BoardsType();
            List<BoardType> boardTypes = boardsType.getBoard();
            for (Board boardEntity : boards) {
                BoardType generatedBoard = new BoardType();
                generatedBoard.setId(boardEntity.getId());
                generatedBoard.setName(boardEntity.getName());
                boardTypes.add(generatedBoard);
            }
            generatedUser.setBoards(boardsType);
        }

        return generatedUser;
    }

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "getAllUserByBoardIdRequest")
    @ResponsePayload
    public GetAllUserByBoardIdResponse getAllUserByBoardId(@RequestPayload GetAllUserByBoardIdRequest request) {
        GetAllUserByBoardIdResponse response = new GetAllUserByBoardIdResponse();
    
        Integer boardId = request.getBoardId();
    
        Board boardEntity = boardRepository.findByIdWithUsers(boardId).orElse(null);
    
        if (boardEntity != null) {
            Hibernate.initialize(boardEntity.getUsers());
            Set<com.hive5.ds.entities.users.User> users = boardEntity.getUsers();
            List<User> generatedUsers = new ArrayList<>();
    
            for (com.hive5.ds.entities.users.User userEntity : users) {
                User generatedUser = convertToGeneratedUser(userEntity);
                generatedUsers.add(generatedUser);
            }
    
            response.getUser().addAll(generatedUsers);
        }
    
        return response;
    }

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "getAllUserRequest")
    @ResponsePayload
    public GetAllUserResponse getAllUsers() {
        GetAllUserResponse response = new GetAllUserResponse();

        List<com.hive5.ds.entities.users.User> users = userRepository.findAll();
        List<User> generatedUsers = new ArrayList<>();

        for (com.hive5.ds.entities.users.User userEntity : users) {
            User generatedUser = convertToGeneratedUser(userEntity);
            generatedUsers.add(generatedUser);
        }

        response.getUser().addAll(generatedUsers);

        return response;
    }



    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "createUserRequest")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUserRequest request) {
        CreateUserResponse response = new CreateUserResponse();

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());

        RoleType role = new RoleType();
        role.setId(request.getRoleId());
        newUser.setRole(role);

        // Convertir Set<Board> a BoardsType
        BoardsType boardsType = new BoardsType();
        List<BoardType> boardTypes = boardsType.getBoard();
        for (Integer boardId : request.getBoards().getBoardId()) {
            Board board = boardRepository.findById(boardId).orElse(null);
            if (board != null) {
                BoardType boardType = new BoardType();
                boardType.setId(board.getId());
                boardType.setName(board.getName());

                boardTypes.add(boardType);
            }
        }
        newUser.setBoards(boardsType);

        com.hive5.ds.entities.users.User userEntity = convertToEntityUser(newUser);
        userEntity = userRepository.save(userEntity);

        // Inicializar las colecciones necesarias antes de devolver la respuesta
        Hibernate.initialize(userEntity.getBoards());  // Inicializar la colección de boards

        User generatedUser = convertToGeneratedUser(userEntity);
        response.setUser(generatedUser);

        return response;
    }

    



    private com.hive5.ds.entities.users.User convertToEntityUser(User user) {
        com.hive5.ds.entities.users.User userEntity = new com.hive5.ds.entities.users.User();
        userEntity.setId(user.getId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
    
        Role roleEntity = new Role();
        roleEntity.setId(user.getRole().getId());
        roleEntity.setName(user.getRole().getName());
        userEntity.setRole(roleEntity);
    
        // Asociar tableros a userEntity
        for (BoardType boardType : user.getBoards().getBoard()) {
            Board boardEntity = boardRepository.findById(boardType.getId()).orElse(null);
            if (boardEntity != null) {
                userEntity.getBoards().add(boardEntity);
                boardEntity.getUsers().add(userEntity); // Asegurar que la relación bidireccional esté actualizada
            }
        }
    
        return userEntity;
    }
    
    


    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "deleteUserRequest")
    @ResponsePayload
    public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        DeleteUserResponse response = new DeleteUserResponse();

        Integer userId = request.getUserId();
        com.hive5.ds.entities.users.User userEntity = userRepository.findById(userId).orElse(null);

        if (userEntity != null) {
            userRepository.delete(userEntity);
            response.setMessage("User successfully removed");
        } else {
            response.setMessage("User wit ID " + userId + " not found");
        }

        return response;
    }

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        UpdateUserResponse response = new UpdateUserResponse();

        Integer userId = request.getUserId();
        com.hive5.ds.entities.users.User userEntity = userRepository.findById(userId).orElse(null);

        if (userEntity != null) {
            // Actualizar los campos básicos del usuario
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                userEntity.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                userEntity.setLastName(request.getLastName());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                userEntity.setEmail(request.getEmail());
            }
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                userEntity.setPassword(request.getPassword());
            }
            if (request.getRoleId() != 0) {
                Role role = roleRepository.findById(request.getRoleId()).orElse(null);
                userEntity.setRole(role);
            }

            // Actualizar los tableros asociados al usuario
            if (request.getBoards() != null && !request.getBoards().getBoardId().isEmpty()) {
                Set<Board> updatedBoards = new HashSet<>();
                for (Integer boardId : request.getBoards().getBoardId()) {
                    Board board = boardRepository.findById(boardId).orElse(null);
                    if (board != null) {
                        updatedBoards.add(board);
                    }
                }
                userEntity.setBoards(updatedBoards);
            }

            // Guardar el usuario actualizado
            userEntity = userRepository.save(userEntity);

            // Convertir el usuario actualizado a tipo generado
            User generatedUser = convertToGeneratedUser(userEntity);
            response.setUser(generatedUser);
        }

        return response;
    }



}
