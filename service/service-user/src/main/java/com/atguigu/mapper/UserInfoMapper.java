package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserInfo;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    UserInfo getByPhone(String phone);

    UserInfo getByNickName(String nickName);
}
