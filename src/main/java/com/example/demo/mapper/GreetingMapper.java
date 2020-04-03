package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.controller.GreetingModel;
import com.example.demo.model.Greeting;

public class GreetingMapper {

  public GreetingModel toModel(Greeting greeting) {
    return new GreetingModel(greeting.getId(), greeting.getLanguage(), greeting.getMessage());
  }

  public List <GreetingModel> toModels(List <Greeting> greetings) {
    return greetings.stream().map(this::toModel).collect(Collectors.toList());
  }

  public Greeting toEntity(GreetingModel greeting) {
    return new Greeting(greeting.getId(), greeting.getLanguage(), greeting.getMessage());
  }

}