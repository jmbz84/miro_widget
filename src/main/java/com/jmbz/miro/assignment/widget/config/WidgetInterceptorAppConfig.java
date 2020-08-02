package com.jmbz.miro.assignment.widget.config;

import com.jmbz.miro.assignment.widget.controller.interceptor.WidgetControllerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WidgetInterceptorAppConfig implements WebMvcConfigurer {
    @Autowired
    WidgetControllerInterceptor widgetControllerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(widgetControllerInterceptor);
    }
}