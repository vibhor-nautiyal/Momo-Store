package com.example.MomoStore.testControllers;


import com.example.MomoStore.controller.AdminController;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.request.UpdateDishRequest;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.exception.DishNotFoundException;
import com.example.MomoStore.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc
public class TestAdminController {

    @MockBean
    AdminService adminService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testGetAllDishes() throws Exception {

        String uri="/admin/dishes";

        Mockito.when(adminService.getAllDishes()).thenReturn(Arrays.asList(new DishResponse()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAllDishes_NOTFOUND() throws Exception {

        String uri="/admin/dishes";

        Mockito.when(adminService.getAllDishes()).thenReturn(new ArrayList<>());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddNewDish() throws Exception {

        String uri="/admin/dish";

        NewDishRequest request=new NewDishRequest();
        request.setCost(10.0);
        request.setName("Dish");

        Mockito.when(adminService.addNewDish(Mockito.any(NewDishRequest.class))).thenReturn(new DishResponse());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request))).andReturn();

        assertEquals(201,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddNewDish_InvalidArgument() throws Exception {

        String uri="/admin/dish";

        NewDishRequest request=new NewDishRequest();
        request.setCost(10.0);

        Mockito.when(adminService.addNewDish(Mockito.any(NewDishRequest.class))).thenReturn(new DishResponse());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request))).andReturn();

        assertEquals(400,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testAddNewDish_DuplicateData() throws Exception {

        String uri="/admin/dish";

        NewDishRequest request=new NewDishRequest();
        request.setCost(10.0);
        request.setName("Dish");

        Mockito.when(adminService.addNewDish(Mockito.any(NewDishRequest.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry found"));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request))).andReturn();

        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateDish() throws Exception {
        String uri="/admin/dish";
        Mockito.when(adminService.updateDish(Mockito.any(UpdateDishRequest.class))).thenReturn(new DishResponse());

        UpdateDishRequest request=new UpdateDishRequest();
        request.setDishId(1);
        request.setAvailable(10);
        request.setCost(10.0);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request))).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateDish_DishNotFound() throws Exception {
        String uri="/admin/dish";
        Mockito.when(adminService.updateDish(Mockito.any(UpdateDishRequest.class))).thenThrow(new DishNotFoundException("Dish not found"));

        UpdateDishRequest request=new UpdateDishRequest();
        request.setDishId(1);
        request.setAvailable(10);
        request.setCost(10.0);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request))).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteDish() throws Exception{
        String uri="/admin/dish/1";

        Mockito.doNothing().when(adminService).removeDish(Mockito.anyInt());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }


    @Test
    public void testDeleteDish_DishNotFound() throws Exception{
        String uri="/admin/dish/1";
        Mockito.doThrow(new DishNotFoundException("")).when(adminService).removeDish(Mockito.anyInt());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

}
