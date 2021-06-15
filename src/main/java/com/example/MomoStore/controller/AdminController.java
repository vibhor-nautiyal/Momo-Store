package com.example.MomoStore.controller;

import com.example.MomoStore.dto.request.ChangeAvailabilityRequest;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminServiceImpl adminService;

    @Autowired
    public AdminController(AdminServiceImpl adminService){
        this.adminService=adminService;
    }

    @GetMapping("/dishes")
    public List<DishResponse> getAllDishes(){
        return adminService.getAllDishes();
    }

    @PostMapping("/dish")
    public DishResponse addNewDish(@RequestBody  NewDishRequest request){
        return adminService.addNewDish(request);
    }

    @PatchMapping("/quantity")
    public DishResponse changeAvailabilty(@RequestBody ChangeAvailabilityRequest request){
        return adminService.changeAvailability(request);
    }

    @DeleteMapping("/dish/{id}")
    public void deleteDish(@PathVariable Integer id){
        adminService.removeDish(id);
    }
}
