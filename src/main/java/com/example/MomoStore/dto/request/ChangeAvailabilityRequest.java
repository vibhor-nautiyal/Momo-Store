package com.example.MomoStore.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeAvailabilityRequest {
    Integer dishId;
    Integer available;
}
