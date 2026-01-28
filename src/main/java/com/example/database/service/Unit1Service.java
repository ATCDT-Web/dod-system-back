package com.example.database.service;

import com.example.database.dto.Unit1Response;
import com.example.database.enteties.Unit1;
import com.example.database.repositories.Unit1Repository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Unit1Service {
    private final Unit1Repository unit1Repository;

    public Unit1Service(Unit1Repository unit1Repository) {
        this.unit1Repository = unit1Repository;
    }

    public Unit1 save(Unit1 unit1) {
        return unit1Repository.save(unit1);
    }

    public Unit1 findById(Long id) {
        return unit1Repository.findById(id).get();
    }

    public Unit1Response sumAllByOrganizationName(String organizationName) {
        return unit1Repository.sumAllByOrganizationName(organizationName);
    }
}
