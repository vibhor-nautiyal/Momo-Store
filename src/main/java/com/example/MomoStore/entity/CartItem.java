package com.example.MomoStore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CartItem {

    @Column(name="itemId")
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    Integer id;

    @Column
    Integer dishId;

    @Column
    Integer quantity;

}
