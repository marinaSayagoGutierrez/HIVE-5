package com.hive5.ds.controllers;

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

import com.hive5.ds.entities.cards.Card;
import com.hive5.ds.service.CardService;

@RestController
@RequestMapping("/cards")
public class CardController {
    
   @Autowired
   private CardService cardService;

   @GetMapping
   public List<Card> getAllCards() {
    return cardService.getAllCards();
   }

   @GetMapping("/{id}")
   public ResponseEntity<Card> getCardById(@PathVariable Integer id) {
    Optional<Card> card = cardService.getCardById(id);
    return card.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
   }

   @PostMapping
   public Card createCard(@RequestBody Card card) {
    return cardService.saveCard(card);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteCard(@PathVariable Integer id) {
    cardService.deleteCard(id);
    return ResponseEntity.noContent().build();
   }

   @PutMapping("/{id}")
   public ResponseEntity<Card> updateCard(@PathVariable Integer id, @RequestBody Card cardDetails) {
    Optional<Card> cardOptional = cardService.getCardById(id);
    if (cardOptional.isPresent()) {
        Card card = cardOptional.get();
        card.setTitle(cardDetails.getTitle());
        card.setDescription(cardDetails.getDescription());
        card.setStartDate(cardDetails.getStartDate());
        card.setEndDate(cardDetails.getEndDate());
        card.setPriority(cardDetails.getPriority());
        card.setUser(cardDetails.getUser());
        card.setBoard(cardDetails.getBoard());
        card.setStatus(cardDetails.getStatus());
        return ResponseEntity.ok(cardService.updateCard(card));
    } else {
        return ResponseEntity.notFound().build();
    }
   }

}
