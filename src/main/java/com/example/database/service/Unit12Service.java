package com.example.database.service;

import com.example.database.enteties.Unit12;
import com.example.database.repositories.Unit12Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit12Service {
    private final Unit12Repository unit12Repository;

    public Unit12Service(Unit12Repository unit12Repository) {
        this.unit12Repository = unit12Repository;
    }
    public Unit12 findById(Long id) {
        return unit12Repository.findById(id).get();
    }

    public Unit12 save(Unit12 unit12) {
        return unit12Repository.save(unit12);
    }
}
