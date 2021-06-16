package com.example.MomoStore.testServices;

import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.request.*;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.*;
import com.example.MomoStore.exception.*;
import com.example.MomoStore.repository.*;
import com.example.MomoStore.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.text.ParseException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TestUserService {

    @Mock
    private UserRepo userRepo;
    @Mock
    private Transformer transformer;
    @Mock
    private CartItemRepo cartItemRepo;
    @Mock
    private DishRepo dishRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private OrderItemRepo orderItemRepo;

    @InjectMocks
    UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setup(){
        user=new User();
        user.setActive(true);
        user.setAddress("India");
        user.setPhone(9999L);
        user.setName("ABC");
    }

    @Test
    public void testCreateUser(){

        UserResponse response=new UserResponse();
        response.setName("ABC");
        response.setAddress("India");
        response.setPhone(9999L);

        NewUserRequest request=new NewUserRequest();
        request.setName("ABC");
        request.setAddress("India");
        request.setPhone(9999L);

        Mockito.when(transformer.newUserRequestToUser(Mockito.any(NewUserRequest.class))).thenCallRealMethod();
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(transformer.userToUserResponse(Mockito.any(User.class))).thenCallRealMethod();

        UserResponse actual=userService.createUser(request);
        assertEquals(response.getAddress(),actual.getAddress());
        assertEquals(response.getName(),actual.getName());
        assertEquals(response.getPhone(),actual.getPhone());
    }

    @Test
    public void testGetUserById(){

        UserResponse response=new UserResponse();
        response.setName("ABC");
        response.setAddress("India");
        response.setPhone(9999L);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.userToUserResponse(Mockito.any(User.class))).thenCallRealMethod();

        UserResponse actual=userService.getUserById(1);
        assertEquals(response.getName(),actual.getName());
        assertEquals(response.getPhone(),actual.getPhone());
    }
    @Test
    public void testGetUserById_UserNotFound(){

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.getUserById(1));
    }

    @Test
    public void testUpdateUser(){
        UserResponse response=new UserResponse();
        response.setName("ABC");
        response.setAddress("India");
        response.setPhone(9999L);

        UpdateUserRequest request=new UpdateUserRequest();
        request.setName("ABC");
        request.setId(1);
        request.setAddress("India");
        request.setPhone(9999L);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(userRepo.save(user)).thenReturn(user);
        Mockito.when(transformer.userToUserResponse(user)).thenCallRealMethod();
        UserResponse actual=userService.updateUser(request);
        assertEquals(response.getName(),actual.getName());
        assertEquals(response.getPhone(),actual.getPhone());
    }

    @Test
    public void testUpdateUser_UserNotFound(){
        UpdateUserRequest request=new UpdateUserRequest();

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.updateUser(request));
    }

    @Test
    public void testDeleteUser(){

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);

        userService.deleteUser(1);
    }

    @Test
    public void testDeleteUser_UserNotFound(){
        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.deleteUser(1));
    }

    @Test
    public void testFindAllUsers(){

        List<User> users=new ArrayList<>();
        users.add(user);
        Iterable<User> iterable=users;

        Mockito.when(userRepo.findAll()).thenReturn(iterable);
        Mockito.when(transformer.userToUserResponse(Mockito.any(User.class))).thenCallRealMethod();

        assertEquals(users.size(),userService.findAllUsers().size());
    }

    @Test
    public void testAddToCart(){

        user.setCart(new ArrayList<>());

        UserResponse response=new UserResponse();
        response.setName("ABC");
        response.setAddress("India");
        response.setPhone(9999L);

        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        CartItem cartItem=new CartItem();

        Dish dish=new Dish();
        dish.setAvailable(10);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.createCartItem(Mockito.any(AddToCartRequest.class))).thenCallRealMethod();
        Mockito.when(cartItemRepo.save(Mockito.any(CartItem.class))).thenReturn(cartItem);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(transformer.userToUserResponse(user)).thenCallRealMethod();

        assertEquals(response.getPhone(),userService.addToCart(request).getPhone());

    }

    @Test
    public void testAddToCart_Insufficient(){


        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        Dish dish=new Dish();
        dish.setAvailable(0);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        assertThrows(QuantityException.class,()->userService.addToCart(request));
    }

    @Test
    public void testAddToCart_UserNotFound(){


        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        Dish dish=new Dish();
        dish.setAvailable(10);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.addToCart(request));
    }

    @Test
    public void testAddToCart_DishNotFound(){

        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class,()->userService.addToCart(request));
    }

    @Test
    public void testUpdateCartItem(){

        UserResponse response=new UserResponse();
        response.setName("ABC");
        response.setAddress("India");
        response.setPhone(9999L);

        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        Dish dish=new Dish();
        dish.setAvailable(10);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.createCartItem(Mockito.any(AddToCartRequest.class))).thenCallRealMethod();
        Mockito.when(cartItemRepo.findByUserIdAndDishId(Mockito.anyInt(),Mockito.anyInt())).thenReturn(cartItem);
        Mockito.when(cartItemRepo.save(Mockito.any(CartItem.class))).thenReturn(cartItem);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(transformer.userToUserResponse(user)).thenCallRealMethod();

        assertEquals(response.getPhone(),userService.updateCartItem(request).getPhone());

    }

    @Test
    public void testUpdateCartItem_Insufficient(){

        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        Dish dish=new Dish();
        dish.setAvailable(0);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));

        assertThrows(QuantityException.class,()->userService.updateCartItem(request));

    }

    @Test
    public void testUpdateCartItem_DishNotFound(){

        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class,()->userService.updateCartItem(request));

    }

    @Test
    public void testUpdateCartItem_UserNotFound(){

        AddToCartRequest request=new AddToCartRequest();
        request.setUserId(1);
        request.setDishId(1);
        request.setQuantity(10);

        Dish dish=new Dish();
        dish.setAvailable(10);

        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.updateCartItem(request));

    }

    @Test
    public void testRemoveFromCart(){

        RemoveFromCartRequest request=new RemoveFromCartRequest();
        request.setDishId(100);
        request.setUserId(100);

        UserResponse response=new UserResponse();
        response.setName("ABC");
        response.setAddress("India");
        response.setPhone(9999L);

        Mockito.when(transformer.userToUserResponse(Mockito.any(User.class))).thenCallRealMethod();
        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        assertEquals(user.getName(),userService.removeFromCart(request).getName());
    }

    @Test
    public void testRemoveFromCart_UserNotFound(){

        RemoveFromCartRequest request=new RemoveFromCartRequest();
        request.setDishId(100);
        request.setUserId(100);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.removeFromCart(request));
    }

    @Test
    public void testCheckout(){

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        cartItem.setDishId(1);

        User user=new User();
        user.setActive(true);
        user.setAddress("India");
        user.setPhone(9999L);
        user.setName("ABC");
        user.setCart(new ArrayList<>());
        user.getCart().add(cartItem);
        user.setHistory(new ArrayList<>());

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Dish dish=new Dish();
        dish.setAvailable(10);
        dish.setCost(100.0);

        Order order=new Order();
        order.setStatus("active");
        order.setPrice(100.0);
        order.setTime(new Date());
        order.setItems(new ArrayList<>());

        OrderResponse response=new OrderResponse();
        response.setStatus("active");
        response.setPrice(100.0);
        response.setTime(new Date());

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.cartItemToOrderItem(Mockito.any(CartItem.class))).thenCallRealMethod();
        Mockito.when(orderItemRepo.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(transformer.orderToOrderResponse(Mockito.any(Order.class))).thenCallRealMethod();

        assertEquals(response.getPrice(),userService.checkout(1).getPrice());

    }

    @Test
    public void testCheckout_WrongTime(){

        assertThrows(WrongTimeException.class,()->userService.checkout(1));

    }

    @Test
    public void testCheckout_Insufficient(){

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        cartItem.setDishId(1);

        user.setCart(new ArrayList<>());
        user.getCart().add(cartItem);
        user.setHistory(new ArrayList<>());

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Dish dish=new Dish();
        dish.setAvailable(0);
        dish.setCost(10.0);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.cartItemToOrderItem(Mockito.any(CartItem.class))).thenCallRealMethod();
        Mockito.when(orderItemRepo.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));

        assertThrows(QuantityException.class,()->userService.checkout(1));

    }

    @Test
    public void testCheckout_UserNotFound(){

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.checkout(1));

    }

    @Test
    public void testCheckout_DishNotFound(){

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        cartItem.setDishId(1);

        user.setCart(new ArrayList<>());
        user.getCart().add(cartItem);
        user.setHistory(new ArrayList<>());

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.cartItemToOrderItem(Mockito.any(CartItem.class))).thenCallRealMethod();
        Mockito.when(orderItemRepo.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class,()->userService.checkout(1));

    }

    @Test
    public void testCheckoutScheduled() throws ParseException {

        ScheduledOrderRequest request=new ScheduledOrderRequest();
        request.setUserId(1);
        request.setScheduledTime("Wed Jun 16 13:00:00 GMT 2021");

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        cartItem.setDishId(1);

        User user=new User();
        user.setActive(true);
        user.setAddress("India");
        user.setPhone(9999L);
        user.setName("ABC");
        user.setCart(new ArrayList<>());
        user.getCart().add(cartItem);
        user.setHistory(new ArrayList<>());

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Order order=new Order();
        order.setStatus("active");
        order.setPrice(100.0);
        order.setTime(new Date());
        order.setItems(new ArrayList<>());

        Dish dish=new Dish();
        dish.setAvailable(10);
        dish.setCost(100.0);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.cartItemToOrderItem(Mockito.any(CartItem.class))).thenCallRealMethod();
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(orderItemRepo.save(Mockito.any(OrderItem.class))).thenReturn(orderItem);
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(transformer.orderToOrderResponse(Mockito.any(Order.class))).thenCallRealMethod();

        assertEquals(100.0,userService.checkoutScheduled(request).getPrice());

    }

    @Test
    public void testCheckoutScheduled_WrongTime() throws ParseException {

        ScheduledOrderRequest request=new ScheduledOrderRequest();
        request.setUserId(1);
        request.setScheduledTime("Wed Jun 16 10:00:00 IST 2021");

        assertThrows(WrongTimeException.class,()->userService.checkoutScheduled(request));
    }

    @Test
    public void testCheckoutScheduled_UserNotFound() throws ParseException {

        ScheduledOrderRequest request=new ScheduledOrderRequest();
        request.setUserId(1);
        request.setScheduledTime("Wed Jun 16 13:00:00 IST 2021");

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()->userService.checkoutScheduled(request));

    }

    @Test
    public void testCheckoutScheduled_DishNotFound() throws ParseException {

        ScheduledOrderRequest request=new ScheduledOrderRequest();
        request.setUserId(1);
        request.setScheduledTime("Wed Jun 16 13:00:00 IST 2021");

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        cartItem.setDishId(1);

        user.setCart(new ArrayList<>());
        user.getCart().add(cartItem);
        user.setHistory(new ArrayList<>());

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.cartItemToOrderItem(Mockito.any(CartItem.class))).thenCallRealMethod();
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class,()->userService.checkoutScheduled(request));

    }

    @Test
    public void testCheckoutScheduled_Insufficient() throws ParseException {

        ScheduledOrderRequest request=new ScheduledOrderRequest();
        request.setUserId(1);
        request.setScheduledTime("Wed Jun 16 13:00:00 IST 2021");

        CartItem cartItem=new CartItem();
        cartItem.setQuantity(1);
        cartItem.setDishId(1);

        user.setCart(new ArrayList<>());
        user.getCart().add(cartItem);
        user.setHistory(new ArrayList<>());


        Dish dish=new Dish();
        dish.setAvailable(0);
        dish.setCost(100.0);

        Mockito.when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        Mockito.when(transformer.cartItemToOrderItem(Mockito.any(CartItem.class))).thenCallRealMethod();
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));

        assertThrows(QuantityException.class,()->userService.checkoutScheduled(request));

    }

    @Test
    public void testCheckoutScheduled_ParseError() {

        ScheduledOrderRequest request=new ScheduledOrderRequest();
        request.setUserId(1);
        request.setScheduledTime("Wed Jun 16 13:00:00 IST");

        assertThrows(ParseException.class,()->userService.checkoutScheduled(request));

    }

    @Test
    public void testProcessOrders(){

        Order order=new Order();
        order.setStatus("scheduled");
        order.setPrice(100.0);
        order.setTime(new Date());
        order.setScheduledTime(new Date());
        order.setItems(new ArrayList<>());

        Iterable<Order> iterable= Arrays.asList(order);
        Mockito.when(orderRepo.findByStatus(Mockito.anyString())).thenReturn(iterable);
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);

        userService.processOrders();

    }

    @Test
    public void testCancelOrder(){

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Order order=new Order();
        order.setStatus("active");
        order.setPrice(100.0);
        order.setTime(new Date());
        order.setItems(Arrays.asList(orderItem));

        Dish dish=new Dish();
        dish.setAvailable(0);
        dish.setCost(100.0);

        OrderResponse response=new OrderResponse();
        response.setStatus("cancelled");
        response.setPrice(100.0);
        response.setTime(order.getTime());

        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(dish));
        Mockito.when(dishRepo.save(Mockito.any(Dish.class))).thenReturn(dish);
        Mockito.when(transformer.orderToOrderResponse(Mockito.any(Order.class))).thenCallRealMethod();

        assertEquals(response.getPrice(),userService.cancelOrder(1).getPrice());

        order=new Order();
        order.setStatus("scheduled");
        order.setPrice(100.0);
        order.setTime(new Date());
        long ltime=order.getTime().getTime()+24*60*60*1000;
        order.setScheduledTime(new Date(ltime));

        order.setItems(Arrays.asList(orderItem));
        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);


        assertEquals(response.getPrice(),userService.cancelOrder(1).getPrice());

    }

    @Test
    public void testCancelOrder_WrongTime(){

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Order order=new Order();
        order.setStatus("scheduled");
        order.setPrice(100.0);
        order.setTime(new Date());
        order.setScheduledTime(new Date());
        order.setItems(Arrays.asList(orderItem));

        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(order));

        assertThrows(WrongTimeException.class,()->userService.cancelOrder(1));
    }




    @Test
    public void testCancelOrder_OrderNotFound(){

        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class,()->userService.cancelOrder(1));
    }

    @Test
    public void testCancelOrder_DishNotFound(){

        OrderItem orderItem=new OrderItem();
        orderItem.setDishId(1);
        orderItem.setQuantity(1);

        Order order=new Order();
        order.setStatus("active");
        order.setPrice(100.0);
        order.setTime(new Date());
        order.setItems(Arrays.asList(orderItem));


        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(dishRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class,()->userService.cancelOrder(1));

    }

    @Test
    public void testReceived(){

        Order order=new Order();
        order.setStatus("active");
        order.setPrice(100.0);
        order.setTime(new Date());

        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(order));
        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(transformer.orderToOrderResponse(Mockito.any(Order.class))).thenCallRealMethod();

        assertEquals(100,userService.received(1).getPrice());
    }

    @Test
    public void testReceived_OrderNotFound(){

        Mockito.when(orderRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,()->userService.received(1));
    }

}
