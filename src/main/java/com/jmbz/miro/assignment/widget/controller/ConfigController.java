package com.jmbz.miro.assignment.widget.controller;

import com.jmbz.miro.assignment.widget.model.WidgetConfig;
import com.jmbz.miro.assignment.widget.services.RateLimiter;
import com.jmbz.miro.assignment.widget.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class ConfigController {

    @Autowired
    private RateLimiter rateLimiter;

    @PatchMapping("/config")
    ResponseEntity updateConfig(@Valid @RequestBody WidgetConfig config) {
        if(!rateLimiter.contains(Utils.getMethodEndpoint(config.getMethod(),config.getEndpoint()))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("method and endpoint config not found");
        }
        rateLimiter.setReqPerMin(Utils.getMethodEndpoint(config.getMethod(),config.getEndpoint()),config.getRpm());
        return ResponseEntity.status(HttpStatus.OK).body(config);
    }

}