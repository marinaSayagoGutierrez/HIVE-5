package com.hive5.ds_boards.entities.boards;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hive5.ds_boards.entities.cards.Card;
import com.hive5.ds_boards.entities.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "BOARD")
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty(message = "Name can't be empty")
    private String name;

    @ManyToMany@JoinTable(
        name = "user_board",
        joinColumns = @JoinColumn(name = "board_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private Set<User> users;
    
    @OneToMany(mappedBy = "boards",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Card> cards;

    public Board() {}

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    
}
