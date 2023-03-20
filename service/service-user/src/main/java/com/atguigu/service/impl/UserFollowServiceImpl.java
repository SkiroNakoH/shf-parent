package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public BaseMapper<UserFollow> getBaseMapper() {
        return userFollowMapper;
    }

    @Override
    public boolean isFollow(Long userId, Long houseId) {
        UserFollow follow = userFollowMapper.isFollow(userId, houseId);
        //判断是否删除
        if (follow == null || follow.getIsDeleted() == 1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void follow(Long userId, Long houseId) {
        //判断是否关注
        UserFollow userFollow = userFollowMapper.isFollow(userId, houseId);
        if (userFollow == null) {
            //未曾关注过
            userFollow = new UserFollow();
            userFollow.setUserId(userId);
            userFollow.setHouseId(houseId);

            userFollowMapper.insert(userFollow);
        }
        if (userFollow != null) {
            //曾经关注过，把is_deleted改回0
            userFollow.setIsDeleted(0);
            userFollowMapper.update(userFollow);
        }


    }

    @Override
    public void cancelFollow(Long userId, Long houseId) {
        //判断是否关注
        UserFollow userFollow = userFollowMapper.isFollow(userId, houseId);
        if (userFollow == null) {
            //未曾关注过
            return;
        }
        if (userFollow != null) {
            //关注，把is_deleted改成1
            userFollow.setIsDeleted(1);
            userFollowMapper.update(userFollow);
        }
    }

    @Override
    public PageInfo<UserFollowVo> findUserFollowVoPage(Integer pageNum, Integer pageSize, Long userId) {
        PageHelper.startPage(pageNum, pageSize);

        List<UserFollowVo> userFollowVoList = userFollowMapper.findUserFollowVoPage(userId);

        return new PageInfo<UserFollowVo>(userFollowVoList);
    }

    @Override
    public void cancelFollowById(Long id) {
        UserFollow userFollow = userFollowMapper.getById(id);
        if(userFollow != null){
            //改关注存在，取消关注
            userFollow.setIsDeleted(1);
            userFollowMapper.update(userFollow);
        }
    }
}
