package com.example.MomoStore.exception;

public class WrongTimeException extends RuntimeException{
    public WrongTimeException(String msg){
        super(msg);
    }
}
