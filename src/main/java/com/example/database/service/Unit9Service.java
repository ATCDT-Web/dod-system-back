package com.example.database.service;

import com.example.database.enteties.Unit9;
import com.example.database.repositories.Unit9Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit9Service {
    private final Unit9Repository unit9Repository;

    public Unit9Service(Unit9Repository unit9Repository) {
        this.unit9Repository = unit9Repository;
    }
    public Unit9 findById(Long id) {
        return unit9Repository.findById(id).get();
    }

    public Unit9 save(Unit9 unit9) {
        return unit9Repository.save(unit9);
    }

}
