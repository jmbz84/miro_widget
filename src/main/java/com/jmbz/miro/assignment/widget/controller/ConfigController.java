package com.jmbz.miro.assignment.widget.controller;

import com.jmbz.miro.assignment.widget.model.WidgetConfig;
import com.jmbz.miro.assignment.widget.services.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
class ConfigController {

    @Autowired
    private RateLimiter rateLimiter;


    @PostMapping("/config")
    ResponseEntity updateConfig(@Valid @RequestBody WidgetConfig config) {
        rateLimiter.setReqPerMin(config.getRpmDefault());
        return ResponseEntity.status(HttpStatus.OK).body(config);
    }

}