package com.atguigu.interceptor;


import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("USER") == null) {
            //用户未登录
            WebUtil.writeJSON(response, Result.build("未登录", ResultCodeEnum.LOGIN_AUTH));
            return false;
        }
        //用户已登录，放行
        return true;
    }
}
