package com.example.campus_mealcardsystem.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // 页面跳转必须用@Controller，不能用@RestController
public class PageController {

    // 首页（日志已自动识别，可保留）
    @GetMapping("/")
    public String index(){
        return "index";
    }

    // 充值消费页面 对应 recharge-consume.html
    @GetMapping("/recharge-consume")
    public String toRechargeConsume(){
        // return 值 = templates下html文件名，不需要后缀
        return "recharge-consume";
    }

    // 补充你所有页面的跳转映射
    @GetMapping("/page/login")  // 改成新路径
    public String login(){
        return "login";
    }

    @GetMapping("/loss-management")
    public String loss(){
        return "loss-management";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/transactions")
    public String transactions(){
        return "transactions";
    }

    @GetMapping("/settings")
    public String settings(){
        return "settings";
    }
}