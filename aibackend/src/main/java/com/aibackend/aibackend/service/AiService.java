package com.aibackend.aibackend.service;

import org.springframework.stereotype.Service;

@Service
public class AiService {

    public String searchProducts(String keyword) {
        return "Searched for:" + keyword;
}
}