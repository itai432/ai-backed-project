package com.aibackend.aibackend.controller;

import com.aibackend.aibackend.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/Ask")
public class HelloController {

    @Autowired
    AiService aiService;

    @RequestMapping(value = "/AI", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam String keyword)
    {
        return new ResponseEntity<>(aiService.searchProducts(keyword), HttpStatus.OK);
}
}