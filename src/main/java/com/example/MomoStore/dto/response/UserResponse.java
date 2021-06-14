package com.example.MomoStore.dto.response;

import com.example.MomoStore.entity.CartItem;
import com.example.MomoStore.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    Integer id;
    String name;
    Long phone;
    String address;
    List<CartItem> cart;
    List<Order> history;
}
