package com.example.springtask.controller;

import com.example.springtask.exception.CustomException;
import com.example.springtask.service.CRUDService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.springtask.service.CRUDServiceImpl.Storage;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping
public class StringController {
    public final static String CANNOT_BE_EMPTY = "array cannot be empty";
    public final static String EMPTY_ARRAY_INCOMING = "Empty array path=/append";
    public final static String PUT_STORAGE = "/managed-strings";
    public final static String ADD_RETURN_STORAGE = "/managed-strings/storage";
    public final static String GET_STORAGE_SIZE = "/managed-strings/size";

    private final CRUDService crudService;

    @Autowired
    public StringController(CRUDService crudService) {
        this.crudService = crudService;
    }

    @PutMapping(path = PUT_STORAGE, consumes = APPLICATION_JSON_VALUE)
    public void appendData(@RequestBody Storage storage) {

        if (storage.getArray().isEmpty()) {
            log.error(EMPTY_ARRAY_INCOMING);
            throw new CustomException(CANNOT_BE_EMPTY, HttpStatus.BAD_REQUEST);
        }
            crudService.addAll(storage);
    }

    @PostMapping(path = ADD_RETURN_STORAGE, consumes = APPLICATION_JSON_VALUE)
    public Storage getData(@RequestBody Storage storage) {
        return crudService.addAndReturn(storage);
    }

    @GetMapping(path = GET_STORAGE_SIZE)
    public Integer getAmountOfStrings() {
        return crudService.getAmount();
    }
}


