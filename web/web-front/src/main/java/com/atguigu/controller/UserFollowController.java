package com.atguigu.controller;

import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/userFollow")
public class UserFollowController {

    @DubboReference
    private UserFollowService userFollowService;

    /**
     * 根据房源id添加关注
     * @param houseId 房源id
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/auth/follow/{id}")
    public Result follow(@PathVariable("id") Long houseId, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");

        userFollowService.follow(userInfo.getId(),houseId);
        return Result.ok();
    }

    /**
     * 根据房源id取消关注
     * @param houseId 房源id
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long houseId, HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        userFollowService.cancelFollow(userInfo.getId(),houseId);
        return Result.ok();
    }

    /**
     * 关注列表分页
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable Integer pageNum,@PathVariable Integer pageSize,HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");

        PageInfo<UserFollowVo> page = userFollowService.findUserFollowVoPage(pageNum,pageSize,userInfo.getId());
        return Result.ok(page);
    }

    /**
     *  根据user_follow主键取消关注
     * @param id user_follow的主键
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/auth/cancelFollowById/{id}")
    public Result cancelFollowById(@PathVariable("id") Long id,HttpSession session){
        userFollowService.cancelFollowById(id);
        return Result.ok();
    }

}
