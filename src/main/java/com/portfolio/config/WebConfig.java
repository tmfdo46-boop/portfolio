package com.portfolio.config;

import com.portfolio.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginCheckInterceptor loginCheckInterceptor;

    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns(
                        "/main",
                        "/posts/**",
                        "/messages/**",
                        "/alerts/**",
                        "/users/profile"
                )
                .excludePathPatterns(
                        "/users/login",
                        "/users/register",
                        "/css/**",
                        "/js/**",
                        "/icons/**"
                );
    }
}
