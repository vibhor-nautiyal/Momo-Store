package com.example.MomoStore.testControllers;

import com.example.MomoStore.controller.UserController;
import com.example.MomoStore.dto.request.*;
import com.example.MomoStore.dto.response.DishResponse;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.User;
import com.example.MomoStore.exception.*;
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

import java.text.ParseException;
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

    @Test
    public void testAddToCart() throws Exception{
        String uri="/user/cart";
        Mockito.when(userService.addToCart(Mockito.any(AddToCartRequest.class))).thenReturn(new UserResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new AddToCartRequest())))
                .andReturn();

        assertEquals(201,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testAddToCart_USERNOTFOUND() throws Exception{
        String uri="/user/cart";
        Mockito.when(userService.addToCart(Mockito.any(AddToCartRequest.class))).thenThrow(new UserNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new AddToCartRequest())))
                .andReturn();
        assertEquals(404,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testAddToCart_DISHNOTFOUND() throws Exception{
        String uri="/user/cart";
        Mockito.when(userService.addToCart(Mockito.any(AddToCartRequest.class))).thenThrow(new DishNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new AddToCartRequest())))
                .andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }


    @Test
    public void testRemoveFromCart() throws Exception{
        String uri="/user/cart-item";
        Mockito.when(userService.removeFromCart((Mockito.any(RemoveFromCartRequest.class)))).thenReturn(new UserResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new RemoveFromCartRequest())))
                .andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testRemoveFromCart_USERNOTFOUND() throws Exception{
        String uri="/user/cart-item";
        Mockito.when(userService.removeFromCart((Mockito.any(RemoveFromCartRequest.class)))).thenThrow(new UserNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new RemoveFromCartRequest())))
                .andReturn();
        assertEquals(404,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testRemoveFromCart_DISHNOTFOUND() throws Exception{
        String uri="/user/cart-item";
        Mockito.when(userService.removeFromCart((Mockito.any(RemoveFromCartRequest.class)))).thenThrow(new DishNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new RemoveFromCartRequest())))
                .andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testUpdateCart() throws Exception{
        String uri="/user/cart-item";
        Mockito.when(userService.updateCartItem(Mockito.any(AddToCartRequest.class))).thenReturn(new UserResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new AddToCartRequest())))
                .andReturn();

        assertEquals(200,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testUpdateCart_USERNOTFOUND() throws Exception{
        String uri="/user/cart-item";
        Mockito.when(userService.updateCartItem(Mockito.any(AddToCartRequest.class))).thenThrow(new UserNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new AddToCartRequest())))
                .andReturn();
        assertEquals(404,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testUpdateCart_DISHNOTFOUND() throws Exception{
        String uri="/user/cart-item";
        Mockito.when(userService.updateCartItem(Mockito.any(AddToCartRequest.class))).thenThrow(new DishNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new AddToCartRequest())))
                .andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testOrder() throws Exception {
        String uri="/user/order/1";
        Mockito.when(userService.checkout(Mockito.anyInt())).thenReturn(new OrderResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(201,mvcResult.getResponse().getStatus());
    }
    @Test
    public void testOrder_ORDERNOTFOUND() throws Exception {
        String uri="/user/order/1";
        Mockito.when(userService.checkout(Mockito.anyInt())).thenThrow(new OrderNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(404,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testOrder_INSUFFICIENT() throws Exception {
        String uri="/user/order/1";
        Mockito.when(userService.checkout(Mockito.anyInt())).thenThrow(new QuantityException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testOrder_WRONGTIME() throws Exception{

        String uri="/user/order/1";
        Mockito.when(userService.checkout(Mockito.anyInt())).thenThrow(new WrongTimeException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testCheckoutScheduled() throws Exception {
        String uri="/user/scheduledOrder";
        Mockito.when(userService.checkoutScheduled(Mockito.any(ScheduledOrderRequest.class))).thenReturn(new OrderResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new ScheduledOrderRequest())))
                .andReturn();
        assertEquals(201,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testCheckoutScheduled_PARSEEXCEPTION() throws Exception {
        String uri="/user/scheduledOrder";
        Mockito.when(userService.checkoutScheduled(Mockito.any(ScheduledOrderRequest.class))).thenThrow(new ParseException("",1));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new ScheduledOrderRequest())))
                .andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testCancel() throws Exception{
        String uri="/user/order/1";
        Mockito.when(userService.cancelOrder(Mockito.anyInt())).thenReturn(new OrderResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }


    @Test
    public void testCancel_ORDERNOTFOUND() throws Exception{
        String uri="/user/order/1";
        Mockito.when(userService.cancelOrder(Mockito.anyInt())).thenThrow(new OrderNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(404,mvcResult.getResponse().getStatus());
    }


    @Test
    public void testCancel_WRONGTIME() throws Exception{
        String uri="/user/order/1";
        Mockito.when(userService.cancelOrder(Mockito.anyInt())).thenThrow(new WrongTimeException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(500,mvcResult.getResponse().getStatus());
    }

    @Test
    public void testComplete() throws Exception {
        String uri="/user/complete/1";
        Mockito.when(userService.received(Mockito.anyInt())).thenReturn(new OrderResponse());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200,mvcResult.getResponse().getStatus());
    }


    @Test
    public void testComplete_ORDERNOTFOUND() throws Exception {
        String uri="/user/complete/1";
        Mockito.when(userService.received(Mockito.anyInt())).thenThrow(new OrderNotFoundException(""));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(404,mvcResult.getResponse().getStatus());
    }
}
