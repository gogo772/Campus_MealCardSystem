package com.example.campus_mealcardsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 访问登录页 /login（Spring Security 表单登录的入口）
    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }
}