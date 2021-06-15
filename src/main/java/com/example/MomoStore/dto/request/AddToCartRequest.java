package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {

    Integer userId;
    Integer dishId;
    Integer quantity;
}
