package com.example.campus_mealcardsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.campus_mealcardsystem.service.BaseService;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T, M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>>
        extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T saveEntity(T entity) {
        super.save(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T updateEntityById(T entity) {
        super.updateById(entity);
        return entity;
    }

    @Override
    public T getById(Long id) {
        return super.getById(id);
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    public List<T> list() {
        return super.list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        super.removeById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return super.getById(id) != null;
    }

    @Override
    public long count() {
        return super.count();
    }
}