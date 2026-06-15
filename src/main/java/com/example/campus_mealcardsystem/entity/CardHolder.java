package com.example.campus_mealcardsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("card_holder")
public class CardHolder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String cardNumber;
    private String name;
    private Integer type;
    private BigDecimal balance;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}