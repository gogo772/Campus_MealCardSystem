package com.example.campus_mealcardsystem.controller;

import com.example.campus_mealcardsystem.common.Result;
import com.example.campus_mealcardsystem.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统数据备份控制器
 *
 * POST /api/backup  - 手动触发全量数据备份，返回备份文件路径
 */
@RestController
@RequestMapping("/api/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    /**
     * 手动触发全量数据备份
     * POST /api/backup
     * 无需请求体
     */
    @PostMapping
    public Result<Map<String, String>> backup() {
        try {
            String filePath = backupService.backupAll();
            Map<String, String> data = new HashMap<>();
            data.put("filePath", filePath);
            data.put("message", "备份成功");
            return Result.success("系统数据备份完成", data);
        } catch (Exception e) {
            return Result.error(500, "备份失败：" + e.getMessage());
        }
    }
}
