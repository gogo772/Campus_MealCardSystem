package com.example.campus_mealcardsystem.service;

import java.util.List;

public interface BaseService<T> {
    T saveEntity(T entity);
    T updateEntityById(T entity);
    T getById(Long id);
    List<T> list();
    void removeById(Long id);
    boolean existsById(Long id);
    long count();
}