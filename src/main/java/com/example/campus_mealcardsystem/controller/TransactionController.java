package com.example.campus_mealcardsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.common.Result;
import com.example.campus_mealcardsystem.entity.TransactionRecord;
import com.example.campus_mealcardsystem.service.TransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionRecordService transactionRecordService;

    @PostMapping
    public Result<TransactionRecord> create(@RequestBody TransactionRecord transactionRecord) {
        try {
            TransactionRecord saved = transactionRecordService.saveEntity(transactionRecord);
            return Result.success("创建成功", saved);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<TransactionRecord> getById(@PathVariable Long id) {
        try {
            TransactionRecord record = transactionRecordService.getById(id);
            if (record == null) {
                return Result.notFound("交易记录不存在");
            }
            return Result.success(record);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping("/card-number/{cardNumber}")
    public Result<List<TransactionRecord>> getByCardNumber(@PathVariable String cardNumber) {
        try {
            List<TransactionRecord> list = transactionRecordService.getByCardNumber(cardNumber);
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping
    public Result<List<TransactionRecord>> list() {
        try {
            List<TransactionRecord> list = transactionRecordService.list();
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        try {
            transactionRecordService.removeById(id);
            return Result.success("删除成功", true);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 分页查询 ====================
    @GetMapping("/page/by-card")
    public Result<Page<TransactionRecord>> getByCardNumberPage(@RequestParam(required = false) String cardNumber,
                                                               @RequestParam(defaultValue = "1") int pageNum,
                                                               @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<TransactionRecord> page = transactionRecordService.getByCardNumberPage(cardNumber, pageNum, pageSize);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping("/page/time-range")
    public Result<Page<TransactionRecord>> getByTimeRange(@RequestParam(required = false) String startTime,
                                                          @RequestParam(required = false) String endTime,
                                                          @RequestParam(defaultValue = "1") int pageNum,
                                                          @RequestParam(defaultValue = "10") int pageSize) {
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            Page<TransactionRecord> page = transactionRecordService.getByTimeRange(start, end, pageNum, pageSize);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping("/page/condition")
    public Result<Page<TransactionRecord>> queryByCondition(@RequestParam(required = false) String cardNumber,
                                                            @RequestParam(required = false) Integer transactionType,
                                                            @RequestParam(required = false) String startTime,
                                                            @RequestParam(required = false) String endTime,
                                                            @RequestParam(defaultValue = "1") int pageNum,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            LocalDateTime start = startTime != null ? LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            LocalDateTime end = endTime != null ? LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            Page<TransactionRecord> page = transactionRecordService.queryByCondition(
                    cardNumber, transactionType, start, end, pageNum, pageSize);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}