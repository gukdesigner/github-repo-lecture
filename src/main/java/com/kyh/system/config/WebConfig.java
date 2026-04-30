package com.kyh.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kyh.system.interceptor.LoginInterceptor;

// Spring MVC の設定クラス。インターセプターの登録を行う。
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // /employee/** 配下のすべてのリクエストにログインチェックを適用する
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/employee", "/employee/**")
                .excludePathPatterns("/employee/*.css", "/employee/*.js");
    }
}
