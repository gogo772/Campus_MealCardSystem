package com.example.campus_mealcardsystem.service.impl;

import com.example.campus_mealcardsystem.entity.CardHolder;
import com.example.campus_mealcardsystem.entity.TransactionRecord;
import com.example.campus_mealcardsystem.mapper.CardHolderMapper;
import com.example.campus_mealcardsystem.mapper.TransactionRecordMapper;
import com.example.campus_mealcardsystem.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 系统数据备份服务实现
 * 生成纯 SQL 格式的备份文件，可直接导入 MySQL 恢复数据
 */
@Service
public class BackupServiceImpl implements BackupService {

    @Autowired
    private CardHolderMapper cardHolderMapper;

    @Autowired
    private TransactionRecordMapper transactionRecordMapper;

    /** 备份文件存放目录，默认放在项目根目录的 backup/ 下 */
    @Value("${app.backup.dir:backup}")
    private String backupDir;

    private static final DateTimeFormatter FILE_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter SQL_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String backupAll() throws IOException {
        // 1. 确保备份目录存在
        File dir = new File(backupDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 2. 生成带时间戳的文件名
        String timestamp = LocalDateTime.now().format(FILE_FMT);
        String fileName = "backup_" + timestamp + ".sql";
        File backupFile = new File(dir, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {
            // 文件头注释
            writer.write("-- ============================================================");
            writer.newLine();
            writer.write("-- 校园饭卡管理系统 数据备份");
            writer.newLine();
            writer.write("-- 备份时间：" + LocalDateTime.now().format(SQL_FMT));
            writer.newLine();
            writer.write("-- ============================================================");
            writer.newLine();
            writer.newLine();

            writer.write("SET NAMES utf8mb4;");
            writer.newLine();
            writer.write("SET FOREIGN_KEY_CHECKS=0;");
            writer.newLine();
            writer.newLine();

            // 3. 备份 card_holder 表
            writeCardHolderBackup(writer);

            // 4. 备份 transaction_record 表
            writeTransactionRecordBackup(writer);

            writer.write("SET FOREIGN_KEY_CHECKS=1;");
            writer.newLine();
        }

        return backupFile.getAbsolutePath();
    }

    // ---------- card_holder ----------

    private void writeCardHolderBackup(BufferedWriter writer) throws IOException {
        List<CardHolder> holders = cardHolderMapper.selectList(null);

        writer.write("-- ---------- 持卡者信息表 card_holder（共 " + holders.size() + " 条）----------");
        writer.newLine();
        writer.write("DELETE FROM `card_holder`;");
        writer.newLine();

        if (!holders.isEmpty()) {
            writer.write("INSERT INTO `card_holder` "
                    + "(`id`,`card_number`,`name`,`type`,`balance`,`status`,`create_time`,`update_time`) VALUES");
            writer.newLine();
            for (int i = 0; i < holders.size(); i++) {
                CardHolder h = holders.get(i);
                String line = String.format("(%d,'%s','%s',%d,%s,%d,'%s','%s')",
                        h.getId(),
                        escape(h.getCardNumber()),
                        escape(h.getName()),
                        h.getType(),
                        h.getBalance() != null ? h.getBalance().toPlainString() : "0.00",
                        h.getStatus(),
                        formatDateTime(h.getCreateTime()),
                        formatDateTime(h.getUpdateTime())
                );
                writer.write(i < holders.size() - 1 ? line + "," : line + ";");
                writer.newLine();
            }
        }
        writer.newLine();
    }

    // ---------- transaction_record ----------

    private void writeTransactionRecordBackup(BufferedWriter writer) throws IOException {
        List<TransactionRecord> records = transactionRecordMapper.selectList(null);

        writer.write("-- ---------- 交易记录表 transaction_record（共 " + records.size() + " 条）----------");
        writer.newLine();
        writer.write("DELETE FROM `transaction_record`;");
        writer.newLine();

        if (!records.isEmpty()) {
            writer.write("INSERT INTO `transaction_record` "
                    + "(`id`,`card_number`,`transaction_type`,`amount`,`before_balance`,`after_balance`,"
                    + "`transaction_time`,`remark`) VALUES");
            writer.newLine();
            for (int i = 0; i < records.size(); i++) {
                TransactionRecord r = records.get(i);
                String line = String.format("(%d,'%s',%d,%s,%s,%s,'%s','%s')",
                        r.getId(),
                        escape(r.getCardNumber()),
                        r.getTransactionType(),
                        decimal(r.getAmount()),
                        decimal(r.getBeforeBalance()),
                        decimal(r.getAfterBalance()),
                        formatDateTime(r.getTransactionTime()),
                        escape(r.getRemark())
                );
                writer.write(i < records.size() - 1 ? line + "," : line + ";");
                writer.newLine();
            }
        }
        writer.newLine();
    }

    // ---------- 工具方法 ----------

    /** SQL 单引号转义 */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("'", "\\'");
    }

    private String decimal(BigDecimal d) {
        return d != null ? d.toPlainString() : "0.00";
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(SQL_FMT) : "1970-01-01 00:00:00";
    }
}
