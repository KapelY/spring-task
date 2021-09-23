package com.example.springtask.service;

import com.example.springtask.repositories.StringRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CRUDServiceImpl implements CRUDService{

    private final StringRepository stringRepository;

    @Autowired
    public CRUDServiceImpl(StringRepository stringRepository) {
        this.stringRepository = stringRepository;
    }

    @Override
    public void addAll(Storage storage) {
        stringRepository.addAll(storage.getArray());
    }

    @Override
    public List<String> getAll() {
        return stringRepository.getAll();
    }

    @Override
    public Integer getAmount() {
        return getAll().size();
    }

    @Override
    public Storage addAndReturn(Storage storage) {
        Storage result = new Storage();
        result.setArray(getAll());
        result.getArray().addAll(storage.getArray());
        return result;
    }

    @Data
    public static class Storage {
        private List<String> array = new LinkedList<>();
    }
}
