package com.aibackend.aibackend.controller;


import com.aibackend.aibackend.service.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RedisDatabaseController {

    @Autowired
    Redis redis;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/get-db-params")
    public ResponseEntity<Map<Object, Object>> getDatabaseParams() {
        Map<Object, Object> dbParams = redisTemplate.opsForHash().entries("db:params");
        return ResponseEntity.ok(dbParams);
    }

    @RequestMapping(value = "connect-db", method = RequestMethod.POST)
    public Map<String, String> setKey(@RequestBody Map<String, String> dbParams) {
        String url = dbParams.get("url");
        String username = dbParams.get("username");
        String password = dbParams.get("password");

        redisTemplate.opsForHash().put("db:params", "url", url);
        redisTemplate.opsForHash().put("db:params", "username", username);
        redisTemplate.opsForHash().put("db:params", "password", password);

        String token = UUID.randomUUID().toString();

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Database parameters saved successfully");
        response.put("token", token);

        return response;
}



    @RequestMapping(value = "getKey", method = RequestMethod.GET)
    public ResponseEntity<Map<Object, Object>> getKey(@RequestParam String hashKey) {
        // קבלת כל הערכים מה-Redis ב-hash
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(hashKey);

        // בדיקה אם ה-hash קיים
        if (!entries.isEmpty()) {
            return ResponseEntity.ok(entries);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Hash key not found"));
        }
    }



}