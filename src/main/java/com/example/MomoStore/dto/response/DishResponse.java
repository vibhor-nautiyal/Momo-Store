package com.example.MomoStore.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponse {
    Integer dishId;
    String dishName;
    Double dishCost;
    Integer available;
}
