package com.example.MomoStore.repository;

import com.example.MomoStore.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends CrudRepository<Order,Integer> {
    Iterable<Order> findByStatus(String scheduled);
}
