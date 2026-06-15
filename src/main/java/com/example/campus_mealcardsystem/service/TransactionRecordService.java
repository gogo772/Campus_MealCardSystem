package com.example.campus_mealcardsystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.entity.TransactionRecord;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRecordService extends BaseService<TransactionRecord> {
    List<TransactionRecord> getByCardNumber(String cardNumber);
    List<TransactionRecord> getByTransactionType(Integer transactionType);

    Page<TransactionRecord> getByCardNumberPage(String cardNumber, int pageNum, int pageSize);
    Page<TransactionRecord> getByTimeRange(LocalDateTime startTime, LocalDateTime endTime, int pageNum, int pageSize);
    Page<TransactionRecord> queryByCondition(String cardNumber, Integer transactionType,
                                             LocalDateTime startTime, LocalDateTime endTime,
                                             int pageNum, int pageSize);
}