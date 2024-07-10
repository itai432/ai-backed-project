package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.model.AppUser;
import com.aibackend.aibackend.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        return appUserService.saveUser(user);
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }
}
