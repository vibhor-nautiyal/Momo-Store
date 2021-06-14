package com.example.MomoStore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Column(name="userId")
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    Integer id;

    @Column
    String name;

    @Column
    String address;

    @Column
    Long phone;

    @Column
    Boolean active;

    @JoinColumn(name = "userId")
    @OneToMany
    List<CartItem> cart;

    @JoinColumn(name = "userId")
    @OneToMany
    List<Order> history;
}
