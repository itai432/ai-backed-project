package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.model.Student;
import com.aibackend.aibackend.service.AiService;
import com.aibackend.aibackend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/Ask")
public class HelloController {

    @Autowired
    private AiService aiService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/getSQLQuery")
    public ResponseEntity<String> getSQLQuery(@RequestParam String userInput) {
        try {
            // שליפת הנתונים מה-API החיצוני
            String apiResponse = aiService.getAPIResponse("https://freetestapi.com/api/v1/students");

            // שליחת הנתונים והבקשה ל-ChatGPT
            String chatGPTResponse = aiService.sendToChatGPT(apiResponse, userInput);
            return new ResponseEntity<>(chatGPTResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/checkConnectionWithDetails")
    public ResponseEntity<String> checkConnectionWithDetails(
            @RequestParam String url,
            @RequestParam String username,
            @RequestParam String password) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection.isValid(1)) {
                return new ResponseEntity<>("Connection is valid", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Connection is not valid", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>("Failed to connect to the database: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
