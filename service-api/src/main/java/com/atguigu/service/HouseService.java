package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface HouseService extends BaseService<House> {
    void publish(Long id, Long status);

    PageInfo<HouseVo> findHOuseVoListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo);
}
