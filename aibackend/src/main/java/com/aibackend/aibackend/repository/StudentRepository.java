package com.aibackend.aibackend.repository;

import com.aibackend.aibackend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
