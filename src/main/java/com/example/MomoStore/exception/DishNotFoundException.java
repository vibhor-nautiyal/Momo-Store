package com.example.MomoStore.exception;

public class DishNotFoundException extends RuntimeException{
    public DishNotFoundException(String msg){
        super(msg);
    }
}
