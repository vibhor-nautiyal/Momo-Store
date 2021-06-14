package com.example.MomoStore.controller;

import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    AdminServiceImpl adminService;

    @Autowired
    AdminController(AdminServiceImpl adminService){
        this.adminService=adminService;
    }

    @GetMapping("/dishes")
    public List<DishResponse> getAllDishes(){
        return adminService.getAllDishes();
    }
}
