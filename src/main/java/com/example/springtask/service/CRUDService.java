package com.example.springtask.service;

import java.util.List;

import static com.example.springtask.service.CRUDServiceImpl.*;

public interface CRUDService {
    void addAll(Storage storage);

    List<String> getAll();

    Integer getAmount();

    Storage addAndReturn(Storage storage);
}
