package com.jmbz.miro.assignment.widget.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class SecurityConfiguration {
    @Bean
    public FilterRegistrationBean dawsonApiFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean();
        registration.setFilter(new SecurityFilter());
        return registration;
    }
}