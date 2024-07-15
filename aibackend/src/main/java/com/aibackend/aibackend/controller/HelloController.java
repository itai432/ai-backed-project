package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.service.AiService;
import com.aibackend.aibackend.service.AppUserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Ask")
public class HelloController {
    @Autowired
    private AiService aiService;
    @Autowired
    private AppUserService userService;
    private final Gson gson = new Gson();

    @GetMapping("/getSQLQuery")
    public ResponseEntity<String> getSQLQuery(@RequestParam String userInput) {
        try {
            String sqlQuery = aiService.generateSQLQuery(userInput);
            List<Object[]> results = userService.executeCustomQuery(sqlQuery);
            String jsonResponse = gson.toJson(results);
            userService.saveCallHistory(userInput, jsonResponse);
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/executeQuery")
    public ResponseEntity<?> executeQuery(@RequestParam String userInput) {
        try {
            String sqlQuery = aiService.generateSQLQuery(userInput);
            List<Object[]> results = userService.executeCustomQuery(sqlQuery);
            String jsonResponse = gson.toJson(results);
            userService.saveCallHistory(userInput, jsonResponse);
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
