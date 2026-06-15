package com.example.campus_mealcardsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.entity.CardHolder;
import java.math.BigDecimal;
import java.util.List;

public interface CardHolderService extends BaseService<CardHolder> {
    CardHolder getByCardNumber(String cardNumber);
    List<CardHolder> getByStatus(Integer status);
    CardHolder register(CardHolder cardHolder);
    boolean cancel(String cardNumber);
    BigDecimal recharge(String cardNumber, BigDecimal amount);
    BigDecimal consume(String cardNumber, BigDecimal amount);
    boolean reportLoss(String cardNumber);
    boolean unfreeze(String cardNumber);

    // 查询功能
    CardHolder queryByCardNumber(String cardNumber);
    Page<CardHolder> queryByNameLike(String name, int pageNum, int pageSize);
    Page<CardHolder> queryByCondition(String cardNumber, String name, Integer status, int pageNum, int pageSize);
}