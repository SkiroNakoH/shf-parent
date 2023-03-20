package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface BaseService<T> {
    List<T> findAll();

    void insert(T t);

    T getById(Long id);

    void update(T t);

    void delete(Long id);

    PageInfo<T> findPage(Map filters);
}
