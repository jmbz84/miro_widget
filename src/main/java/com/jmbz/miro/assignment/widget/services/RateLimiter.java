package com.jmbz.miro.assignment.widget.services;

import com.jmbz.miro.assignment.widget.model.RateLimitInfo;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

import static com.jmbz.miro.assignment.widget.utils.Utils.getJson;

/**
 *  Rate Limiter Component
 *  Limits the number of request per minute with the property 'ratelimit.rpm'
 */
@Controller
public class RateLimiter extends OncePerRequestFilter {

    private Bucket bucket;
    private Refill refill;
    private Bandwidth limit;

    @Value("${ratelimit.rpm}")
    private Integer reqPerMin;

    @Value("${ratelimit.endpoint}")
    private String endpoint;


    @PostConstruct
    private void init() {
        build();
    }

    public ConsumptionProbe tryConsumeAndReturnRemaining(long total){
        return bucket.tryConsumeAndReturnRemaining(total);
    }

    public void setReqPerMin(Integer reqPerMin){
        this.reqPerMin=reqPerMin;
        build();
    }

    private void build() {
        this.refill = Refill.intervally(reqPerMin, Duration.ofMinutes(1));
        this.limit = Bandwidth.classic(reqPerMin, refill);
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        RateLimitInfo rateLimitInfo=new RateLimitInfo(endpoint,reqPerMin,(int)bucket.getAvailableTokens());
        httpResponse.setHeader("RATE_LIMIT",getJson(rateLimitInfo));
        filterChain.doFilter(httpRequest, httpResponse);
    }


}
