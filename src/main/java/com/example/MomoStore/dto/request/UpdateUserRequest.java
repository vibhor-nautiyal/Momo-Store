package com.example.MomoStore.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateUserRequest {

    @NotNull
    Integer id;
    @NotNull
    String name;
    @NotNull
    Long phone;
    @NotNull
    String address;
}
