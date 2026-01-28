package com.example.database.service;

import com.example.database.enteties.Unit17;
import com.example.database.repositories.Unit17Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit17Service {
    private final Unit17Repository unit17Repository;

    public Unit17Service(Unit17Repository unit17Repository) {
        this.unit17Repository = unit17Repository;
    }
    public Unit17 findById(Long id) {
        return unit17Repository.findById(id).get();
    }

    public Unit17 save(Unit17 unit17) {
        return unit17Repository.save(unit17);
    }
}
