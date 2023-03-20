package com.atguigu.aspect;

import com.atguigu.entity.UserInfo;
import com.atguigu.util.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.github.pagehelper.util.MetaObjectUtil.method;

@Aspect
@Component
public class ControllerAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerAspect.class);

    @Pointcut("execution(* *..*Controller.*(..))")
    public void p1() {
    }

    @Around("p1()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Long startTime = System.currentTimeMillis();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();

        if (session.getAttribute("USER") == null)
            return pjp.proceed();

        UserInfo user = (UserInfo) session.getAttribute("USER");

        Long userId = user.getId();
        String userName = user.getNickName();

        String url = request.getRequestURI();
        String method = request.getMethod();

        String ip = IpUtil.getIpAddress(request);

        String className = pjp.getSignature().getDeclaringType().getName();
        String methodName = pjp.getSignature().getName();

        StringBuilder params = new StringBuilder();
        Object[] args = pjp.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (i == args.length - 1) {
                params.append("param" + (i + 1) + ":" + args[i]);
            } else {
                params.append("param" + (i + 1) + ":" + args[i] + ",");
            }
        }

        Long endTime = System.currentTimeMillis();

        Long time = endTime - startTime;
        String str = "用户编号 : " + userId + " -- 用户名 : " + userName +
                " -- 请求路径 : " + url + " -- 请求方式 : " + method + " -- ip地址 : " + ip +
                " -- 类名 : " + className + " -- 方法名 : " + methodName +
                " -- 参数 : " + (params.toString().equals("") ? "无参数" : params) +
                " -- 运行时长 : " + time + "毫秒";

        log.info(str);

        return pjp.proceed();

    }

}
