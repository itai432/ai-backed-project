package com.aibackend.aibackend.repository;

import com.aibackend.aibackend.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
}
