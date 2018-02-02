package com.edf.datalake.controller;

import com.edf.datalake.model.dto.MessagesDTO;
import com.edf.datalake.model.dto.Status;
import com.edf.datalake.service.AccessPointService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1.0")
public class ApiOutController {

    @Autowired
    private AccessPointService accessPointService;

    @Autowired
    private Environment env;

    private Logger logger = LoggerFactory.getLogger(ApiOutController.class);
    private static final String TOPIC_PREFIX = "topic.prefix";

    @GetMapping(path = "/topics/{topic}", produces = "application/json")
    public ResponseEntity<List<JSONObject>> getAllMessages(@PathVariable(value = "topic") String topic, @RequestHeader("Authorization") String apiKey) {

        String fullTopic = env.getProperty(TOPIC_PREFIX) + topic;
        Boolean granted = accessPointService.checkPrerequisites(fullTopic, apiKey);
        MessagesDTO results;

        if(granted) {
            results = accessPointService.getCurrentMessages(apiKey, fullTopic);

            if( Status.GRANTED.equals(results.status) ) {
                return new ResponseEntity<>(results.messages, HttpStatus.OK);
            }

            if( Status.BUSY_ERROR.equals(results.status) ) {
                logger.error("BUSY ERROR !");
                return new ResponseEntity<>(results.messages, HttpStatus.CONFLICT);
            }

            if( Status.PARSE_ERROR.equals(results.status) ) {
                logger.error("PARSING ERROR !");
                return new ResponseEntity<>(results.messages, HttpStatus.FORBIDDEN);
            }

            return new ResponseEntity<>(results.messages, HttpStatus.NO_CONTENT);

        } else {
            logger.info("NOT GRANTED");
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/shutdown")
    public ResponseEntity shutdown() {
        accessPointService.shutdown();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

