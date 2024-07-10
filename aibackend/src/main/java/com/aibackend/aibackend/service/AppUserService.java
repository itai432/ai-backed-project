package com.aibackend.aibackend.service;

import com.aibackend.aibackend.model.AppUser;
import com.aibackend.aibackend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }

    public List<AppUser> getAllUsers() {
        List<AppUser> users = new ArrayList<>();
        appUserRepository.findAll().forEach(users::add);
        return users;
    }

    public List<Object[]> executeCustomQuery(String sql) {
        return entityManager.createNativeQuery(sql).getResultList();
    }
}
