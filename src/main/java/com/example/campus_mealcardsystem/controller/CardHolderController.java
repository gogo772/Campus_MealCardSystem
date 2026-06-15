package com.example.campus_mealcardsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.common.Result;
import com.example.campus_mealcardsystem.entity.CardHolder;
import com.example.campus_mealcardsystem.service.CardHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/card-holder")
public class CardHolderController {

    @Autowired
    private CardHolderService cardHolderService;

    // ==================== 注册开卡 ====================
    @PostMapping("/register")
    public Result<CardHolder> register(@RequestBody CardHolder cardHolder) {
        try {
            CardHolder saved = cardHolderService.register(cardHolder);
            return Result.success("开卡成功，卡号：" + saved.getCardNumber(), saved);
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 注销饭卡 ====================
    @PostMapping("/cancel")
    public Result<Boolean> cancel(@RequestParam String cardNumber) {
        try {
            boolean ok = cardHolderService.cancel(cardNumber);
            return Result.success("注销成功", ok);
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (IllegalStateException e) {
            return Result.error(409, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 基础查询 ====================
    @GetMapping("/{id}")
    public Result<CardHolder> getById(@PathVariable Long id) {
        CardHolder cardHolder = cardHolderService.getById(id);
        return cardHolder != null ? Result.success(cardHolder) : Result.notFound("持卡者不存在");
    }

    @GetMapping("/by-card/{cardNumber}")
    public Result<CardHolder> getByCardNumber(@PathVariable String cardNumber) {
        CardHolder cardHolder = cardHolderService.getByCardNumber(cardNumber);
        return cardHolder != null ? Result.success(cardHolder) : Result.notFound("持卡者不存在");
    }

    @GetMapping
    public Result<List<CardHolder>> list() {
        return Result.success(cardHolderService.list());
    }

    // ==================== 充值 ====================
    @PostMapping("/recharge")
    public Result<BigDecimal> recharge(@RequestParam String cardNumber, @RequestParam BigDecimal amount) {
        try {
            return Result.success("充值成功", cardHolderService.recharge(cardNumber, amount));
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (IllegalStateException e) {
            return Result.error(409, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 消费 ====================
    @PostMapping("/consume")
    public Result<BigDecimal> consume(@RequestParam String cardNumber, @RequestParam BigDecimal amount) {
        try {
            return Result.success("消费成功", cardHolderService.consume(cardNumber, amount));
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (IllegalStateException e) {
            return Result.error(409, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 挂失 ====================
    @PostMapping("/report-loss/{cardNumber}")
    public Result<Boolean> reportLoss(@PathVariable String cardNumber) {
        try {
            return Result.success("挂失成功", cardHolderService.reportLoss(cardNumber));
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (IllegalStateException e) {
            return Result.error(409, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 解冻 ====================
    @PostMapping("/unfreeze/{cardNumber}")
    public Result<Boolean> unfreeze(@PathVariable String cardNumber) {
        try {
            return Result.success("解冻成功", cardHolderService.unfreeze(cardNumber));
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (IllegalStateException e) {
            return Result.error(409, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    // ==================== 信息查询（模糊/分页） ====================
    @GetMapping("/query/card")
    public Result<CardHolder> queryByCardNumber(@RequestParam String cardNumber) {
        try {
            CardHolder cardHolder = cardHolderService.queryByCardNumber(cardNumber);
            if (cardHolder == null) {
                return Result.notFound("饭卡不存在");
            }
            return Result.success(cardHolder);
        } catch (IllegalArgumentException e) {
            return Result.paramError(e.getMessage());
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping("/page/name")
    public Result<Page<CardHolder>> queryByNameLike(@RequestParam(required = false) String name,
                                                    @RequestParam(defaultValue = "1") int pageNum,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<CardHolder> page = cardHolderService.queryByNameLike(name, pageNum, pageSize);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @GetMapping("/page/condition")
    public Result<Page<CardHolder>> queryByCondition(@RequestParam(required = false) String cardNumber,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) Integer status,
                                                     @RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<CardHolder> page = cardHolderService.queryByCondition(cardNumber, name, status, pageNum, pageSize);
            return Result.success(page);
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}