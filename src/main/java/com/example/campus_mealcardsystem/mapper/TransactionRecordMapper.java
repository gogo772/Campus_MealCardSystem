package com.example.campus_mealcardsystem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.campus_mealcardsystem.entity.TransactionRecord;

import java.util.List;

public interface TransactionRecordMapper extends BaseMapper<TransactionRecord> {

    default List<TransactionRecord> selectByCardNumber(String cardNumber) {
        return selectList(new LambdaQueryWrapper<TransactionRecord>()
                .eq(TransactionRecord::getCardNumber, cardNumber)
                .orderByDesc(TransactionRecord::getTransactionTime));
    }
}