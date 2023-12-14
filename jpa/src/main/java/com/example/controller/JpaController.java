package com.example.controller;

import com.example.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/jpa")
public class JpaController {
    @Autowired
    private RedisService redisService ;

    @PostMapping()
    public String save(@RequestBody Map<String, Object> map){
        Set<String> keys = map.keySet();
        for (String key : keys) {
            redisService.set(key, map.get(key));
        }
        return "save ok";
    }

    @GetMapping()
    public Object get(@RequestParam("key") String key) throws JsonProcessingException {
        return redisService.get(key);
    }
}
