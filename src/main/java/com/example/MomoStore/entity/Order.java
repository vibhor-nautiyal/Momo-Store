package com.example.MomoStore.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "AllOrders")
public class Order {

    @Column(name="orderId")
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    Integer id;

    @Column
    Integer userId;

    @Column
    Date date;

    @Column
    Boolean scheduled;

    @Column
    Integer scheduledTime;

    @Column
    String status;

    @ManyToMany
    List<Dish> items;
}
