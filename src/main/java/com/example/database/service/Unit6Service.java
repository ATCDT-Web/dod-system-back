package com.example.database.service;

import com.example.database.dto.Unit6Response;
import com.example.database.enteties.Unit6;
import com.example.database.repositories.Unit6Repository;
import org.springframework.stereotype.Service;

@Service
public class Unit6Service {
    private final Unit6Repository unit6Repository;

    public Unit6Service(Unit6Repository unit6Repository) {
        this.unit6Repository = unit6Repository;
    }

    public void save(Unit6 unit6){
        unit6Repository.save(unit6);
    }

    public Unit6 findById(Long id) {
        return unit6Repository.findById(id).get();
    }
    public Unit6Response sumAllByOrganizationName(String organizationName){
        return unit6Repository.sumAllByOrganizationName(organizationName);
    }
    public Unit6Response sumAllByOrganizations(java.util.List<String> organizationNames) {
        long hiking = 0;
        long excursions = 0;
        long inFieldExpeditions = 0;

        for (String name : organizationNames) {
            Unit6Response response = unit6Repository.sumAllByOrganizationName(name);
            if (response != null) {
                hiking += response.getHiking() != null ? response.getHiking() : 0;
                excursions += response.getExcursions() != null ? response.getExcursions() : 0;
                inFieldExpeditions += response.getInFieldExpeditions() != null ? response.getInFieldExpeditions() : 0;
            }
        }

        return new Unit6Response(hiking, excursions, inFieldExpeditions);
    }

}
