package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Community;

public interface CommunityService extends BaseService<Community> {
    Community getByHouseId(Long houseId);
}
