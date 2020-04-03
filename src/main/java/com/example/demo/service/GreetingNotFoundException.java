package com.example.demo.service;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GreetingNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GreetingNotFoundException(String language) {
        super("Could not find a greeting for " + language);
    }

}