package com.example.MomoStore.service;

import com.example.MomoStore.MomoStoreApplication;
import com.example.MomoStore.dto.request.UpdateDishRequest;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.entity.Dish;
import com.example.MomoStore.exception.DishNotFoundException;
import com.example.MomoStore.repository.DishRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl  implements AdminService{

    private DishRepo dishRepo;
    private Transformer transformer;
    @Autowired
    public AdminServiceImpl(DishRepo dishRepo,Transformer transformer){
        this.dishRepo=dishRepo;
        this.transformer=transformer;
    }

    public DishResponse addNewDish(NewDishRequest newDishRequest){
        Dish dish=transformer.dishDetailsToDish(newDishRequest);
        dish=dishRepo.save(dish);
        log.info("New dish added");
        return transformer.dishToDishResponse(dish);
    }

    public DishResponse updateDish(UpdateDishRequest request){
        Dish dish=dishRepo.findById(request.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+request.getDishId()+" not found."));
        dish.setAvailable(request.getAvailable());
        dish.setCost(request.getCost());
        dish=dishRepo.save(dish);
        log.info("Dish updated");
        return transformer.dishToDishResponse(dish);
    }

    public void removeDish(Integer id){
        Dish dish=dishRepo.findById(id).orElseThrow(()->new DishNotFoundException("Dish with id "+id+" not found."));
        dish.setActive(false);
        dishRepo.save(dish);
        log.info("Dish removed");
    }

    public List<DishResponse> getAllDishes(){
        Iterable<Dish> dishes=dishRepo.findAll();
        List<DishResponse> responses=new ArrayList<>();
        for(Dish dish:dishes){
            if(dish.getActive())
               responses.add(transformer.dishToDishResponse(dish));
        }
        log.info("Fetching all dishes");
        return responses;
    }

}
