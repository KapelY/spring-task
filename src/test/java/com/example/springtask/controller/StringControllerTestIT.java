package com.example.springtask.controller;

import com.example.springtask.exception.CustomException;
import com.example.springtask.repositories.StringRepository;
import com.example.springtask.repositories.StringRepositoryImpl;
import com.example.springtask.service.CRUDService;
import com.example.springtask.service.CRUDServiceImpl;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.example.springtask.controller.StringController.*;
import static com.example.springtask.service.CRUDServiceImpl.Storage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ContextConfiguration
public class StringControllerTestIT {
    private final static List<String> STORAGE_DEFAULT = List.of("Antony", "Joshua", "Winner", "Champion");
    private final static String PARAM_STRING = "{\"array\":[\"Antony\",\"Joshua\",\"Winner\",\"Champion\"]}";
    private final static String BAD_PARAM_STRING = "\"array\":[\"A\",\"J\"]";
    private final static String BAD_PARAM_STRING2 = "{\"array\": \"A\"}";
    private final static String PARAM_STRING3 = "{\"array\":[\"Antony\"]}";
    private final static String PARAM_EXPECTED3 = "{\"array\":[\"Antony\",\"Joshua\",\"Winner\",\"Champion\",\"Antony\"]}";
    private final static String BAD_PARAM_STRING4 = "{\"array\": []}";

    @Autowired
    StringRepository stringRepository;

    @Autowired
    MockMvc mvc;

    @Autowired
    CRUDService crudService;

    @Autowired
    StringController stringController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(stringController).build();
        stringRepository.clear();
    }

    @Test
    @DisplayName("When call PUT_STORAGE then storage must have correct size & content")
    public void whenDataAppendOK() throws Exception {
        assertThat(crudService.getAmount().equals(0)).isTrue();

        this.mvc.perform(put(PUT_STORAGE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(PARAM_STRING))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());

        assertThat(crudService.getAmount().equals(4)).isTrue();
        assertThat(crudService.getAll()).isEqualTo(STORAGE_DEFAULT);
    }

    @Test
    @DisplayName("When call PUT_STORAGE fails then storage must contain empty List<String>")
    public void whenDataAppendFail() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.put(PUT_STORAGE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING))
                .andExpect(status().isBadRequest())
                .andExpect(status().is4xxClientError());

        assertThat(crudService.getAmount().equals(0)).isTrue();
        assertThat(crudService.getAll()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("When call PUT_STORAGE contains empty array like {'array':[]} then custom exception is thrown")
    void whenExceptionIsThrown() {
        assertThatThrownBy(() -> mvc
                .perform(put(PUT_STORAGE).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING4)))
                .hasCause(new CustomException(CANNOT_BE_EMPTY, HttpStatus.BAD_REQUEST));
    }

    @Test
    @DisplayName("When call ADD_RETURN_STORAGE then storage has correct size(no changes) " +
            "& content(no changes) & response (storage + income)")
    void whenDataAddOk() throws Exception {
        crudService.addAll(setUpDefault());
        assertThat(crudService.getAmount().equals(4)).isTrue();

        this.mvc.perform(MockMvcRequestBuilders.post(ADD_RETURN_STORAGE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(PARAM_STRING3))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(PARAM_EXPECTED3));

        assertThat(crudService.getAmount().equals(4)).isTrue();
        assertThat(crudService.getAll()).isEqualTo(STORAGE_DEFAULT);
    }

    @Test
    @DisplayName("When call ADD_RETURN_STORAGE fails then storage must contain empty List<String>")
    void whenDataAddFails() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.post(ADD_RETURN_STORAGE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(BAD_PARAM_STRING2))
                .andExpect(status().isBadRequest())
                .andExpect(status().is4xxClientError());

        assertThat(crudService.getAmount().equals(0)).isTrue();
        assertThat(crudService.getAll()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("When call GET_STORAGE_SIZE then return correct array size of 4")
    void getAmountOfStrings() throws Exception {
        crudService.addAll(setUpDefault());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(GET_STORAGE_SIZE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("4"))
                .andReturn();

        Assertions.assertEquals(result.getResponse().getContentAsString(), "4");
    }

    private Storage setUpDefault(){
        Storage storage = new Storage();
        storage.setArray(STORAGE_DEFAULT);
        return storage;
    }

    @Configuration
    public static class TestConfig {

        @Bean
        public StringRepository stringRepository() {
            return new StringRepositoryImpl();
        }

        @Bean
        public CRUDService crudService() {
            return new CRUDServiceImpl(stringRepository());
        }

        @Bean
        public StringController stringController() {
            return new StringController(crudService());
        }
    }
}