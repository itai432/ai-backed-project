package com.aibackend.aibackend.repository;

import com.aibackend.aibackend.model.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
}
