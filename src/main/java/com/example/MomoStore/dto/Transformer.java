package com.example.MomoStore.dto;

import com.example.MomoStore.dto.request.AddToCartRequest;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.request.NewUserRequest;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Transformer {

    public Dish dishDetailsToDish(NewDishRequest newDishRequest){
        Dish dish=new Dish();
        dish.setName(newDishRequest.getName());
        dish.setCost(newDishRequest.getCost());
        dish.setAvailable(10);
        dish.setActive(true);
        return dish;
    }

    public DishResponse dishToDishResponse(Dish dish){
        DishResponse dishResponse=new DishResponse();
        dishResponse.setDishId(dish.getId());
        dishResponse.setDishName(dish.getName());
        dishResponse.setAvailable(dish.getAvailable());
        dishResponse.setDishCost(dish.getCost());
        return dishResponse;
    }

    public User newUserRequestToUser(NewUserRequest request) {
        User user=new User();
        user.setActive(true);
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setCart(new ArrayList<>());
        user.setHistory(new ArrayList<>());
        user.setPhone(request.getPhone());
        return user;
    }

    public UserResponse userToUserResponse(User user) {
        UserResponse response=new UserResponse();
        response.setAddress(user.getAddress());
        response.setId(user.getId());
        response.setCart(user.getCart());
        response.setHistory(user.getHistory());
        response.setPhone(user.getPhone());
        response.setName(user.getName());
        return response;
    }

    public CartItem createCartItem(AddToCartRequest request) {
        CartItem cartItem=new CartItem();
        cartItem.setDishId(request.getDishId());
        cartItem.setQuantity(request.getQuantity());
        return cartItem;
    }

    public OrderItem cartItemToOrderItem(CartItem cartItem){
        OrderItem orderItem=new OrderItem();
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setDishId(cartItem.getDishId());
        return orderItem;
    }

    public OrderResponse orderToOrderResponse(Order order) {
        OrderResponse response=new OrderResponse();
//        response.setOrderId(order.getId());
        response.setTime(order.getTime());
        response.setScheduledTime(order.getScheduledTime());
        response.setPrice(order.getPrice());
        response.setStatus(order.getStatus());
        return response;
    }
}
