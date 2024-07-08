package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.model.User;
import com.aibackend.aibackend.service.AiService;
import com.aibackend.aibackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/Ask")
public class HelloController {
    @Autowired
    private AiService aiService;
    @Autowired
    private UserService userService; // שינוי שם מן 'studentService' ל-'userService'

    @GetMapping("/getSQLQuery")
    public ResponseEntity<String> getSQLQuery(@RequestParam String userInput) {
        try {
            // יצירת שאילתת SQL מותאמת אישית מהמשתמש
            String sqlQuery = aiService.generateSQLQuery(userInput);
            // ביצוע השאילתה
            List<Object[]> results = userService.executeCustomQuery(sqlQuery);
            StringBuilder response = new StringBuilder("SQL Query: " + sqlQuery + "\n\nResults:\n");
            for (Object[] result : results) {
                response.append(java.util.Arrays.toString(result)).append("\n");
            }
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            // כאן מניח שהפונקציה executeCustomQuery מטפלת בשאילתות כלליות. עדיף להשתמש ב-JPA Repository אם אפשר.
            List<User> users = userService.getAllUsers(); // הפונקציה צריכה להשתמש ב-UserRepository שמחזיר את כל המשתמשים
            return new ResponseEntity<>(users, HttpStatus.OK);
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

    @GetMapping("/askAndSearchUsers")
    public ResponseEntity<?> askAndSearchUsers(@RequestParam String userInput) {
        try {
            String sqlQuery = aiService.generateSQLQuery(userInput);
            System.out.println("Generated SQL Query: " + sqlQuery);  // הדפסת השאילתה

            List<Object[]> results = userService.executeCustomQuery(sqlQuery);
            System.out.println("Query Results: " + Arrays.deepToString(results.toArray()));  // הדפסת תוצאות

            StringBuilder response = new StringBuilder("SQL Query: " + sqlQuery + "\n\nResults:\n");
            for (Object[] result : results) {
                response.append(Arrays.toString(result)).append("\n");
            }
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();  // הדפסת שגיאות
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/executeQuery")
    public ResponseEntity<?> executeQuery(@RequestParam String userInput) {
        try {
            List<Object[]> results = userService.executeCustomQuery(userInput);
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/populateUsers")
    public ResponseEntity<String> populateUsers() {
        try {
            userService.populateUsers();  // קריאה לפונקציה שיצרנו בשירות שממלאת את המשתמשים
            return new ResponseEntity<>("Users populated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to populate users: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
