package com.example.demo.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import com.example.demo.mapper.GreetingMapper;
import com.example.demo.model.Greeting;
import com.example.demo.service.GreetingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class GreetingController {

  @Autowired
  GreetingService service;

  @GetMapping(value = "/greetings/{languageCode}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GreetingModel> getGreeting(@PathVariable("languageCode") String language) {
      Locale locale = Locale.forLanguageTag(language);
      return ResponseEntity.ok(service.getGreeting(locale));
  }

  @GetMapping(value = "/greetings", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<GreetingModel>> getAllGreetings(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @Valid @Positive(message = "Page size should be a positive number")
      @RequestParam(required = false, defaultValue = "10") Integer size) {
      Page<Greeting> greetingEntities = service.getAllGreetings(PageRequest.of(page, size));
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-Todos-Total", Long.toString(greetingEntities.getTotalElements()));
      GreetingMapper mapper = new GreetingMapper();
      List<GreetingModel> greetingModels = mapper.toModels(greetingEntities.getContent());
      return new ResponseEntity<>(greetingModels, headers, HttpStatus.OK);
  }

  @PostMapping(value = "/greetings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GreetingModel> postNewGreeting(@RequestBody @Valid GreetingModel greeting) {
      GreetingMapper mapper = new GreetingMapper();
      Greeting savedGreeting = service.addGreeting(mapper.toEntity(greeting));
      return ResponseEntity.ok(mapper.toModel(savedGreeting));
  }
  
}