package com.example.springtask.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class StringRepositoryImpl implements StringRepository {
    private final Queue<String> storage;

    @Autowired
    public StringRepositoryImpl() {
        this.storage = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addAll(List<String> list) {
        Optional.of(list).ifPresent(this.storage::addAll);
    }

    @Override
    public List<String> getAll() {
        return new ArrayList<>(storage);
    }
}
