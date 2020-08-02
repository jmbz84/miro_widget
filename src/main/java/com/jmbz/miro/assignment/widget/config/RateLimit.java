package com.jmbz.miro.assignment.widget.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.Data;

import java.time.Duration;

@Data
public class RateLimit {
    private Bucket bucket;
    private Refill refill;
    private Bandwidth limit;

    private Integer reqPerMin;
    private String endpoint;

    public RateLimit(Integer reqPerMin,String endpoint){
        this.reqPerMin=reqPerMin;
        this.endpoint=endpoint;
        this.build();
    }
    public void build() {
        this.refill = Refill.intervally(reqPerMin, Duration.ofMinutes(1));
        this.limit = Bandwidth.classic(reqPerMin, refill);
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }
    public ConsumptionProbe tryConsumeAndReturnRemaining(long total){
        return bucket.tryConsumeAndReturnRemaining(total);
    }

    public RateLimit setReqPerMin(Integer reqPerMin){
        this.reqPerMin=reqPerMin;
        return this;
    }

    public Integer getAvailableTokens() {
        return (int)this.bucket.getAvailableTokens();
    }
}
