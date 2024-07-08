package com.aibackend.aibackend.repository;

import com.aibackend.aibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = ":sqlQuery", nativeQuery = true)
    List<Object[]> executeDynamicQuery(@Param("sqlQuery") String sqlQuery);
}
