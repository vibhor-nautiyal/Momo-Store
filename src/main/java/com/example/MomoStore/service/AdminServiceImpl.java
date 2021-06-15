package com.example.MomoStore.service;

import com.example.MomoStore.dto.request.ChangeAvailabilityRequest;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.entity.Dish;
import com.example.MomoStore.repository.DishRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl {

    DishRepo dishRepo;
    Transformer transformer;
    @Autowired
    public AdminServiceImpl(DishRepo dishRepo,Transformer transformer){
        this.dishRepo=dishRepo;
        this.transformer=transformer;
    }

    public DishResponse addNewDish(NewDishRequest newDishRequest){
        Dish dish=transformer.dishDetailsToDish(newDishRequest);
        dish=dishRepo.save(dish);
        return transformer.dishToDishResponse(dish);
    }

    public DishResponse changeAvailability(ChangeAvailabilityRequest request){
        Dish dish=dishRepo.findById(request.getDishId()).orElseThrow();
        dish.setAvailable(request.getAvailable());
        dish=dishRepo.save(dish);
        return transformer.dishToDishResponse(dish);
    }

    public void removeDish(Integer id){
        Dish dish=dishRepo.findById(id).orElseThrow();
        dish.setActive(false);
        dishRepo.save(dish);
    }

    public List<DishResponse> getAllDishes(){
        Iterable<Dish> dishes=dishRepo.findAll();
        List<DishResponse> response=new ArrayList<>();
        for(Dish dish:dishes){
            if(dish.getActive())
               response.add(transformer.dishToDishResponse(dish));
        }
        return response;
    }

}
