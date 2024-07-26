package com.hive5.conectivitysecurity.entities.checklist_item;

import com.hive5.conectivitysecurity.entities.cards.Card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "CHECKLIST_ITEM")
public class Checklist_Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty(message = "Name can't be empty.")
    private String name;

    @Column(name = "completed")
    @Min(value = 0)
    @Max(value = 1)
    private Short completed;

    @Column(name = "task_id")
    @NotNull(message = "task_id can't be null.")
    private Integer task_id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card cards;

}
