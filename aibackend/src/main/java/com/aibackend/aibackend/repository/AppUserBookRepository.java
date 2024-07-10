package com.aibackend.aibackend.repository;

import com.aibackend.aibackend.model.AppUserBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserBookRepository extends JpaRepository<AppUserBook, Long> {
}
