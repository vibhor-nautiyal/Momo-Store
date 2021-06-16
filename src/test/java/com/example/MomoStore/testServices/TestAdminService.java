package com.example.MomoStore.testServices;

import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.request.UpdateDishRequest;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.entity.Dish;
import com.example.MomoStore.exception.DishNotFoundException;
import com.example.MomoStore.repository.DishRepo;
import com.example.MomoStore.service.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TestAdminService {

    @Mock
    private DishRepo dishRepo;

    @Mock
    private Transformer transformer;

    @InjectMocks
    AdminServiceImpl adminService;

    @Test
    public void testAddNewDish(){

        NewDishRequest request=new NewDishRequest();
        request.setName("Momo");
        request.setCost(10.0);

        Dish dish=new Dish();
        dish.setActive(true);
        dish.setAvailable(10);
        dish.setCost(10.0);
        dish.setName("Momo");

        DishResponse response=new DishResponse();
        response.setDishCost(10.0);
        response.setDishName("Momo");
        response.setAvailable(10);

        Mockito.when(transformer.dishDetailsToDish(Mockito.any(NewDishRequest.class))).thenCallRealMethod();
        Mockito.when(transformer.dishToDishResponse(Mockito.any(Dish.class))).thenCallRealMethod();
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);
        assertEquals(response.getAvailable(),adminService.addNewDish(request).getAvailable());
        assertEquals(response.getDishName(),adminService.addNewDish(request).getDishName());
        assertEquals(response.getDishCost(),adminService.addNewDish(request).getDishCost());
    }

    @Test
    public void testupdateDish(){

        UpdateDishRequest request=new UpdateDishRequest();
        request.setDishId(1);
        request.setAvailable(10);
        request.setCost(10.0);

        Dish dish=new Dish();
        dish.setActive(true);
        dish.setAvailable(10);
        dish.setCost(10.0);
        dish.setName("Momo");

        DishResponse response=new DishResponse();
        response.setDishCost(10.0);
        response.setDishName("Momo");
        response.setAvailable(10);

//        Mockito.when(transformer.dishDetailsToDish(Mockito.any(NewDishRequest.class))).thenReturn(dish);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(transformer.dishToDishResponse(Mockito.any(Dish.class))).thenCallRealMethod();
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);

        DishResponse actual=adminService.updateDish(request);
        assertEquals(response.getAvailable(),actual.getAvailable());
        assertEquals(response.getDishName(),actual.getDishName());
        assertEquals(response.getDishCost(),actual.getDishCost());
    }

    @Test
    public void testupdateDish_DishNotFound(){

        UpdateDishRequest request=new UpdateDishRequest();
        request.setDishId(1);
        request.setAvailable(10);
        request.setCost(10.0);

        Dish dish=new Dish();
        dish.setActive(true);
        dish.setAvailable(10);
        dish.setCost(10.0);
        dish.setName("Momo");

        DishResponse response=new DishResponse();
        response.setDishCost(10.0);
        response.setDishName("Momo");
        response.setAvailable(10);

//        Mockito.when(transformer.dishDetailsToDish(Mockito.any(NewDishRequest.class))).thenReturn(dish);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(transformer.dishToDishResponse(Mockito.any(Dish.class))).thenCallRealMethod();
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);

        assertThrows(DishNotFoundException.class,()->adminService.updateDish(request));
//        assertEquals(response.getAvailable(),adminService.updateDish(request).getAvailable());
//        assertEquals(response.getDishName(),adminService.updateDish(request).getDishName());
//        assertEquals(response.getDishCost(),adminService.updateDish(request).getDishCost());
    }

    @Test
    public void testRemoveDish(){
        Dish dish=new Dish();
        dish.setActive(true);
        dish.setAvailable(10);
        dish.setCost(10.0);
        dish.setName("Momo");

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);

        adminService.removeDish(1);

    }


    @Test
    public void testRemoveDish_DishNotFound(){
        Dish dish=new Dish();
        dish.setActive(true);
        dish.setAvailable(10);
        dish.setCost(10.0);
        dish.setName("Momo");

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);

        assertThrows(DishNotFoundException.class,()->adminService.removeDish(1));

    }

    @Test
    public void testGetAllDishes(){
        Dish dish=new Dish();
        dish.setActive(true);
        dish.setAvailable(10);
        dish.setCost(10.0);
        dish.setName("Momo");

        DishResponse response=new DishResponse();
        response.setDishCost(10.0);
        response.setDishName("Momo");
        response.setAvailable(10);

        List<Dish> dishes=new ArrayList<>();
        dishes.add(dish);
        Iterable<Dish> dishIterable=dishes;

        Mockito.when(dishRepo.findAll()).thenReturn(dishIterable);
        Mockito.when(transformer.dishToDishResponse(Mockito.any(Dish.class))).thenCallRealMethod();

        DishResponse actual=adminService.getAllDishes().get(0);

        assertEquals(response.getDishCost(),actual.getDishCost());
        assertEquals(response.getDishName(),actual.getDishName());

    }

}
