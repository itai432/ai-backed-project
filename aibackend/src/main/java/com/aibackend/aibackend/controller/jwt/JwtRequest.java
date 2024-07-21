package com.aibackend.aibackend.controller.jwt;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;
    private String email; // הוסף את שדה האימייל

    //need default constructor for JSON Parsing
    public JwtRequest() {

    }

    public JwtRequest(String username, String password, String email) {
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email); // הוסף את שדה האימייל
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { // הוסף את שדה האימייל
        return this.email;
    }

    public void setEmail(String email) { // הוסף את שדה האימייל
        this.email = email;
    }
}
