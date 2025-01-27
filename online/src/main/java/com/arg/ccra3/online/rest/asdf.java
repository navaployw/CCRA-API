package com.arg.ccra3.online.rest;

import ch.qos.logback.classic.Logger;
import java.util.HashMap;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/asdf")
public class asdf {
    private final Logger logger = (Logger) LoggerFactory.getLogger(asdf.class);
    
    @GetMapping
    public String getMappingTest(){
        return "getMappingTest";
    }

    @PostMapping
    public String PostMapping(@RequestBody HashMap<String, Object> newUser){
        for (var entry : newUser.entrySet()) {
            logger.info(entry.getKey() + "/" + entry.getValue());
        }
        return "PostMapping";
    }

    @DeleteMapping
    public String DeleteMapping(){
        return "DeleteMapping";
    }

    @PutMapping
    public String PutMapping(){
        return "PutMapping";
    }

    @PatchMapping
    public String PatchMapping(){
        return "PatchMapping";
    }
}
