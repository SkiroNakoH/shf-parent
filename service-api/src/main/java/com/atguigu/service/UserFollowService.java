package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

public interface UserFollowService extends BaseService<UserFollow> {
    boolean isFollow(Long userId, Long houseId);

    void follow(Long userId, Long houseId);

    void cancelFollow(Long userId, Long houseId);

    PageInfo<UserFollowVo> findUserFollowVoPage(Integer pageNum, Integer pageSize, Long userId);

    void cancelFollowById(Long id);
}
