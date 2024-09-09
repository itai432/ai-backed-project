package com.aibackend.aibackend.controller;


import com.aibackend.aibackend.model.DatabaseParams;
import com.aibackend.aibackend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connect-db")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping
    public ResponseEntity<Object> connectDatabase(@RequestBody DatabaseParams databaseParams) {
        try {
            databaseService.saveDatabaseParamsToRedis(databaseParams);
            Object result = databaseService.getDatabaseSchema(databaseParams);
            return ResponseEntity.ok("Connecting to the database was successful!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving database schema: " + e.getMessage());
}
}
}