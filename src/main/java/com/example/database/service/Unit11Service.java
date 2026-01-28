package com.example.database.service;

import com.example.database.enteties.Unit11;
import com.example.database.repositories.Unit11Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit11Service {
    private final Unit11Repository unit11Repository;

    public Unit11Service(Unit11Repository unit11Repository) {
        this.unit11Repository = unit11Repository;
    }
    public Unit11 findById(Long id) {
        return unit11Repository.findById(id).get();
    }

    public Unit11 save(Unit11 unit11) {
        return unit11Repository.save(unit11);
    }
}
