package com.atguigu.config;

import com.atguigu.handler.SHFAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity //@EnableWebSecurity是开启springSecurity的默认行为
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
        auth.inMemoryAuthentication()
                .withUser("whitezhu")
//                .password("{noop}whitezhu") //{noop}是不加密
                .password(passwordEncoder.encode("whitezhu"))
                .roles("");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //静态资源放行
        http.authorizeRequests()
                        .antMatchers("/static/**","/login","/login.html").permitAll()   //静态资源和login放行
                        .anyRequest().authenticated();  //已认证其他路径放行

        //登录页面
        http.formLogin()
                .loginPage("/login.html")   //登录页面
                        .loginProcessingUrl("/login")   //登录验证请求
                                .defaultSuccessUrl("/") //登录成功的请求
                                        .failureForwardUrl("/login.html");  //登录失败的请求

        //允许iframe嵌套显示
        http.headers().frameOptions().disable();

        //注销登录
        http.logout()
                .logoutUrl("/logout")   //登出请求
                .logoutSuccessUrl("/login.html");   //登出成功请求

        //禁用csrf
        http.csrf().disable();

        //自定义403页面
        http.exceptionHandling()
                .accessDeniedHandler(new SHFAccessDeniedHandler());
    }
}
