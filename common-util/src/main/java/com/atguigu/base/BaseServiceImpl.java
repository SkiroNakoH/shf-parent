package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T> {

    public abstract BaseMapper<T> getBaseMapper();

    /**
     * 查询所有
     * @return
     */

    public List<T> findAll() {
        return getBaseMapper().findAll();
    }


    /**
     *新增用户
     * @param t
     */
    
    public void insert(T t) {
        getBaseMapper().insert(t);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    
    public T getById(Long id) {
        return getBaseMapper().getById(id);
    }

    /**
     * 修改用户
     * @param t
     */
    
    public void update(T t) {
        getBaseMapper().update(t);
    }

    
    public void delete(Long id) {
        getBaseMapper().delete(id);
    }

    
    public PageInfo<T> findPage(Map filters) {

        int pageSize = CastUtil.castInt(filters.get("pageSize"), 4);
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        PageHelper.startPage(pageNum,pageSize);

        List<T> TList = getBaseMapper().findPage(filters);

        return new PageInfo<T>(TList);
    }
}
