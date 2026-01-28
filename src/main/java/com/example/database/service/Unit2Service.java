package com.example.database.service;

import com.example.database.enteties.Unit2;
import com.example.database.repositories.Unit2Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Unit2Service {
    private final Unit2Repository unit2Repository;

    public Unit2Service(Unit2Repository unit2Repository) {
        this.unit2Repository = unit2Repository;
    }

    public Unit2 findById(Long id) {
        return unit2Repository.findById(id).get();
    }

    public Unit2 save(Unit2 unit2) {
        return unit2Repository.save(unit2);
    }
}
