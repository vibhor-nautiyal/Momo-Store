package com.example.MomoStore.controller;

import com.example.MomoStore.dto.request.UpdateDishRequest;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService=adminService;
    }

    @GetMapping("/dishes")
    public ResponseEntity<List<DishResponse>> getAllDishes(){


        List<DishResponse> response=adminService.getAllDishes();
        if(response.size()==0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/dish")
    public ResponseEntity<DishResponse> addNewDish(@RequestBody  NewDishRequest request){
//        return ResponseEntity.of(Optional.of(adminService.addNewDish(request)));
        return new ResponseEntity<>(adminService.addNewDish(request),HttpStatus.CREATED);
    }

    @PatchMapping("/quantity")
    public ResponseEntity<DishResponse> changeAvailabilty(@RequestBody UpdateDishRequest request){
        return new ResponseEntity<>(adminService.updateDish(request),HttpStatus.OK);
    }

    @DeleteMapping("/dish/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable Integer id){
        adminService.removeDish(id);
        return new ResponseEntity<>("Dish with id="+id+" deleted",HttpStatus.OK);
    }
}
