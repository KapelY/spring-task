package com.example.springtask.repositories;

import java.util.List;

public interface StringRepository {

    void addAll(List<String> list);

    List<String> getAll();

    Integer size();

    void clear();
}
