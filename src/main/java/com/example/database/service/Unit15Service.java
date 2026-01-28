package com.example.database.service;

import com.example.database.enteties.Unit15;
import com.example.database.repositories.Unit15Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit15Service {
    private final Unit15Repository unit15Repository;

    public Unit15Service(Unit15Repository unit15Repository) {
        this.unit15Repository = unit15Repository;
    }
    public Unit15 findById(Long id) {
        return unit15Repository.findById(id).get();
    }

    public Unit15 save(Unit15 unit15) {
        return unit15Repository.save(unit15);
    }
}
