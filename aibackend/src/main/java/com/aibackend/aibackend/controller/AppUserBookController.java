package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.model.AppUserBook;
import com.aibackend.aibackend.service.AppUserBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userbooks")
public class AppUserBookController {

    @Autowired
    private AppUserBookService appUserBookService;

    @PostMapping
    public ResponseEntity<AppUserBook> createUserBook(@RequestBody AppUserBook userBook) {
        AppUserBook savedUserBook = appUserBookService.saveUserBook(userBook);
        return ResponseEntity.status(201).body(savedUserBook);
    }
}
