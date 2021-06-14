package com.example.MomoStore.service;

import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.request.NewUserRequest;
import com.example.MomoStore.dto.request.UpdateUserRequest;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.User;
import com.example.MomoStore.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl {

    UserRepo userRepo;
    Transformer transformer;
    UserServiceImpl(UserRepo userRepo,Transformer transformer){
        this.userRepo=userRepo;
        this.transformer=transformer;
    }

    public UserResponse createUser(NewUserRequest request){

        User user=transformer.newUserRequestToUser(request);
        user=userRepo.save(user);
        UserResponse response=transformer.userToUserResponse(user);
        return response;
    }

    public UserResponse getUserById(Integer id){
        User user=userRepo.findById(id).orElseThrow();
        return transformer.userToUserResponse(user);
    }

    public UserResponse updateUser(UpdateUserRequest request){

        User user=userRepo.findById(request.getId()).orElseThrow();
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setName(request.getName());
        return transformer.userToUserResponse(user);
    }

    public void deleteUser(Integer id){
        User user=userRepo.findById(id).orElseThrow();
        user.setActive(false);
        userRepo.save(user);
    }

    public List<UserResponse> findAllUsers(){
        Iterable<User> users=userRepo.findAll();
        List<UserResponse> response=new ArrayList<>();
        for(User user:users){
            response.add(transformer.userToUserResponse(user));
        }
        return response;
    }

}
