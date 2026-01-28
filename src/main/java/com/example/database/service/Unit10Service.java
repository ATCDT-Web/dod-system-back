package com.example.database.service;

import com.example.database.enteties.Unit10;
import com.example.database.repositories.Unit10Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit10Service {
    private final Unit10Repository unit10Repository;

    public Unit10Service(Unit10Repository unit10Repository) {
        this.unit10Repository = unit10Repository;
    }
    public Unit10 findById(Long id) {
        return unit10Repository.findById(id).get();
    }

    public Unit10 save(Unit10 unit10) {
        return unit10Repository.save(unit10);
    }
}
