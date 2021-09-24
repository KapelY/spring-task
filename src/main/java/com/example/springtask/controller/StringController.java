package com.example.springtask.controller;

import com.example.springtask.exception.CustomException;
import com.example.springtask.service.CRUDService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.springtask.service.CRUDServiceImpl.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController()
@RequestMapping("/")
public class StringController {

    final String EMPTY_ARRAY_INCOMING = "Empty array path=/append";

    private final CRUDService crudService;

    @Autowired
    public StringController(CRUDService crudService) {
        this.crudService = crudService;
    }

    @PutMapping(path = "/append", consumes = APPLICATION_JSON_VALUE)
    public void appendData(@RequestBody Storage storage) throws CustomException {

        if (storage.getArray().isEmpty()) {
            log.error(EMPTY_ARRAY_INCOMING);
            throw new CustomException("array cannot be empty", HttpStatus.BAD_REQUEST);
        }
            crudService.addAll(storage);

    }

    @PostMapping(path = "/add", consumes = APPLICATION_JSON_VALUE)
    public String getData(@RequestBody Storage storage) {
        return new Gson().toJson(crudService.addAndReturn(storage));
    }

    @GetMapping(path = "/amount")
    public String getAmountOfStrings() {
        return new Gson().toJson(crudService.getAmount());
    }
}


