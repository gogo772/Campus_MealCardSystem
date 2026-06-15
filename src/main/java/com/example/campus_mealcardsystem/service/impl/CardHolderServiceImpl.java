package com.example.campus_mealcardsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.campus_mealcardsystem.entity.CardHolder;
import com.example.campus_mealcardsystem.entity.TransactionRecord;
import com.example.campus_mealcardsystem.mapper.CardHolderMapper;
import com.example.campus_mealcardsystem.mapper.TransactionRecordMapper;
import com.example.campus_mealcardsystem.service.CardHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CardHolderServiceImpl extends BaseServiceImpl<CardHolder, CardHolderMapper>
        implements CardHolderService {

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    // ==================== 查询 ====================

    @Override
    public CardHolder getByCardNumber(String cardNumber) {
        LambdaQueryWrapper<CardHolder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CardHolder::getCardNumber, cardNumber);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<CardHolder> getByStatus(Integer status) {
        LambdaQueryWrapper<CardHolder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CardHolder::getStatus, status);
        return baseMapper.selectList(wrapper);
    }

    // ==================== 注册开卡 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CardHolder register(CardHolder cardHolder) {
        if (!StringUtils.hasText(cardHolder.getName())) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (cardHolder.getType() == null || (cardHolder.getType() != 1 && cardHolder.getType() != 2)) {
            throw new IllegalArgumentException("类型不合法，1-学生，2-教职工");
        }

        if (!StringUtils.hasText(cardHolder.getCardNumber())) {
            cardHolder.setCardNumber(generateCardNumber());
        } else {
            if (getByCardNumber(cardHolder.getCardNumber()) != null) {
                throw new IllegalArgumentException("卡号 " + cardHolder.getCardNumber() + " 已存在");
            }
        }

        if (cardHolder.getBalance() == null) {
            cardHolder.setBalance(BigDecimal.ZERO);
        }
        cardHolder.setStatus(1);
        super.save(cardHolder);
        return cardHolder;
    }

    private String generateCardNumber() {
        String prefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<CardHolder> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(CardHolder::getCardNumber, prefix)
                .orderByDesc(CardHolder::getCardNumber)
                .last("LIMIT 1");
        CardHolder last = baseMapper.selectOne(wrapper);
        int seq = 1;
        if (last != null && last.getCardNumber().length() >= 14) {
            try {
                seq = Integer.parseInt(last.getCardNumber().substring(8)) + 1;
            } catch (NumberFormatException ignored) {}
        }
        return prefix + String.format("%06d", seq);
    }

    // ==================== 注销饭卡 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(String cardNumber) {
        CardHolder cardHolder = getByCardNumber(cardNumber);
        if (cardHolder == null) {
            throw new IllegalArgumentException("饭卡不存在：" + cardNumber);
        }
        if (cardHolder.getStatus() == 3) {
            throw new IllegalStateException("该饭卡已注销");
        }
        if (cardHolder.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("饭卡余额不为零，请先退款后注销");
        }
        cardHolder.setStatus(3);
        baseMapper.updateById(cardHolder);
        return true;
    }

    // ==================== 充值 / 消费 / 挂失 / 解冻 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal recharge(String cardNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("充值金额必须大于0");
        }
        CardHolder cardHolder = getByCardNumber(cardNumber);
        if (cardHolder == null) throw new IllegalArgumentException("饭卡不存在");
        if (cardHolder.getStatus() != 1) throw new IllegalStateException("饭卡状态异常，无法充值");

        BigDecimal before = cardHolder.getBalance();
        cardHolder.setBalance(before.add(amount));
        baseMapper.updateById(cardHolder);
        saveTransaction(cardNumber, 1, amount, before, cardHolder.getBalance(), "充值");
        return cardHolder.getBalance();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal consume(String cardNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("消费金额必须大于0");
        }
        CardHolder cardHolder = getByCardNumber(cardNumber);
        if (cardHolder == null) throw new IllegalArgumentException("饭卡不存在");
        if (cardHolder.getStatus() != 1) throw new IllegalStateException("饭卡状态异常，无法消费");
        if (cardHolder.getBalance().compareTo(amount) < 0) throw new IllegalStateException("余额不足");

        BigDecimal before = cardHolder.getBalance();
        cardHolder.setBalance(before.subtract(amount));
        baseMapper.updateById(cardHolder);
        saveTransaction(cardNumber, 2, amount, before, cardHolder.getBalance(), "消费");
        return cardHolder.getBalance();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reportLoss(String cardNumber) {
        CardHolder cardHolder = getByCardNumber(cardNumber);
        if (cardHolder == null) throw new IllegalArgumentException("饭卡不存在");
        if (cardHolder.getStatus() == 2) throw new IllegalStateException("饭卡已处于挂失状态");
        if (cardHolder.getStatus() == 3) throw new IllegalStateException("饭卡已注销，无法挂失");
        cardHolder.setStatus(2);
        baseMapper.updateById(cardHolder);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfreeze(String cardNumber) {
        CardHolder cardHolder = getByCardNumber(cardNumber);
        if (cardHolder == null) throw new IllegalArgumentException("饭卡不存在");
        if (cardHolder.getStatus() != 2) throw new IllegalStateException("饭卡未挂失，无需解冻");
        cardHolder.setStatus(1);
        baseMapper.updateById(cardHolder);
        return true;
    }

    private void saveTransaction(String cardNumber, int type, BigDecimal amount,
                                 BigDecimal before, BigDecimal after, String remark) {
        TransactionRecord record = new TransactionRecord();
        record.setCardNumber(cardNumber);
        record.setTransactionType(type);
        record.setAmount(amount);
        record.setBeforeBalance(before);
        record.setAfterBalance(after);
        record.setRemark(remark);
        transactionRecordMapper.insert(record);
    }

    // ==================== 信息查询功能 ====================

    @Override
    public CardHolder queryByCardNumber(String cardNumber) {
        if (!StringUtils.hasText(cardNumber)) {
            throw new IllegalArgumentException("卡号不能为空");
        }
        return getByCardNumber(cardNumber);
    }

    @Override
    public Page<CardHolder> queryByNameLike(String name, int pageNum, int pageSize) {
        Page<CardHolder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CardHolder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(CardHolder::getName, name);
        }
        wrapper.orderByDesc(CardHolder::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Page<CardHolder> queryByCondition(String cardNumber, String name, Integer status,
                                             int pageNum, int pageSize) {
        Page<CardHolder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CardHolder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(cardNumber)) {
            wrapper.eq(CardHolder::getCardNumber, cardNumber);
        }
        if (StringUtils.hasText(name)) {
            wrapper.like(CardHolder::getName, name);
        }
        if (status != null) {
            wrapper.eq(CardHolder::getStatus, status);
        }
        wrapper.orderByDesc(CardHolder::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }
}