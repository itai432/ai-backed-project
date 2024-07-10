package com.aibackend.aibackend.service;

import com.aibackend.aibackend.model.AppUserBook;
import com.aibackend.aibackend.repository.AppUserBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserBookService {

    @Autowired
    private AppUserBookRepository appUserBookRepository;

    public AppUserBook saveUserBook(AppUserBook userBook) {
        return appUserBookRepository.save(userBook);
    }

    public List<AppUserBook> getAllUserBooks() {
        return appUserBookRepository.findAll();
    }
}
