package com.aibackend.aibackend.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Parameters required to connect to a database")
public class DatabaseParams {

    @ApiModelProperty(value = "JDBC URL of the database", example = "jdbc:postgresql://localhost:5432/postgres", required = true)
    private String url;

    @ApiModelProperty(value = "Username for database connection", example = "postgres", required = true)
    private String username;

    @ApiModelProperty(value = "Password for database connection", example = "password123", required = true)
    private String password;

    // Getter for url
    public String getUrl() {
        return url;
    }

    // Setter for url
    public void setUrl(String url) {
        this.url = url;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DatabaseParams{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password=''}"; // Do not show password in toString()
    }
}
