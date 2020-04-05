package com.example.demo;

import static org.hamcrest.Matchers.equalTo;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootIntegrationTest
@DisplayName("Integration Tests of the Greeting Controller")
@FlywayTest
class GreetingControllerIT {

  @Autowired
  WebApplicationContext webContext;

  @BeforeEach
  public void setUp() {
    RestAssuredMockMvc.webAppContextSetup(webContext);
  }

  @Test
  @FlywayTest(locationsForMigrate = "classpath:/db/data")
  public void getGreetingForKnownLocaleReturnsOK() {
    RestAssuredMockMvc.given()
        .when()
          .get("/greetings/en")
        .then()
          .log().ifValidationFails()
          .statusCode(200)
          .contentType(ContentType.JSON)
          .body("message", equalTo("Hello"));
  }

}
