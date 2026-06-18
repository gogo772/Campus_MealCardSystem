package com.example.campus_mealcardsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;    // 登录账号
    private String password;    // BCrypt加密密码
    private String role;        // 角色：ADMIN/STUDENT/TEACHER
    private String realName;    // 用户真实姓名
    private String cardNumber;  // 绑定饭卡号
    private Integer enabled;    // 账号状态 1-启用 0-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}