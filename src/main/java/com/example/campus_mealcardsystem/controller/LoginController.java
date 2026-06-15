package com.example.campus_mealcardsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // 访问登录页 /login
    @GetMapping("/login")
    public String toLogin() {
        // 对应 templates/login.html
        return "login";
    }

    // 访问首页 /index
    @GetMapping("/index")
    public String toIndex() {
        // 对应 templates/index.html
        return "index";
    }
}