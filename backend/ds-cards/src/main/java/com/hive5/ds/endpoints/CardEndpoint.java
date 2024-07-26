package com.hive5.ds.endpoints;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.hive5.ds.elements.BoardType;
import com.hive5.ds.elements.Card;
import com.hive5.ds.elements.CreateCardRequest;
import com.hive5.ds.elements.CreateCardResponse;
import com.hive5.ds.elements.DeleteCardRequest;
import com.hive5.ds.elements.DeleteCardResponse;
import com.hive5.ds.elements.GetAllCardResponse;
import com.hive5.ds.elements.GetCardRequest;
import com.hive5.ds.elements.GetCardResponse;
import com.hive5.ds.elements.RoleType;
import com.hive5.ds.elements.StatusType;
import com.hive5.ds.elements.UpdateCardRequest;
import com.hive5.ds.elements.UpdateCardResponse;
import com.hive5.ds.elements.UserType;
import com.hive5.ds.entities.boards.Board;
import com.hive5.ds.entities.role.Role;
import com.hive5.ds.entities.status.Status;
import com.hive5.ds.entities.users.User;
import com.hive5.ds.repositories.boards.BoardRepository;
import com.hive5.ds.repositories.cards.CardRepository;
import com.hive5.ds.repositories.status.StatusRepository;
import com.hive5.ds.repositories.users.UserRepository;

@Endpoint
public class CardEndpoint {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StatusRepository statusRepository;

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "getCardRequest" )
    @ResponsePayload
    public GetCardResponse getCard(@RequestPayload GetCardRequest request) throws DatatypeConfigurationException {
        GetCardResponse response = new GetCardResponse();

        Integer cardId = request.getCardId();

        com.hive5.ds.entities.cards.Card cardEntity = cardRepository.findById(cardId).orElse(null);

        Card generatedCard = convertToGeneratedCard(cardEntity);

        response.setCard(generatedCard);

        return response;
    }

    private Card convertToGeneratedCard(com.hive5.ds.entities.cards.Card cardEntity) throws DatatypeConfigurationException {
        Card generatedCard = new Card();
        generatedCard.setId(cardEntity.getId());
        generatedCard.setTitle(cardEntity.getTitle());
        generatedCard.setPriority(cardEntity.getPriority());
        generatedCard.setDescription(cardEntity.getDescription());
        generatedCard.setStartDate(this.convertTGregorianCalendar(cardEntity.getStartDate()));
        generatedCard.setEndDate(this.convertTGregorianCalendar(cardEntity.getEndDate()));

        User userEntity = cardEntity.getUser();
        if (userEntity != null) {
            UserType generatedUser = new UserType();
            generatedUser.setId(userEntity.getId());
            generatedUser.setFirstName(userEntity.getFirstName());
            generatedUser.setLastName(userEntity.getLastName());
            generatedUser.setEmail(userEntity.getEmail());
            generatedUser.setPassword(userEntity.getPassword());


            Role roleEntity = userEntity.getRole();
            if (roleEntity != null) {
                RoleType generatedRole = new RoleType();
                generatedRole.setId(roleEntity.getId());
                generatedRole.setName(roleEntity.getName());
                generatedUser.setRole(generatedRole);
            }

            generatedCard.setUser(generatedUser);
        }


        Board boardEntity = cardEntity.getBoard();
        if (boardEntity != null) {
            BoardType generatedBoard = new BoardType();
            generatedBoard.setId(boardEntity.getId());
            generatedBoard.setName(boardEntity.getName());
            generatedCard.setBoard(generatedBoard);
        }


        Status statusEntity = cardEntity.getStatus();
        if (statusEntity != null) {
            StatusType generatedStatus = new StatusType();
            generatedStatus.setId(statusEntity.getId());
            generatedStatus.setName(statusEntity.getName());
            generatedCard.setStatus(generatedStatus);
        }

        return generatedCard;
    }

    private XMLGregorianCalendar convertTGregorianCalendar(LocalDate localDate) throws DatatypeConfigurationException {
        // Crear un objeto DatatypeFactory
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

        // Convertir LocalDate a XMLGregorianCalendar
        return datatypeFactory.newXMLGregorianCalendar(localDate.toString());
    }


    @PayloadRoot(namespace = "http://ds.hive5.com/elements",  localPart = "getAllCardRequest" )
    @ResponsePayload
    public GetAllCardResponse getAllCards() throws DatatypeConfigurationException {
        GetAllCardResponse response = new GetAllCardResponse();

        List<com.hive5.ds.entities.cards.Card> cards = cardRepository.findAll();
        List<Card> generatedCards = new ArrayList<>();

        for (com.hive5.ds.entities.cards.Card cardEntity : cards) {
            Card generatedCard = convertToGeneratedCard(cardEntity);
            generatedCards.add(generatedCard);
        }

        response.getCard().addAll(generatedCards);

        return response;
    }


    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "createCardRequest")
    @ResponsePayload
    public CreateCardResponse createCard(@RequestPayload CreateCardRequest request) throws DatatypeConfigurationException {
        CreateCardResponse response = new CreateCardResponse();

        Card newCard = new Card();
        newCard.setTitle(request.getTitle());
        newCard.setDescription(request.getDescription());
        newCard.setStartDate(request.getStartDate());
        newCard.setEndDate(request.getEndDate());
        newCard.setPriority(request.getPriority());

        UserType user = new UserType();
        user.setId(request.getUserId());
        newCard.setUser(user);

        BoardType board = new BoardType();
        board.setId(request.getBoardId());
        newCard.setBoard(board);

        StatusType status = new StatusType();
        status.setId(request.getStatusId());
        newCard.setStatus(status);

        com.hive5.ds.entities.cards.Card cardEntity = convertToEntityCard(newCard);
        cardRepository.save(cardEntity);

        Card generatedCard = convertToGeneratedCard(cardEntity);
        response.setCard(generatedCard);

        return response;
    }

    private com.hive5.ds.entities.cards.Card convertToEntityCard(Card card) {
        com.hive5.ds.entities.cards.Card cardEntity = new com.hive5.ds.entities.cards.Card();

        cardEntity.setTitle(card.getTitle());
        cardEntity.setDescription(card.getDescription());
        cardEntity.setStartDate(LocalDate.parse(card.getStartDate().toXMLFormat()));
        cardEntity.setEndDate(LocalDate.parse(card.getEndDate().toXMLFormat()));
        cardEntity.setPriority(card.getPriority());

        User userEntity = userRepository.findById(card.getUser().getId()).orElse(null);
        cardEntity.setUser(userEntity);

        Board boardEntity = boardRepository.findById(card.getBoard().getId()).orElse(null);
        cardEntity.setBoard(boardEntity);

        Status statusEntity = statusRepository.findById(card.getStatus().getId()).orElse(null);
        cardEntity.setStatus(statusEntity);

        return cardEntity;
    }

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "deleteCardRequest")
    @ResponsePayload
    public DeleteCardResponse deleteCard(@RequestPayload DeleteCardRequest request) {
        DeleteCardResponse response = new DeleteCardResponse();

        Integer cardId = request.getCardId();
        com.hive5.ds.entities.cards.Card cardEntity = cardRepository.findById(cardId).orElse(null);

        if (cardEntity != null) {
            cardRepository.delete(cardEntity);
            response.setMessage("Card successfully removed");
        } else {
            response.setMessage("Card with ID " + cardId + " not found");
        }

        return response;
    }

    @PayloadRoot(namespace = "http://ds.hive5.com/elements", localPart = "updateCardRequest")
    @ResponsePayload
    public UpdateCardResponse updateCard(@RequestPayload UpdateCardRequest request) throws DatatypeConfigurationException {
        UpdateCardResponse response = new UpdateCardResponse();

        Integer cardId = request.getCardId();
        com.hive5.ds.entities.cards.Card cardEntity = cardRepository.findById(cardId).orElse(null);

        if (cardEntity != null) {
            if (!"".equals(request.getTitle())) {
                cardEntity.setTitle(request.getTitle());
            }
            if (!"".equals(request.getDescription())) {
                cardEntity.setDescription(request.getDescription());
            }
            if (request.getStartDate() != null) {
                cardEntity.setStartDate(convertToLocalDate(request.getStartDate()));
            }
            if (request.getEndDate() != null) {
                cardEntity.setEndDate(convertToLocalDate(request.getEndDate()));
            }
            if (request.getPriority() != 0) {
                cardEntity.setPriority(request.getPriority());
            }
            if (request.getUserId() != 0) {
                User user = userRepository.findById(request.getUserId()).orElse(null);
                cardEntity.setUser(user);
            }
            if (request.getBoardId() != 0) {
                Board board = boardRepository.findById(request.getBoardId()).orElse(null);
                cardEntity.setBoard(board);
            }
            if (request.getStatusId() != 0) {
                Status status = statusRepository.findById(request.getStatusId()).orElse(null);
                cardEntity.setStatus(status);
            }

            cardEntity = cardRepository.save(cardEntity);

            System.out.println(cardEntity);

            Card generatedCard = convertToGeneratedCard(cardEntity);
            response.setCard(generatedCard);

        }

        return response;
    }

    private LocalDate convertToLocalDate(XMLGregorianCalendar xmlGregorianCalendar) throws DatatypeConfigurationException {
        return LocalDate.parse(xmlGregorianCalendar.toXMLFormat());
    }

}
