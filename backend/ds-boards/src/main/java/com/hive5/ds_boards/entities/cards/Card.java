package com.hive5.ds_boards.entities.cards;

import java.time.LocalDate;
import java.util.List;

import com.hive5.ds_boards.entities.boards.Board;
import com.hive5.ds_boards.entities.checklist_item.Checklist_Item;
import com.hive5.ds_boards.entities.status.Status;
import com.hive5.ds_boards.entities.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "CARD")
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    @NotBlank(message = "title can't be blank")
    private String title;

    @Column(name = "description")
    @NotBlank(message = "description can't be blank")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "priority")
    @NotNull(message = "priority can't be null")
    //Value= 0 = hold| 1 = baja| 2 = media | 3 = alta
    @Min(value = 0)
    @Max(value = 3)
    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board boards;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status statuss;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User users;

    @OneToMany(mappedBy = "cards", cascade = CascadeType.ALL)
    private List<Checklist_Item> checklist_Items;

    public Card(){}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public User getUser() {
        return users;
    }

    public void setUser(User user) {
        this.users = user;
    }

    public Board getBoard() {
        return boards;
    }

    public void setBoard(Board board) {
        this.boards = board;
    }

    public Status getStatus() {
        return statuss;
    }

    public void setStatus(Status status) {
        this.statuss = status;
    }

}
