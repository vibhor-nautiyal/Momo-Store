package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserRequest {

    String name;
    String address;
    Long phone;
}
