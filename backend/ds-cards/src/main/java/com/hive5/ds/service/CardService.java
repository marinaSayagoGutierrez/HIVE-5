package com.hive5.ds.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hive5.ds.entities.cards.Card;
import com.hive5.ds.repositories.cards.CardRepository;

@Service
public class CardService {
    
    @Autowired
    private CardRepository cardRepository;

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardById(Integer id) {
        return cardRepository.findById(id);
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public void deleteCard(Integer id) {
        cardRepository.deleteById(id);
    }

    public Card updateCard(Card card) {
        return cardRepository.save(card);
    }
}
