package com.example.campus_mealcardsystem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus_mealcardsystem.entity.CardHolder;

public interface CardHolderMapper extends BaseMapper<CardHolder> {

    default CardHolder selectByCardNumber(String cardNumber) {
        return selectOne(new LambdaQueryWrapper<CardHolder>()
                .eq(CardHolder::getCardNumber, cardNumber));
    }
}