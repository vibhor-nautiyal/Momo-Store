package com.example.MomoStore.service;

import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.request.UpdateDishRequest;
import com.example.MomoStore.dto.response.DishResponse;

import java.util.List;

public interface AdminService{
    DishResponse addNewDish(NewDishRequest newDishRequest);
    DishResponse updateDish(UpdateDishRequest request);
    void removeDish(Integer id);
    List<DishResponse> getAllDishes();
}
