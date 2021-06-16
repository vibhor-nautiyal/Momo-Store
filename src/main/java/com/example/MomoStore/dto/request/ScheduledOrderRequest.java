package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduledOrderRequest {

    Integer userId;
    String scheduledTime;
}
