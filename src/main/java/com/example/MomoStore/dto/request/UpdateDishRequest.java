package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDishRequest {
    Integer dishId;
    Integer available;
    Double cost;
}
