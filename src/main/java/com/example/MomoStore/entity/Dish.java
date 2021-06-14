package com.example.MomoStore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Dish {

    @Column(name="dishId")
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    Integer id;

    @Column
    String name;

    @Column
    Integer available;

    @Column
    Double cost;

    @ManyToMany(mappedBy = "cart")
    List<User> userList;
}
