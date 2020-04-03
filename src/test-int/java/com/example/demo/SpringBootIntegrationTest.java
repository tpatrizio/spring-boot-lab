package com.example.demo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FlywayTestExtension
@ActiveProfiles
public @interface SpringBootIntegrationTest {

  @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles") 
  String[] activeProfiles() default {"it"};

} 