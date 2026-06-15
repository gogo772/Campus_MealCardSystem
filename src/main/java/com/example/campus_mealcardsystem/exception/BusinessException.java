package com.example.campus_mealcardsystem.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}