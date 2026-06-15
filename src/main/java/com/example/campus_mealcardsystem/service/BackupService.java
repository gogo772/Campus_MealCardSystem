package com.example.campus_mealcardsystem.service;

import java.io.IOException;

/**
 * 系统数据备份服务接口
 */
public interface BackupService {

    /**
     * 手动触发全量数据备份
     * 将 card_holder 和 transaction_record 两张表的全量数据
     * 以 INSERT SQL 的形式写入备份文件（带时间戳命名）。
     *
     * @return 备份文件的绝对路径
     * @throws IOException 文件写入异常
     */
    String backupAll() throws IOException;
}
