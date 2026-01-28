package com.example.database.service;

import com.example.database.enteties.Unit18;
import com.example.database.repositories.Unit18Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit18Service {
    private final Unit18Repository unit18Repository;

    public Unit18Service(Unit18Repository unit18Repository) {
        this.unit18Repository = unit18Repository;
    }
    public Unit18 findById(Long id) {
        return unit18Repository.findById(id).get();
    }

    public Unit18 save(Unit18 unit18) {
        return unit18Repository.save(unit18);
    }
}
