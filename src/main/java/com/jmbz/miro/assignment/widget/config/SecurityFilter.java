package com.jmbz.miro.assignment.widget.config;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(httpRequest, httpResponse);
    }
}