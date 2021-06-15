package com.example.MomoStore.controller;

import com.example.MomoStore.dto.request.NewUserRequest;
import com.example.MomoStore.dto.request.UpdateUserRequest;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.service.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    UserServiceImpl userService;
    UserController(UserServiceImpl userService){
        this.userService=userService;
    }

    @PostMapping("/new-user")
    public UserResponse createUser(@RequestBody NewUserRequest request){
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @GetMapping("/all")
    public List<UserResponse> getAllUsers(){
        return userService.findAllUsers();
    }

    @PatchMapping("")
    public UserResponse updateUser(@RequestBody  UpdateUserRequest request){
        return userService.updateUser(request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }
}
