package com.hive5.ds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hive5.ds.repositories.cards.CardRepository;
import com.hive5.ds.entities.cards.Card;

@Service
public class CardSOAPService {
    
    @Autowired
    private CardRepository cardRepository;
    
    public Card getCardById(Integer cardId) {
        return cardRepository.findById(cardId).orElse(null);
    }

}
