package com.example.MomoStore.service;

import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.request.AddToCartRequest;
import com.example.MomoStore.dto.request.NewUserRequest;
import com.example.MomoStore.dto.request.RemoveFromCartRequest;
import com.example.MomoStore.dto.request.UpdateUserRequest;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.*;
import com.example.MomoStore.exception.DishNotFoundException;
import com.example.MomoStore.exception.QuantityException;
import com.example.MomoStore.exception.UserNotFoundException;
import com.example.MomoStore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl {

    private UserRepo userRepo;
    private Transformer transformer;
    private CartItemRepo cartItemRepo;
    private DishRepo dishRepo;
    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo,Transformer transformer,CartItemRepo cartItemRepo,DishRepo dishRepo,OrderRepo orderRepo,OrderItemRepo orderItemRepo){
        this.userRepo=userRepo;
        this.transformer=transformer;
        this.cartItemRepo=cartItemRepo;
        this.dishRepo=dishRepo;
        this.orderRepo=orderRepo;
        this.orderItemRepo=orderItemRepo;
    }

    public UserResponse createUser(NewUserRequest request){

        User user=transformer.newUserRequestToUser(request);
        user=userRepo.save(user);
        UserResponse response=transformer.userToUserResponse(user);
        return response;
    }

    public UserResponse getUserById(Integer id){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User with id "+id+" not found."));
        return transformer.userToUserResponse(user);
    }

    public UserResponse updateUser(UpdateUserRequest request){

        User user=userRepo.findById(request.getId()).orElseThrow(()->new UserNotFoundException("User with id "+request.getId()+" not found."));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setName(request.getName());
        user=userRepo.save(user);
        return transformer.userToUserResponse(user);
    }

    public void deleteUser(Integer id){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User with id "+id+" not found."));
        user.setActive(false);
        userRepo.save(user);
    }

    public List<UserResponse> findAllUsers(){
        Iterable<User> users=userRepo.findAll();
        List<UserResponse> response=new ArrayList<>();
        for(User user:users){
            if(user.getActive())
                response.add(transformer.userToUserResponse(user));
        }
        return response;
    }

    public UserResponse addToCart(AddToCartRequest request){

        Dish dish=dishRepo.findById(request.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+request.getDishId()+" not found."));
        if(dish.getAvailable()< request.getQuantity()){
            throw new QuantityException("Quantity Exceeded for dish id="+request.getDishId());
        }
        User user=userRepo.findById(request.getUserId()).orElseThrow(()->new UserNotFoundException("User with id "+request.getUserId()+" not found."));
        CartItem cartItem=transformer.createCartItem(request);
//        dish.setAvailable(dish.getAvailable()- request.getQuantity());
//        dishRepo.save(dish);
        cartItemRepo.save(cartItem);
        user.getCart().add(cartItem);
        userRepo.save(user);
        return transformer.userToUserResponse(user);
    }

    public UserResponse removeFromCart(RemoveFromCartRequest request){

        cartItemRepo.deleteByUserIdAndDishId(request.getUserId(),request.getDishId());
        return transformer.userToUserResponse(userRepo.findById(request.getUserId()).orElseThrow());
    }

    public OrderResponse checkout(Integer id){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User with id "+id+" not found."));
        List<OrderItem> orderItems=new ArrayList<>();
        Double bill=0.0;
        for(CartItem cartItem:user.getCart()){
            OrderItem orderItem=transformer.cartItemToOrderItem(cartItem);
            orderItemRepo.save(orderItem);
            orderItems.add(orderItem);
            Dish dish=dishRepo.findById(cartItem.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+cartItem.getDishId()+" not found."));
            if(dish.getAvailable()< cartItem.getQuantity()){
                throw new QuantityException("Quantity exceeded for dish id="+dish.getId());
            }
            bill+=dish.getCost()* cartItem.getQuantity();
        }
        Order order=new Order();
        order.setItems(orderItems);
        order.setStatus("submitted");
        order.setTime(new Date());
        order.setPrice(bill);

        orderRepo.save(order);

        user.getHistory().add(order);
        user.getCart().clear();
        userRepo.save(user);

        return transformer.orderToOrderResponse(order);
    }

}
