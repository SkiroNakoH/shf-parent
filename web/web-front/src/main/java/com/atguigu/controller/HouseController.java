package com.atguigu.controller;

import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/house")
@Controller
public class HouseController {
    @DubboReference
    private HouseService houseService;
    @DubboReference
    private CommunityService communityService;
    @DubboReference
    private HouseImageService houseImageService;
    @DubboReference
    private UserFollowService userFollowService;
    @DubboReference
    private HouseBrokerService houseBrokerService;

    /**
     * 首页 搜索加载房源数据
     * @param pageNum
     * @param pageSize
     * @param houseQueryBo 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable Integer pageNum, @PathVariable Integer pageSize, @RequestBody HouseQueryBo houseQueryBo){
        PageInfo<HouseVo> houseVoList = houseService.findHOuseVoListPage(pageNum,pageSize,houseQueryBo);
        return Result.ok(houseVoList);
    }

    /**
     * 点击进入 该房屋详情页面
     * @param houseId 房源id
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/auth/info/{id}")
    public Result info(@PathVariable("id") Long houseId, HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        if(userInfo == null)
            return Result.build(null, ResultCodeEnum.LOGIN_AUTH);

        Map map = new HashMap();

        House house = houseService.getById(houseId);
        map.put("house",house);

        Community community = communityService.getByHouseId(houseId);
        map.put("community",community);
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(houseId);
        map.put("houseBrokerList", houseBrokerList);

        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(houseId, 1);
        map.put("houseImage1List",houseImage1List);

        boolean isFollow = userFollowService.isFollow(userInfo.getId(),houseId);
        map.put("isFollow",isFollow);

        return Result.ok(map);
    }
}
