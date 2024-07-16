package com.aibackend.aibackend.repository;

import com.aibackend.aibackend.model.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    AppUser findByUsernameAndPassword(String username, String password);
}
