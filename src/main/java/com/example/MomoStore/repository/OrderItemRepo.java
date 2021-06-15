package com.example.MomoStore.repository;

import com.example.MomoStore.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends CrudRepository<OrderItem,Integer> {
}
