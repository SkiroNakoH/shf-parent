package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.HouseImage;
import com.atguigu.mapper.HouseImageMapper;
import com.atguigu.service.HouseImageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class HouseImageServiceImpl extends BaseServiceImpl<HouseImage> implements HouseImageService {
    @Autowired
    private HouseImageMapper houseImageMapper;

    @Override
    public BaseMapper<HouseImage> getBaseMapper() {
        return houseImageMapper;
    }

    @Override
    public List<HouseImage> findHouseImageList(Long houseId, int type) {
        return houseImageMapper.findHouseImageList(houseId, type);
    }
}
