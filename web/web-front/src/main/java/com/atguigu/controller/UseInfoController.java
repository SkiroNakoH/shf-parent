package com.atguigu.controller;

import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.util.NumUtil;
import com.atguigu.util.SendMessageUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.REUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/userInfo")
public class UseInfoController {

    @DubboReference
    private UserInfoService userInfoService;

    /**
     * 注册用户
     * @param registerBo 用户注册信息,包含:验证码CODE\手机号phone\昵称NickName\密码password
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/register")
    public Result register(@RequestBody RegisterBo registerBo, HttpSession session) {
        //判断短信验证码是否正确,忽略大小写
        if (session.getAttribute("CODE") == null){
            return Result.build(null, ResultCodeEnum.CODE_OVERDUE);
        }
        if( ! ((String) session.getAttribute("CODE")).equalsIgnoreCase(registerBo.getCode())) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        //判断手机是否注册过
        if (userInfoService.getByPhone(registerBo.getPhone()) != null) {
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        //判断昵称是否被占用
        if (userInfoService.getByNickName(registerBo.getNickName()) != null) {
            return Result.build(null,ResultCodeEnum.NICKNAME_ERROR);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setPhone(registerBo.getPhone());

        //密码加密
        userInfo.setPassword(MD5.encrypt(registerBo.getPassword()));

        userInfo.setNickName(registerBo.getNickName());
        userInfoService.insert(userInfo);
        return Result.ok();
    }

    /**
     * 发送短信验证码
     * @param phone
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpSession session) {
        String CODE = "1111";
        //获取随机验证码
        //cyclic:  获取验证码的位数
//        String CODE = NumUtil.getFourNum(6);

        //TODO: 第三方给phone发短信 目前缺TemplateId
     /*   Map<String, String> map = new HashMap<>();
        map.put("code",CODE);
        SendMessageUtil.sendMessageCheck(phone,map);*/

        session.setAttribute("CODE", CODE);
        return Result.ok();
    }

    /**
     * 用户登录
     * @param loginBo 用户登录信息，包含手机号和密码
     * @param session
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Result login(@RequestBody LoginBo loginBo,HttpSession session){

        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());

        //判断账户是否存在
        if(userInfo == null)
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        //判断账户是否锁定
        if(userInfo.getStatus() == 0)
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);

        //判断密码是否正确
        if (!userInfo.getPassword().equals(MD5.encrypt(loginBo.getPassword())))
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);

        session.setAttribute("USER",userInfo);

        Map map = new HashMap<>();
        map.put("nickName",userInfo.getNickName());

        return Result.ok(map);
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping("/logout")
    public Result logout(HttpSession session){
        session.removeAttribute("USER");
        return Result.ok();
    }

}
