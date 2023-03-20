package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.service.CommunityService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Autowired
    private CommunityMapper communityMapper;

    @Override
    public BaseMapper<Community> getBaseMapper() {
        return communityMapper;
    }

    @Override
    public Community getByHouseId(Long houseId) {
        return communityMapper.getByHouseId(houseId);
    }
}
