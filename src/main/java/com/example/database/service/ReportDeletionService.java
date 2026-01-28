package com.example.database.service;

import com.example.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportDeletionService {
    private final MainInfoRepository mainInfoRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final Unit1Repository unit1Repository;
    private final Unit2Repository unit2Repository;
    private final Unit3Repository unit3Repository;
    private final Unit4Repository unit4Repository;
    private final Unit5Repository unit5Repository;
    private final Unit6Repository unit6Repository;
    private final Unit7Repository unit7Repository;
    private final Unit8Repository unit8Repository;
    private final Unit9Repository unit9Repository;
    private final Unit10Repository unit10Repository;
    private final Unit11Repository unit11Repository;
    private final Unit12Repository unit12Repository;
    private final Unit13Repository unit13Repository;
    private final Unit14Repository unit14Repository;
    private final Unit15Repository unit15Repository;
    private final Unit16Repository unit16Repository;
    private final Unit17Repository unit17Repository;
    private final Unit18Repository unit18Repository;

    @Autowired
    public ReportDeletionService(
            MainInfoRepository mainInfoRepository,
            ContactInfoRepository contactInfoRepository,
            Unit1Repository unit1Repository,
            Unit2Repository unit2Repository,
            Unit3Repository unit3Repository,
            Unit4Repository unit4Repository,
            Unit5Repository unit5Repository,
            Unit6Repository unit6Repository,
            Unit7Repository unit7Repository,
            Unit8Repository unit8Repository,
            Unit9Repository unit9Repository,
            Unit10Repository unit10Repository,
            Unit11Repository unit11Repository,
            Unit12Repository unit12Repository,
            Unit13Repository unit13Repository,
            Unit14Repository unit14Repository,
            Unit15Repository unit15Repository,
            Unit16Repository unit16Repository,
            Unit17Repository unit17Repository,
            Unit18Repository unit18Repository
    ) {
        this.mainInfoRepository = mainInfoRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.unit1Repository = unit1Repository;
        this.unit2Repository = unit2Repository;
        this.unit3Repository = unit3Repository;
        this.unit4Repository = unit4Repository;
        this.unit5Repository = unit5Repository;
        this.unit6Repository = unit6Repository;
        this.unit7Repository = unit7Repository;
        this.unit8Repository = unit8Repository;
        this.unit9Repository = unit9Repository;
        this.unit10Repository = unit10Repository;
        this.unit11Repository = unit11Repository;
        this.unit12Repository = unit12Repository;
        this.unit13Repository = unit13Repository;
        this.unit14Repository = unit14Repository;
        this.unit15Repository = unit15Repository;
        this.unit16Repository = unit16Repository;
        this.unit17Repository = unit17Repository;
        this.unit18Repository = unit18Repository;
    }

    public void deleteReport(Long reportId) {
        safeDelete(unit18Repository, reportId);
        safeDelete(unit17Repository, reportId);
        safeDelete(unit16Repository, reportId);
        safeDelete(unit15Repository, reportId);
        safeDelete(unit14Repository, reportId);
        safeDelete(unit13Repository, reportId);
        safeDelete(unit12Repository, reportId);
        safeDelete(unit11Repository, reportId);
        safeDelete(unit10Repository, reportId);
        safeDelete(unit9Repository, reportId);
        safeDelete(unit8Repository, reportId);
        safeDelete(unit7Repository, reportId);
        safeDelete(unit6Repository, reportId);
        safeDelete(unit5Repository, reportId);
        safeDelete(unit4Repository, reportId);
        safeDelete(unit3Repository, reportId);
        safeDelete(unit2Repository, reportId);
        safeDelete(unit1Repository, reportId);
        safeDelete(contactInfoRepository, reportId);
        safeDelete(mainInfoRepository, reportId);
    }

    private <T extends org.springframework.data.repository.CrudRepository<?, Long>> void safeDelete(T repo, Long id) {
        try {
            repo.deleteById(id);
        } catch (Exception ignored) {
        }
    }
}
