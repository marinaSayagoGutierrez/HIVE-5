package com.hive5.ds_boards.entities.users;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hive5.ds_boards.entities.boards.Board;
import com.hive5.ds_boards.entities.cards.Card;
import com.hive5.ds_boards.entities.role.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USER", schema = "hive5")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name")
  @NotNull(message = "Name can't be null.")
  private String firstName;

  @Column(name = "last_name")
  @NotNull(message = "El apellido no puede ser nulo.")
  private String lastName;

  @Column(name = "email")
  @NotBlank(message = "Email is mandatory.")
  private String email;

  @Column(name = "password")
  @NotBlank(message = "Password is mandatory.")
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  @NotEmpty
  private Role role;

  @JsonIgnore
  @ManyToMany(mappedBy = "users")
  private Set<Board> boards;

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Card> cards;

  public User() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public Set<Board> getBoards() {
    return boards;
  }

  public void setBoards(Set<Board> boards) {
    this.boards = boards;
  }

  public List<Card> getCards() {
    return cards;
  }

  public void setCards(List<Card> cards) {
    this.cards = cards;
  }

}
