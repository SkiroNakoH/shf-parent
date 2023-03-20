package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFollowMapper extends BaseMapper<UserFollow> {
    UserFollow isFollow(@Param("userId") Long userId,@Param("houseId") Long houseId);

    List<UserFollowVo> findUserFollowVoPage(Long userId);
}
