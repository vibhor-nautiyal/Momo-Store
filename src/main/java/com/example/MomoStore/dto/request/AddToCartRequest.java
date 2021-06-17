package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddToCartRequest {


    @NotNull
    Integer userId;
    @NotNull
    Integer dishId;
    @NotNull
    Integer quantity;
}
