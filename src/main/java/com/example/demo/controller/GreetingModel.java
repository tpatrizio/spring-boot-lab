package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class GreetingModel {

  Long id;

  String language;

  String message;

}