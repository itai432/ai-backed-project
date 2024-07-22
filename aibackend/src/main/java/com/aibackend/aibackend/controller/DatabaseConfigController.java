package com.aibackend.aibackend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DatabaseConfigController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/test-connection")
    public Map<String, String> testConnection(@RequestBody Map<String, String> dbDetails) {
        String url = dbDetails.get("url");
        String username = dbDetails.get("username");
        String password = dbDetails.get("password");

        Map<String, String> response = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection.isValid(2)) {
                response.put("message", "Connection successful");
            } else {
                response.put("message", "Connection failed: Connection is not valid");
            }
        } catch (Exception e) {
            response.put("message", "Connection failed: " + e.getMessage());
        }
        return response;
    }
}
