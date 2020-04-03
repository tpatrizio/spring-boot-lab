package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.model.Greeting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingRepository extends JpaRepository<Greeting, Long> {

  public Optional<Greeting> findByLanguage(String language);
  
}