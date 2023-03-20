package com.atguigu.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
    void insert(T t);

    void update(T t);

    void delete(Long id);

    T getById(Long id);

    List<T> findAll();

    List<T> findPage(Map<String,Object> filters);
}
