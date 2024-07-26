package com.hive5.ds_boards.entities.status;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hive5.ds_boards.entities.cards.Card;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "STATUS")
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  @NotEmpty(message = "Name can't be empty.")
  private String name;

  @OneToMany(mappedBy = "statuss", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Card> cards_boards;

  public Status() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Card> getCards() {
    return cards_boards;
  }

  public void setCards(List<Card> cards) {
    this.cards_boards = cards;
  }

}
