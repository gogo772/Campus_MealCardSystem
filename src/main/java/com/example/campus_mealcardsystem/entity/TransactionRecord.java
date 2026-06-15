package com.example.campus_mealcardsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("transaction_record")
public class TransactionRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String cardNumber;
    private Integer transactionType;
    private BigDecimal amount;
    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime transactionTime;
    private String remark;
}