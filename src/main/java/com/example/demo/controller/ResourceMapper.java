package com.example.demo.controller;

import java.util.List;

import com.example.demo.model.Greeting;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {

  public GreetingResource toResource(Greeting greeting);

  public List <GreetingResource> toResources(List <Greeting> greetings);

  public Greeting toEntity(GreetingResource greeting);

}