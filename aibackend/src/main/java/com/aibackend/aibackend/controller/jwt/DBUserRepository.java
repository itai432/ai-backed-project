package com.aibackend.aibackend.controller.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DBUserRepository extends JpaRepository<DBUser, Long> {
    Optional<DBUser> findByUsername(String username); // השתמשנו ב-username במקום name
}
