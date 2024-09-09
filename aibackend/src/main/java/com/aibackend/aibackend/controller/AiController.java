package com.aibackend.aibackend.controller;


import com.aibackend.aibackend.service.AiService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/Ask")
public class AiController {

    @Autowired
    private AiService aiService;

    private final Gson gson = new Gson();

    @GetMapping("/askChatGPT")
    public ResponseEntity<String> executeQuery(@RequestParam String userInput) {
        try {
            String sqlQuery = aiService.generateSQLQuery(userInput);
            aiService.saveCallHistory(userInput, sqlQuery);
            String result = aiService.executeSQLQuery(sqlQuery);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalStateException e) {

            String errorResponse = gson.toJson(new ErrorResponse("Data Error", e.getMessage()));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {

            String errorResponse = gson.toJson(new ErrorResponse("IO Error", e.getMessage()));
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {

            String errorResponse = gson.toJson(new ErrorResponse("SQL Error", e.getMessage()));
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {

            String errorResponse = gson.toJson(new ErrorResponse("Unknown Error", e.getMessage()));
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // מחלקת שגיאות למענה אחיד
    private static class ErrorResponse {
        private String status;
        private String message = "גשדגשדגשדג";

        public ErrorResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
}
}
}