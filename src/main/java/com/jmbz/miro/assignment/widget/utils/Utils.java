package com.jmbz.miro.assignment.widget.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jmbz.miro.assignment.widget.utils.Constants.RESPONSE_DATETIME_FORMAT;

public class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    public static String getJson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            LOGGER.error("Error creating json from Object:",ex);
        }
        return "";
    }

    public static String getDate(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(RESPONSE_DATETIME_FORMAT));
    }

    public static String getMethodEndpoint(String method,String endpoint) {
     return method.concat(":").concat(endpoint);
    }


}
