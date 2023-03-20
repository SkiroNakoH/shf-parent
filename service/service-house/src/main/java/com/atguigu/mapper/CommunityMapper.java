package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Community;

public interface CommunityMapper extends BaseMapper<Community> {
    Community getByHouseId(Long houseId);
}
