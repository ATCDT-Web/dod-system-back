package com.example.database.service;

import com.example.database.enteties.Unit13;
import com.example.database.repositories.Unit13Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit13Service {
    private final Unit13Repository unit13Repository;

    public Unit13Service(Unit13Repository unit13Repository) {
        this.unit13Repository = unit13Repository;
    }
    public Unit13 findById(Long id) {
        return unit13Repository.findById(id).get();
    }

    public Unit13 save(Unit13 unit13) {
        return unit13Repository.save(unit13);
    }
}
