package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import com.example.demo.controller.GreetingController;
import com.example.demo.controller.ResourceMapperImpl;
import com.example.demo.controller.errors.GlobalExceptionHandler;
import com.example.demo.model.Greeting;
import com.example.demo.service.GreetingNotFoundException;
import com.example.demo.service.GreetingService;
import com.google.common.collect.Lists;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(GreetingController.class)
@ContextConfiguration(classes = { GreetingController.class, GlobalExceptionHandler.class, ResourceMapperImpl.class })
@ExtendWith(RestDocumentationExtension.class)
public class GreetingControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  GreetingService service;

  RestDocumentationContextProvider restDocumentation;

  @BeforeEach
  public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(
            documentationConfiguration(restDocumentation).operationPreprocessors().withResponseDefaults(prettyPrint()))
        .alwaysDo(print())
        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .build();
  }

  @Test
  public void getAllShouldReturnListOfGreetings() throws Exception {
    when(service.getAllGreetings(any(Pageable.class))).thenReturn(
        new PageImpl<>(Lists.newArrayList(new Greeting(1L, "en", "Hello"), new Greeting(2L, "it", "Ciao"))));
    mockMvc.perform(get("/greetings?page=0&size=2").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].message", is("Hello"))).andExpect(jsonPath("$.[0].language", is("en")))
        .andExpect(jsonPath("$.[1].message", is("Ciao"))).andExpect(jsonPath("$.[1].language", is("it")))
        .andExpect(header().longValue("X-Todos-Total", 2L))
        .andDo(document("{method-name}", pageParameters(), greetingsCollection(), pageHeaders()));
    ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
    verify(service).getAllGreetings(captor.capture());
    assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
    assertThat(captor.getValue().getPageSize()).isEqualTo(2);
  }

  @Test
  public void getWithKnownLanguageShouldReturnGreeting() throws Exception {
    when(service.getGreeting(Locale.ENGLISH)).thenReturn(new Greeting(1L, "en", "Hello"));
    mockMvc.perform(get("/greetings/{languageCode}", "en").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("{method-name}",
            pathParameters(parameterWithName("languageCode").description("The two letters language code")),
            greeting()));
  }

  @Test
  public void getWithUnknownLanguageShouldReturnNotFound() throws Exception {
    when(service.getGreeting(Locale.FRENCH))
        .thenThrow(new GreetingNotFoundException(Locale.FRENCH.getDisplayLanguage()));
    mockMvc.perform(get("/greetings/{languageCode}", "fr").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("NOT_FOUND")))
        .andExpect(jsonPath("$.message", is("Could not find a greeting for " + Locale.FRENCH.getDisplayLanguage())))
        .andDo(document("{method-name}",
            pathParameters(parameterWithName("languageCode").description("The two letters language code")),
            apiError()));
  }

  private ResponseFieldsSnippet apiError() {
    return responseFields(
      fieldWithPath("status").description("The HTTP status of the request"),
      fieldWithPath("timestamp").description("The timestamp of the error"),
      fieldWithPath("path").description("The path of the requested resource"),
      fieldWithPath("message").description("The error message"),
      fieldWithPath("debug").description("A detailed description of the error").type(STRING).optional(),
      fieldWithPath("errors").description("The list of errors if any").type(ARRAY).optional()
    );
  }

  private ResponseFieldsSnippet greeting() {
    return responseFields(
        fieldWithPath("id").description("The unique identifier of the greeting"),
        fieldWithPath("language").description("The language of the greeting"),
        fieldWithPath("message").description("The greeting message")
    );
  }

  private ResponseFieldsSnippet greetingsCollection() {
    return responseFields(fieldWithPath("[].id").description("The unique identifier of the greeting"),
      fieldWithPath("[].language").description("The language of the greeting"),
      fieldWithPath("[].message").description("The greeting message").optional());
  }

  private RequestParametersSnippet pageParameters() {
    return requestParameters(
      parameterWithName("page").description("The page to retrieve").optional(),
      parameterWithName("size").description("The number of elements within a single page").optional()
    );
  }

  private ResponseHeadersSnippet pageHeaders() {
    return responseHeaders(
      headerWithName("X-Todos-Total").description("The total amount of grretings")
    );
  }

}