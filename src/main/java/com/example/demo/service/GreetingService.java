package com.example.demo.service;

import java.util.Locale;

import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

  @Autowired
  GreetingRepository repository;

  public Page<Greeting> getAllGreetings(Pageable pageable) {
    return repository.findAll(pageable);
  }
  
  public Greeting getGreeting(Locale locale) {
      return repository.findByLanguage(locale.getLanguage())
          .orElseThrow(() -> new GreetingNotFoundException(locale.getDisplayLanguage()));
  }

  public Greeting addGreeting(Greeting greeting) {
    return repository.save(greeting);
  }

}