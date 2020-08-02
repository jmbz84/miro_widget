package com.jmbz.miro.assignment.widget.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateLimitInfo {

    String endpoint;
    Integer rateLimit;
    Integer availableRequests;
}
