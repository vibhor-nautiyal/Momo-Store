package com.example.MomoStore.service;

import com.example.MomoStore.dto.request.*;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import java.text.ParseException;
import java.util.List;

public interface UserService {
    UserResponse createUser(NewUserRequest request);
    UserResponse getUserById(Integer id);
    UserResponse updateUser(UpdateUserRequest request);
    void deleteUser(Integer id);
    List<UserResponse> findAllUsers();
    UserResponse addToCart(AddToCartRequest request);
    UserResponse updateCartItem(AddToCartRequest request);
    UserResponse removeFromCart(RemoveFromCartRequest request);
    OrderResponse checkout(Integer id);
    OrderResponse checkoutScheduled(ScheduledOrderRequest request) throws ParseException;
    void processOrders();
    OrderResponse cancelOrder(Integer id);
    OrderResponse received(Integer orderId);
}
