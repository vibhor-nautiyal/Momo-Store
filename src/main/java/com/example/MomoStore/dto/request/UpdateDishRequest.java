package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateDishRequest {
    @NotNull
    Integer dishId;
    @NotNull
    Integer available;
    @NotNull
    Double cost;
}
