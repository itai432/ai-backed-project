package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.service.AiService;
import com.aibackend.aibackend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/Ask")
public class HelloController {
    @Autowired
    private AiService aiService;
    @Autowired
    private AppUserService userService;

    @GetMapping("/getSQLQuery")
    public ResponseEntity<String> getSQLQuery(@RequestParam String userInput) {
        try {
            String sqlQuery = aiService.generateSQLQuery(userInput);
            List<Object[]> results = userService.executeCustomQuery(sqlQuery);
            StringBuilder response = new StringBuilder("SQL Query: " + sqlQuery + "\n\nResults:\n");
            for (Object[] result : results) {
                response.append(Arrays.toString(result)).append("\n");
            }
            userService.saveCallHistory(userInput, response.toString()); // שמירת היסטוריה
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/executeQuery")
    public ResponseEntity<?> executeQuery(@RequestParam String userInput) {
        try {
            String sqlQuery = aiService.generateSQLQuery(userInput);
            List<Object[]> results = userService.executeCustomQuery(sqlQuery);
            userService.saveCallHistory(userInput, results.toString()); // שמירת היסטוריה
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
