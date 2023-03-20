package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface HouseMapper extends BaseMapper<House> {
    void publish(@Param("id") Long id,@Param("status") Long status);

    Page<HouseVo> findHouseVoListPage(HouseQueryBo houseQueryBo);
}
