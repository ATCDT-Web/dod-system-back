package com.example.database.service;

import com.example.database.enteties.Unit16;
import com.example.database.repositories.Unit16Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit16Service {
    private final Unit16Repository unit16Repository;

    public Unit16Service(Unit16Repository unit16Repository) {
        this.unit16Repository = unit16Repository;
    }
    public Unit16 findById(Long id) {
        return unit16Repository.findById(id).get();
    }

    public Unit16 save(Unit16 unit16) {
        return unit16Repository.save(unit16);
    }
}
