package com.example.MomoStore.testControllers;

import com.example.MomoStore.controller.UserController;
import com.example.MomoStore.dto.request.NewDishRequest;
import com.example.MomoStore.dto.request.NewUserRequest;
import com.example.MomoStore.dto.request.UpdateDishRequest;
import com.example.MomoStore.dto.request.UpdateUserRequest;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.User;
import com.example.MomoStore.exception.UserNotFoundException;
import com.example.MomoStore.service.UserService;
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
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class TestUserController {

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception{
        String uri="/user/new-user";
        Mockito.when(userService.createUser(Mockito.any(NewUserRequest.class))).thenReturn(new UserResponse());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(new NewDishRequest()))).andReturn();

        assertEquals(201,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testCreateUser_DuplicateData() throws Exception{
        String uri="/user/new-user";
        Mockito.when(userService.createUser(Mockito.any(NewUserRequest.class))).thenThrow(new DataIntegrityViolationException(""));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(new NewDishRequest()))).andReturn();

        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetUserById() throws Exception{
        String uri="/user/1";
        Mockito.when(userService.getUserById(Mockito.anyInt())).thenReturn(new UserResponse());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetUserById_NOTFOUND() throws Exception{
        String uri="/user/1";
        Mockito.when(userService.getUserById(Mockito.anyInt())).thenThrow(new UserNotFoundException(""));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAllUsers() throws Exception{
        String uri="/user/all";
        Mockito.when(userService.findAllUsers()).thenReturn(Arrays.asList(new UserResponse()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testGetAllUsers_NOTFOUND() throws Exception{
        String uri="/user/all";
        Mockito.when(userService.findAllUsers()).thenReturn(new ArrayList<>());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateUser() throws Exception{
        String uri="/user/";
        Mockito.when(userService.updateUser(Mockito.any(UpdateUserRequest.class))).thenReturn(new UserResponse());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(new UpdateDishRequest()))).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateUser_NOTFOUND() throws Exception{
        String uri="/user/";
        Mockito.when(userService.updateUser(Mockito.any(UpdateUserRequest.class))).thenThrow(new UserNotFoundException(""));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(new UpdateDishRequest()))).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }


    @Test
    public void testDeleteUser() throws Exception{
        String uri="/user/1";
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyInt());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testDeleteUser_NOTFOUND() throws Exception{
        String uri="/user/1";
        Mockito.doThrow(new UserNotFoundException("")).when(userService).deleteUser(Mockito.anyInt());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }
}
