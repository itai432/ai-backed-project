package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.model.AppBook;
import com.aibackend.aibackend.service.AppBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private AppBookService appBookService;

    // Endpoint to create a single book
    @PostMapping("/create")
    public ResponseEntity<AppBook> createBook(@RequestBody AppBook book) {
        AppBook savedBook = appBookService.saveBook(book);
        return ResponseEntity.ok(savedBook);
    }

    // Endpoint to create multiple books
    @PostMapping("/createMultiple")
    public ResponseEntity<List<AppBook>> createBooks(@RequestBody List<AppBook> books) {
        List<AppBook> savedBooks = books.stream().map(appBookService::saveBook).toList();
        return ResponseEntity.ok(savedBooks);
    }

    @GetMapping
    public List<AppBook> getAllBooks() {
        return appBookService.getAllBooks();
    }
}
