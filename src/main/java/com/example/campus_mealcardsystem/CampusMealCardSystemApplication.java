package com.example.campus_mealcardsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.campus_mealcardsystem.mapper")
public class CampusMealCardSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusMealCardSystemApplication.class, args);
    }
}