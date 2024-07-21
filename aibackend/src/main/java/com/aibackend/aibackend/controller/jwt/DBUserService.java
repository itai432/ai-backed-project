package com.aibackend.aibackend.controller.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DBUserService {

    @Autowired
    private DBUserRepository dbUserRepository;

    public DBUser save(DBUser user) {
        return dbUserRepository.save(user);
    }

    public Optional<DBUser> findByUsername(String username) {
        return dbUserRepository.findByUsername(username);
    }
}
