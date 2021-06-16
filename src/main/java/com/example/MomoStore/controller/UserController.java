package com.example.MomoStore.controller;

import com.example.MomoStore.dto.request.*;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/new-user")
    public ResponseEntity<UserResponse> createUser(@RequestBody NewUserRequest request){

        UserResponse response=userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id){

        UserResponse response= userService.getUserById(id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers(){

        List<UserResponse> response=userService.findAllUsers();
        if(response.size()==0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<UserResponse> updateUser(@RequestBody  UpdateUserRequest request){
        return new ResponseEntity<>(userService.updateUser(request),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User with id="+id+" deleted",HttpStatus.OK);
    }

    @PostMapping("/cart")
    public ResponseEntity<UserResponse> addToCart(@RequestBody AddToCartRequest request){
        return new ResponseEntity<>(userService.addToCart(request),HttpStatus.CREATED);
    }

    @DeleteMapping("/cart-item")
    public ResponseEntity<UserResponse> removeFromCart(@RequestBody RemoveFromCartRequest request){
        return new ResponseEntity<>(userService.removeFromCart(request),HttpStatus.OK);
    }

    @PatchMapping("/cart-item")
    public ResponseEntity<UserResponse> updateCartItem(@RequestBody AddToCartRequest request){
        return new ResponseEntity<>(userService.updateCartItem(request),HttpStatus.OK);
    }

    @PostMapping("/order/{id}")
    public ResponseEntity<OrderResponse> order(@PathVariable Integer id){

        return new ResponseEntity<>(userService.checkout(id),HttpStatus.CREATED);
    }

    @PostMapping("/scheduledOrder")
    public ResponseEntity<OrderResponse> scheduledOrder(@RequestBody ScheduledOrderRequest request) throws ParseException {
            return new ResponseEntity<>(userService.checkoutScheduled(request),HttpStatus.CREATED);
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Integer id){
        return new ResponseEntity<>(userService.cancelOrder(id),HttpStatus.OK);
    }

    @PatchMapping("/complete/{id}")
    public ResponseEntity<OrderResponse> orderComplete(@PathVariable Integer id){
        return new ResponseEntity<>(userService.received(id),HttpStatus.OK);
    }
}
