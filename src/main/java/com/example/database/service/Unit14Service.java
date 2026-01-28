package com.example.database.service;

import com.example.database.enteties.Unit14;
import com.example.database.repositories.Unit14Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit14Service {
    private final Unit14Repository unit14Repository;

    public Unit14Service(Unit14Repository unit14Repository) {
        this.unit14Repository = unit14Repository;
    }
    public Unit14 findById(Long id) {
        return unit14Repository.findById(id).get();
    }

    public Unit14 save(Unit14 unit14) {
        return unit14Repository.save(unit14);
    }
}
