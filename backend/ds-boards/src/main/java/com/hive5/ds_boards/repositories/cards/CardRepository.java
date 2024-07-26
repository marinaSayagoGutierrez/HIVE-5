package com.hive5.ds_boards.repositories.cards;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds_boards.entities.cards.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
    
}
