package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduledOrderRequest {

    Integer userId;
    String scheduledTime;
}
