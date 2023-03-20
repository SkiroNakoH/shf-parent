package com.atguigu.ascept;

import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.util.IpUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志拦截器，打印controller层的入参和出参
 */
@Aspect
@Component
public class ControllerAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    @DubboReference
    private AdminService adminService;

    @Pointcut("execution(* com.atguigu.controller..*.*(..))")
    public void controllerAspect() {
    }

    @Around(value = "controllerAspect()")
    public Object aroundAspect(ProceedingJoinPoint pjp) throws Throwable {
        //1.判断用户是否登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null) {
            //登录失败
            return pjp.proceed();
        }

        String userName = authentication.getName();
        if (userName == null || "anonymousUser".equals(userName)) {
            //没有登录，不做日志记录
            return pjp.proceed();
        }

        //2.登录，打印日志
        /**
         *    "用户编号 : " + userId + " -- 用户名 : " + userName +
         *          " -- 请求路径 : " + url + " -- 请求方式 : " + method + " -- ip地址 : " + ip +
         *           " -- 类名 : " + className + " -- 方法名 : " + methodName +
         *          " -- 参数 : " + (params.toString().equals("") ? "无参数" : params) +
         *          " -- 运行时长 : " + time + "毫秒"
         */


        //① 查询用户,获取用户编号（userId）和用户名（userName）
        Admin admin = adminService.getByName(userName);
        String userId = String.valueOf(admin.getId());

        //②获取请求对象,通过请求获取到 请求路径（url）、请求方式（method）和ip地址（ip）
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        StringBuffer url = request.getRequestURL();
        String method = request.getMethod();

        String ip = IpUtil.getIpAddress(request);

        //③通过切入点获取签名（signature）,从而获取到类名（className）、方法名（methodName）
        Signature signature = pjp.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();

        //④通过切入点获取参数
        Object[] args = pjp.getArgs();
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i == args.length - 1) {
                params.append("param" + (i + 1) + ":" + args[i]);
            } else {
                params.append("param" + (i + 1) + ":" + args[i] + ",");
            }
        }

        //记录方法开始时间
        long startTime = System.currentTimeMillis();

        try {

            return pjp.proceed();
        } finally {
            //记录方法结束时间
            long endTime = System.currentTimeMillis();
            //计算运行时长
            long time = endTime - startTime;

            log.info("用户编号 : " + userId + " -- 用户名 : " + userName +
                    " -- 请求路径 : " + url + " -- 请求方式 : " + method + " -- ip地址 : " + ip +
                    " -- 类名 : " + className + " -- 方法名 : " + methodName +
                    " -- 参数 : " + (params.toString().equals("") ? "无参数" : params) +
                    " -- 运行时长 : " + time + "毫秒");
        }
    }

}
