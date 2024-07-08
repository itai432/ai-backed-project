package com.aibackend.aibackend.service;

import com.aibackend.aibackend.model.User;
import com.aibackend.aibackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Object[]> executeCustomQuery(String sql) {
        return entityManager.createNativeQuery(sql).getResultList();
    }
    @Transactional
    public void populateUsers() {
        List<User> users = Arrays.asList(
                new User("John Doe", 25),
                new User("Jane Smith", 30),
                new User("Alice Johnson", 28),
                new User("Michael Brown", 32),
                new User("Lisa White", 27),
                new User("Robert Jones", 24),
                new User("Laura Taylor", 29),
                new User("David Wilson", 31),
                new User("Sarah Moore", 26),
                new User("James Anderson", 35),
                new User("Maria Thomas", 33),
                new User("Charles Martinez", 22),
                new User("Emily Jackson", 34),
                new User("Daniel Lee", 21),
                new User("Emma Harris", 23),
                new User("Anthony Clark", 36),
                new User("Olivia Lewis", 37),
                new User("William Young", 39),
                new User("Sophia Hall", 28),
                new User("Isabella Allen", 40)
        );

        userRepository.saveAll(users);
    }

}
