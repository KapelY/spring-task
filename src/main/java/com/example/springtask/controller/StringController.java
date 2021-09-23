package com.example.springtask.controller;

import com.example.springtask.service.CRUDService;
import com.example.springtask.service.CRUDServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.springtask.service.CRUDServiceImpl.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping("/")
public class StringController {

    private final CRUDService crudService;

    @Autowired
    public StringController(CRUDService crudService) {
        this.crudService = crudService;
    }

    @PutMapping(path = "/append", consumes = APPLICATION_JSON_VALUE)
    public void appendData(@RequestBody Storage storage) {
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


