package com.example.campus_mealcardsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.entity.TransactionRecord;
import com.example.campus_mealcardsystem.mapper.TransactionRecordMapper;
import com.example.campus_mealcardsystem.service.TransactionRecordService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionRecordServiceImpl extends BaseServiceImpl<TransactionRecord, TransactionRecordMapper>
        implements TransactionRecordService {

    @Override
    public List<TransactionRecord> getByCardNumber(String cardNumber) {
        LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransactionRecord::getCardNumber, cardNumber);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<TransactionRecord> getByTransactionType(Integer transactionType) {
        LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TransactionRecord::getTransactionType, transactionType);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<TransactionRecord> getByCardNumberPage(String cardNumber, int pageNum, int pageSize) {
        Page<TransactionRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
        if (cardNumber != null && !cardNumber.isEmpty()) {
            wrapper.eq(TransactionRecord::getCardNumber, cardNumber);
        }
        wrapper.orderByDesc(TransactionRecord::getTransactionTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<TransactionRecord> getByTimeRange(LocalDateTime startTime, LocalDateTime endTime,
                                                  int pageNum, int pageSize) {
        Page<TransactionRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            wrapper.ge(TransactionRecord::getTransactionTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(TransactionRecord::getTransactionTime, endTime);
        }
        wrapper.orderByDesc(TransactionRecord::getTransactionTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<TransactionRecord> queryByCondition(String cardNumber, Integer transactionType,
                                                    LocalDateTime startTime, LocalDateTime endTime,
                                                    int pageNum, int pageSize) {
        Page<TransactionRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TransactionRecord> wrapper = new LambdaQueryWrapper<>();
        if (cardNumber != null && !cardNumber.isEmpty()) {
            wrapper.eq(TransactionRecord::getCardNumber, cardNumber);
        }
        if (transactionType != null) {
            wrapper.eq(TransactionRecord::getTransactionType, transactionType);
        }
        if (startTime != null) {
            wrapper.ge(TransactionRecord::getTransactionTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(TransactionRecord::getTransactionTime, endTime);
        }
        wrapper.orderByDesc(TransactionRecord::getTransactionTime);
        return baseMapper.selectPage(page, wrapper);
    }
}