package com.example.springtask.repositories;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class StringRepositoryImpl implements StringRepository {
    private final Queue<String> storage;

    public StringRepositoryImpl() {
        this.storage = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addAll(List<String> list) {
        storage.addAll(list);
    }

    @Override
    public List<String> getAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public Integer size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
