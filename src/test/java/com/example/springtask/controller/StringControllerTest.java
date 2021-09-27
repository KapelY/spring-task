package com.example.springtask.controller;

import com.example.springtask.exception.CustomException;
import com.example.springtask.service.CRUDService;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.example.springtask.controller.StringController.CANNOT_BE_EMPTY;
import static com.example.springtask.service.CRUDServiceImpl.Storage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration
public class StringControllerTest {
    private final Storage storage = new Storage();
    private final static String PARAM_STRING = "{\"array\":[\"Antony\",\"Joshua\",\"Winner\",\"Champion\"]}";
    private final static String BAD_PARAM_STRING = "\"array\":[\"A\",\"J\"]";
    private final static String BAD_PARAM_STRING2 = "{\"array\": \"A\"}";
    private final static String PARAM_STRING3 = "{\"array\":[\"Antony\"]}";
    private final static String BAD_PARAM_STRING4 = "{\"array\": []}";

    @Autowired
    MockMvc mvc;

    @Mock
    CRUDService crudService;

    @Autowired
    StringController stringController;

    @BeforeEach
    void setUp() {
        storage.setArray(List.of("Antony", "Joshua", "Winner", "Champion"));
        mvc = MockMvcBuilders.standaloneSetup(stringController).build();
        when(crudService.getAll()).thenReturn(storage.getArray());
    }

    @Test
    public void whenDataAppendOK() throws Exception {
        this.mvc.perform(put("/append")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(PARAM_STRING))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void whenDataAppendFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.put("/append")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void exceptionTesting() {
        storage.setArray(Collections.emptyList());
        assertThatThrownBy(() -> mvc
                .perform(put("/append").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING4)))
                .hasCause(new CustomException(CANNOT_BE_EMPTY, HttpStatus.BAD_REQUEST));
    }

    @Test
    void whenDataAddOk() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.post("/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(PARAM_STRING3))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void whenDataAddFails() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.post("/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING2))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void getAmountOfStrings() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/amount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Configuration
    public static class TestConfig {

        @Bean
        public CRUDService crudService() {
            return Mockito.mock(CRUDService.class);
        }

        @Bean
        public StringController stringController() {
            return new StringController(crudService());
        }
    }
}