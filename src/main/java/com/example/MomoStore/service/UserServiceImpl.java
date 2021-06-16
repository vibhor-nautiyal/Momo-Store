package com.example.MomoStore.service;

import com.example.MomoStore.MomoStoreApplication;
import com.example.MomoStore.dto.Transformer;
import com.example.MomoStore.dto.request.*;
import com.example.MomoStore.dto.response.OrderResponse;
import com.example.MomoStore.dto.response.UserResponse;
import com.example.MomoStore.entity.*;
import com.example.MomoStore.exception.*;
import com.example.MomoStore.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserRepo userRepo;
    private Transformer transformer;
    private CartItemRepo cartItemRepo;
    private DishRepo dishRepo;
    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;
    private static final Logger log= LoggerFactory.getLogger(MomoStoreApplication.class.getName());

    @Autowired
    public UserServiceImpl(UserRepo userRepo,Transformer transformer,CartItemRepo cartItemRepo,DishRepo dishRepo,OrderRepo orderRepo,OrderItemRepo orderItemRepo){
        this.userRepo=userRepo;
        this.transformer=transformer;
        this.cartItemRepo=cartItemRepo;
        this.dishRepo=dishRepo;
        this.orderRepo=orderRepo;
        this.orderItemRepo=orderItemRepo;
    }

    public UserResponse createUser(NewUserRequest request){

        User user=transformer.newUserRequestToUser(request);
        user=userRepo.save(user);
        UserResponse response=transformer.userToUserResponse(user);
        log.info("New user created");
        return response;
    }

    public UserResponse getUserById(Integer id){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User with id "+id+" not found."));
        log.info("Fetching user by id={}",id);
        return transformer.userToUserResponse(user);
    }

    public UserResponse updateUser(UpdateUserRequest request){

        User user=userRepo.findById(request.getId()).orElseThrow(()->new UserNotFoundException("User with id "+request.getId()+" not found."));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setName(request.getName());
        user=userRepo.save(user);
        log.info("Updating using with id={}",request.getId());
        return transformer.userToUserResponse(user);
    }

    public void deleteUser(Integer id){
        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User with id "+id+" not found."));
        user.setActive(false);
        userRepo.save(user);
        log.info("Deleting user with id={}",id);
    }

    public List<UserResponse> findAllUsers(){
        Iterable<User> users=userRepo.findAll();
        List<UserResponse> response=new ArrayList<>();
        for(User user:users){
            if(user.getActive())
                response.add(transformer.userToUserResponse(user));
        }
        log.info("Fetching all users");
        return response;
    }

    public UserResponse addToCart(AddToCartRequest request){

        Dish dish=dishRepo.findById(request.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+request.getDishId()+" not found."));
        if(dish.getAvailable()< request.getQuantity()){
            throw new QuantityException("Quantity Exceeded for dish id="+request.getDishId());
        }
        User user=userRepo.findById(request.getUserId()).orElseThrow(()->new UserNotFoundException("User with id "+request.getUserId()+" not found."));
        CartItem cartItem=transformer.createCartItem(request);
        cartItemRepo.save(cartItem);
        user.getCart().add(cartItem);
        userRepo.save(user);
        log.info("Item with id={} added to cart",request.getDishId());
        return transformer.userToUserResponse(user);
    }

    public UserResponse updateCartItem(AddToCartRequest request){

        Dish dish=dishRepo.findById(request.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+request.getDishId()+" not found."));
        if(dish.getAvailable()<request.getQuantity()){
            throw new QuantityException("Quantity Exceeded for dish id="+request.getDishId());
        }
        User user=userRepo.findById(request.getUserId()).orElseThrow(()-> new UserNotFoundException("User with id="+request.getUserId()+" not found."));
        CartItem cartItem=cartItemRepo.findByUserIdAndDishId(request.getUserId(), request.getDishId());
        cartItem.setQuantity(request.getQuantity());
        cartItemRepo.save(cartItem);
        log.info("Updated quantity in cart for item id={}",request.getDishId());
        return transformer.userToUserResponse(user);
    }

    public UserResponse removeFromCart(RemoveFromCartRequest request){

        cartItemRepo.deleteByUserIdAndDishId(request.getUserId(),request.getDishId());
        log.info("Item with id={} removed from cart",request.getDishId());
        return transformer.userToUserResponse(userRepo.findById(request.getUserId()).orElseThrow(()-> new UserNotFoundException("User with id="+request.getUserId()+" not found.")));
    }

    public OrderResponse checkout(Integer id){

        Date date=new Date();
        if(date.getHours()<11 || date.getHours()>=21)
            throw new WrongTimeException("Order can be placed only between 9 AM and 11 PM.");

        User user=userRepo.findById(id).orElseThrow(()->new UserNotFoundException("User with id "+id+" not found."));
        List<OrderItem> orderItems=new ArrayList<>();
        Double bill=0.0;
        for(CartItem cartItem:user.getCart()){
            OrderItem orderItem=transformer.cartItemToOrderItem(cartItem);
            orderItemRepo.save(orderItem);
            orderItems.add(orderItem);
            Dish dish=dishRepo.findById(cartItem.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+cartItem.getDishId()+" not found."));
            if(dish.getAvailable()< cartItem.getQuantity()){
                throw new QuantityException("Quantity exceeded for dish id="+dish.getId());
            }
            dish.setAvailable(dish.getAvailable()- cartItem.getQuantity());
            dishRepo.save(dish);
            bill+=dish.getCost()* cartItem.getQuantity();
        }
        Order order=new Order();
        order.setItems(orderItems);
        order.setStatus("active");
        order.setTime(new Date());
        order.setPrice(bill);

        orderRepo.save(order);

        user.getHistory().add(order);
        user.getCart().clear();
        userRepo.save(user);
        log.info("Order placed for user id={}",id);
        return transformer.orderToOrderResponse(order);
    }


    public OrderResponse checkoutScheduled(ScheduledOrderRequest request) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date scheduledDate=formatter.parse(request.getScheduledTime());
        if(scheduledDate.getHours()<11 || scheduledDate.getHours()>=21)
            throw new WrongTimeException("Order can be placed only between 9 AM and 11 PM.");

        User user=userRepo.findById(request.getUserId()).orElseThrow(()->new UserNotFoundException("User with id "+request.getUserId()+" not found."));
        List<OrderItem> orderItems=new ArrayList<>();
        Double bill=0.0;
        for(CartItem cartItem:user.getCart()){
            OrderItem orderItem=transformer.cartItemToOrderItem(cartItem);
            orderItems.add(orderItem);
            Dish dish=dishRepo.findById(cartItem.getDishId()).orElseThrow(()->new DishNotFoundException("Dish with id "+cartItem.getDishId()+" not found."));
            if(dish.getAvailable()< cartItem.getQuantity()){
                throw new QuantityException("Quantity exceeded for dish id="+dish.getId());
            }
            orderItemRepo.save(orderItem);
            dish.setAvailable(dish.getAvailable()- cartItem.getQuantity());
            dishRepo.save(dish);
            bill+=dish.getCost()* cartItem.getQuantity();
        }
        Order order=new Order();
        order.setItems(orderItems);
        order.setStatus("scheduled");
        order.setTime(new Date());
        order.setPrice(bill);
        order.setScheduledTime(scheduledDate);
        orderRepo.save(order);

        user.getHistory().add(order);
        user.getCart().clear();
        userRepo.save(user);
        log.info("Order placed for user id={}",request.getUserId());
        return transformer.orderToOrderResponse(order);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void processOrders(){

        Iterable<Order> scheduledOrders=orderRepo.findByStatus("scheduled");
        Date current=new Date();
        for(Order order:scheduledOrders){

            if(order.getScheduledTime().getDate()==current.getDate() && order.getScheduledTime().getHours()==current.getHours()){
                order.setStatus("active");
            }
            orderRepo.save(order);
        }
        log.info("Changed status for current scheduled jobs to active");
    }

    public OrderResponse cancelOrder(Integer id){

        Order order=orderRepo.findById(id).orElseThrow(()->new OrderNotFoundException("Order with id="+id+" not found."));

        if(order.getScheduledTime()==null) {
            Date currTime = new Date();
            Date orderTime = order.getTime();
            long minuteDiff = ((currTime.getTime() - orderTime.getTime()) / 60000) % 60;
            if (minuteDiff >= 1) {
                throw new WrongTimeException("Order cal only be cancelled within one minute.");
            }
            order.setStatus("Cancelled");
            order = orderRepo.save(order);
            for(OrderItem item: order.getItems()){
                Dish dish=dishRepo.findById(item.getDishId()).orElseThrow(()-> new DishNotFoundException("Dish with id "+item.getDishId()+" not found."));
                dish.setAvailable(dish.getAvailable()+item.getQuantity());
                dishRepo.save(dish);
            }
            log.info("Order with id={} cancelled",id);
            return transformer.orderToOrderResponse(order);
        }
        else{
            Date currTime=new Date();
            Date scheduledTime=order.getScheduledTime();
            if(currTime.after(scheduledTime))
                throw new WrongTimeException("Active Scheduled Order can't be cancelled");

            order.setStatus("Cancelled");
            for(OrderItem item: order.getItems()){
                Dish dish=dishRepo.findById(item.getDishId()).orElseThrow(()-> new DishNotFoundException("Dish with id "+item.getDishId()+" not found."));
                dish.setAvailable(dish.getAvailable()+item.getQuantity());
                dishRepo.save(dish);
            }
            order=orderRepo.save(order);
            log.info("Order with id={} cancelled",id);
            return transformer.orderToOrderResponse(order);
        }
    }

    public OrderResponse received(Integer orderId){
        Order order=orderRepo.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order with id="+orderId+" not found"));
        order.setStatus("completed");
        orderRepo.save(order);
        log.info("Order with id={} is complete",orderId);
        return transformer.orderToOrderResponse(order);
    }
}
