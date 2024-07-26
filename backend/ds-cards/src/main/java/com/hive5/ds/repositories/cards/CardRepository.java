package com.hive5.ds.repositories.cards;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds.entities.cards.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
    
}
