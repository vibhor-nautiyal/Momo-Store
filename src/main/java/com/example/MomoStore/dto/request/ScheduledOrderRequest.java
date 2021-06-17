package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ScheduledOrderRequest {

    @NotNull
    Integer userId;
    @NotNull
    String scheduledTime;
}
