package com.aibackend.aibackend.service;

import com.aibackend.aibackend.model.AppBook;
import com.aibackend.aibackend.repository.AppBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppBookService {

    @Autowired
    private AppBookRepository appBookRepository;

    public AppBook saveBook(AppBook book) {
        return appBookRepository.save(book);
    }

    public List<AppBook> getAllBooks() {
        List<AppBook> books = new ArrayList<>();
        appBookRepository.findAll().forEach(books::add);
        return books;
    }
}
