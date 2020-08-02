package com.jmbz.miro.assignment.widget.controller.interceptor;

import com.jmbz.miro.assignment.widget.services.RateLimiter;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jmbz.miro.assignment.widget.utils.Constants.RESPONSE_DATETIME_FORMAT;

/**
 * Intercepts incoming request in order to validate teh rate limiting
 */
@Component
public class WidgetControllerInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ConsumptionProbe probe=rateLimiter.tryConsumeAndReturnRemaining(1L);

        if(!probe.isConsumed()){
            LocalDateTime date=LocalDateTime.now();
            date.plusNanos(probe.getNanosToWaitForRefill());
            response.addHeader("RATE_LIMIT_NEXT_REFILL",date.format(DateTimeFormatter.ofPattern(RESPONSE_DATETIME_FORMAT)));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) { }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception exception) {}
}
