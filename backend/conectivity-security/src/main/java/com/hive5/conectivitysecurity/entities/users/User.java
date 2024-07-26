package com.hive5.conectivitysecurity.entities.users;

import java.util.List;
import java.util.Set;

import com.hive5.conectivitysecurity.entities.boards.Board;
import com.hive5.conectivitysecurity.entities.cards.Card;
import com.hive5.conectivitysecurity.entities.role.Role;

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

  // @Column(name = "username")
  // @NotNull(message = "El usuario no puede ser nulo.")
  // private String username;

  @Column(name = "email")
  @NotBlank(message = "Email is mandatory.")
  private String email;

  @Column(name = "password")
  @NotBlank(message = "Password is mandatory.")
  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @ManyToMany
  @JoinTable(name = "user_board", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "board_id"))
  private Set<Board> boards;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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

  // public String getUsername() {
  // return username;
  // }

  // public void setUsername(String username) {
  // this.username = username;
  // }

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
