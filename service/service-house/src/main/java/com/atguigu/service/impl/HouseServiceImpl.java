package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.mapper.HouseMapper;

import com.atguigu.service.HouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@DubboService
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public BaseMapper<House> getBaseMapper() {
        return houseMapper;
    }

    @Override
    public void publish(Long id, Long status) {
        houseMapper.publish(id,status);
    }

    @Override
    public PageInfo<HouseVo> findHOuseVoListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo) {
        PageHelper.startPage(pageNum,pageSize);
       Page<HouseVo> page = houseMapper.findHouseVoListPage(houseQueryBo);

        return new PageInfo<HouseVo>(page);
    }
}
