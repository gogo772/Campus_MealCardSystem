package com.example.campus_mealcardsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.common.Result;
import com.example.campus_mealcardsystem.entity.CardHolder;
import com.example.campus_mealcardsystem.entity.TransactionRecord;
import com.example.campus_mealcardsystem.security.SysUserDetails;
import com.example.campus_mealcardsystem.service.CardHolderService;
import com.example.campus_mealcardsystem.service.TransactionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionRecordService transactionRecordService;

    @Autowired
    private CardHolderService cardHolderService;

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

    // 全量交易记录查询（ADMIN返回全部，普通用户仅返回自己的）
    @GetMapping
    public Result<List<TransactionRecord>> list(@AuthenticationPrincipal SysUserDetails userDetails) {
        try {
            List<TransactionRecord> list;
            if ("ADMIN".equals(userDetails.getRole())) {
                // 管理员：查看所有交易记录
                list = transactionRecordService.list();
            } else {
                // 普通用户（STUDENT/TEACHER）：仅查看自己的交易记录
                String cardNumber = resolveCardNumber(userDetails);
                if (cardNumber == null || cardNumber.isEmpty()) {
                    return Result.success(java.util.Collections.emptyList());
                }
                list = transactionRecordService.getByCardNumber(cardNumber);
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    /**
     * 解析当前用户绑定的饭卡号。
     * 优先使用 sys_user.card_number 字段；若为空则通过 real_name 匹配 card_holder.name 反查。
     */
    private String resolveCardNumber(SysUserDetails userDetails) {
        String cardNumber = userDetails.getCardNumber();
        if (cardNumber != null && !cardNumber.isEmpty()) {
            return cardNumber;
        }
        // Fallback: 通过真实姓名匹配 card_holder
        String realName = userDetails.getRealName();
        if (realName != null && !realName.isEmpty()) {
            LambdaQueryWrapper<CardHolder> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CardHolder::getName, realName).last("LIMIT 1");
            CardHolder card = cardHolderService.getOne(wrapper);
            if (card != null) {
                return card.getCardNumber();
            }
        }
        return null;
    }

    // 删除交易记录（仅ADMIN可操作）
    @PreAuthorize("hasRole('ADMIN')")
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