package com.example.MomoStore.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderResponse {

//    Integer orderId;
    Double price;
    Date time;
    Date scheduledTime;
    String status;
}
