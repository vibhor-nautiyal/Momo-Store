package com.example.MomoStore.exception;


import com.example.MomoStore.MomoStoreApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log= LoggerFactory.getLogger(MomoStoreApplication.class.getName());

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<String> dishNotFound(DishNotFoundException e){
        log.warn("Dish not found");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(UserNotFoundException e){
        log.warn("User not found");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> orderNotFound(OrderNotFoundException e){
        log.warn("Order not found");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuantityException.class)
    public ResponseEntity<String> quantityException(QuantityException e){
        log.warn("Specified quantity exceeded available quantity");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WrongTimeException.class)
    public ResponseEntity<String> wrongTime(WrongTimeException e){
        log.warn("Bad time error");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> duplicateEntry(DataIntegrityViolationException e){
        log.warn("Duplicate value found");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<String> parseException(ParseException e){
        log.warn("Error in parsing date");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> illegalArgs(MethodArgumentNotValidException e){
        log.warn("Invalid Argument Found");
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
