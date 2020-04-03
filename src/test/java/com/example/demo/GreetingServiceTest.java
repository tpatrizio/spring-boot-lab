package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.is;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Locale;
import java.util.Optional;

import com.example.demo.mapper.GreetingMapper;
import com.example.demo.model.Greeting;
import com.example.demo.repository.GreetingRepository;
import com.example.demo.service.GreetingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests of the Greeting Service")
public class GreetingServiceTest {

  @Mock
  GreetingRepository repository;

  @InjectMocks // auto inject repository
  GreetingService service;

  @BeforeEach
  void setMockOutput() {
      when(repository.findByLanguage("en")).thenReturn(
        Optional.of(new Greeting(1L, "en", "Hello"))
      );
  }

  @Test
	void getGreetingForKnownLanguage() {
		assertEquals("Hello", service.getGreeting(Locale.ENGLISH).getMessage());
  }
  
}