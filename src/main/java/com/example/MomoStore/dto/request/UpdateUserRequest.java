package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    Integer id;
    String name;
    Long phone;
    String address;
}
