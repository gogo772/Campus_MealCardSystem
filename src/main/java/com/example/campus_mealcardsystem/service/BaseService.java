package com.example.campus_mealcardsystem.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import java.util.List;

public interface BaseService<T> {
    T saveEntity(T entity);
    T updateEntityById(T entity);
    T getById(Long id);
    T getOne(Wrapper<T> queryWrapper);
    List<T> list();
    void removeById(Long id);
    boolean existsById(Long id);
    long count();
}