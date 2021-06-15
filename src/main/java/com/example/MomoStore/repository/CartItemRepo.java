package com.example.MomoStore.repository;

import com.example.MomoStore.entity.CartItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartItemRepo extends CrudRepository<CartItem,Integer> {

    @Modifying
    @Transactional
    @Query(value = "delete from cart_item where user_id=?1 and dish_id=?2",nativeQuery = true)
    void deleteByUserIdAndDishId(Integer userId,Integer dishId);

    @Query(value = "select * from cart_item where user_id=?1 and dish_id=?2",nativeQuery = true)
    CartItem findByUserIdAndDishId(Integer userId, Integer dishId);
}
