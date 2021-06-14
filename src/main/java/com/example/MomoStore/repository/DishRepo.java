package com.example.MomoStore.repository;

import com.example.MomoStore.entity.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepo extends CrudRepository<Dish,Integer> {

}
