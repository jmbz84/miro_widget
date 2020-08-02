package com.jmbz.miro.assignment.widget.services;

import com.jmbz.miro.assignment.widget.config.RateLimit;
import com.jmbz.miro.assignment.widget.model.RateLimitInfo;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.jmbz.miro.assignment.widget.utils.Utils.getJson;
import static com.jmbz.miro.assignment.widget.utils.Utils.getMethodEndpoint;

/**
 *  Rate Limiter Component
 *  Limits the number of request per minute with the property 'ratelimit.rpm'
 */
@Controller
public class RateLimiter extends OncePerRequestFilter {

    @Value("#{'${rate.limit.endpoint.list}'.split(',')}")
    private List<String> endpointList;

    @Value("#{'${rate.limit.value.list}'.split(',')}")
    private List<Integer> rpmList;

    private List<RateLimit> rateLimitList=new ArrayList<>();

    @PostConstruct
    private void init() {
        if(rpmList.size()!=endpointList.size()){
            throw new IllegalArgumentException("endpointList and rpmList must be the same size.");
        }
        if(endpointList==null || endpointList.isEmpty()){
            endpointList.add("ALL:default");
            rpmList.add(100);
        }

        create();
        build();
    }

    private void create(){
        for(int i=0;i<this.rpmList.size();i++){
            rateLimitList.add(new RateLimit(rpmList.get(i),endpointList.get(i)));
        }
    }

    private void build(){
        rateLimitList.stream().forEach(rateLimit -> rateLimit.build());
    }

    private RateLimit getRateLimiter(String endpoint) {
        int index=0;
        for(int i=0;i<this.endpointList.size();i++){
            if(endpointList.get(i).equalsIgnoreCase(endpoint)){
                index=i;break;
            }
        }
        return rateLimitList.get(index);
    }

    public ConsumptionProbe tryConsumeAndReturnRemaining(String endpoint,long total){
        return getRateLimiter(endpoint).tryConsumeAndReturnRemaining(total);
    }

    public void setReqPerMin(String endpoint,Integer rpm){
        this.getRateLimiter(endpoint).setReqPerMin(rpm).build();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        RateLimit rateLimit=getRateLimiter(getMethodEndpoint(httpRequest.getMethod(),httpRequest.getRequestURI()));

        RateLimitInfo rateLimitInfo=new RateLimitInfo(rateLimit.getEndpoint(),rateLimit.getReqPerMin(),rateLimit.getAvailableTokens());
        httpResponse.setHeader("RATE_LIMIT",getJson(rateLimitInfo));
        filterChain.doFilter(httpRequest, httpResponse);
    }

    public boolean contains(String endpoint){
        return endpointList.stream().filter(listItem-> listItem.equalsIgnoreCase(endpoint)).findFirst().isPresent();
    }


}
