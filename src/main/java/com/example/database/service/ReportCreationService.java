package com.example.database.service;

import com.example.database.enteties.*;
import com.example.database.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReportCreationService {

    @Autowired
    private MainInfoRepository mainInfoRepository;

    @Autowired
    private ContactInfoRepository contactInfoRepository;

    @Autowired
    private Unit1Repository unit1Repository;

    @Autowired
    private Unit2Repository unit2Repository;

    @Autowired
    private Unit3Service unit3Service;

    @Autowired
    private Unit4Service unit4Service;

    @Autowired
    private Unit5Service unit5Service;

    @Autowired
    private Unit6Service unit6Service;

    @Autowired
    private Unit7Service unit7Service;

    @Autowired
    private Unit8Service unit8Service;

    @Autowired
    private Unit9Repository unit9Repository;

    @Autowired
    private Unit10Repository unit10Repository;

    @Autowired
    private Unit11Repository unit11Repository;

    @Autowired
    private Unit12Repository unit12Repository;

    @Autowired
    private Unit13Repository unit13Repository;

    @Autowired
    private Unit14Repository unit14Repository;

    @Autowired
    private Unit15Repository unit15Repository;

    @Autowired
    private Unit16Repository unit16Repository;

    @Autowired
    private Unit17Repository unit17Repository;

    @Autowired
    private Unit18Repository unit18Repository;

    public MainInfo createEmptyReport(MainInfo mainInfo) {
        if (mainInfo.getStatus() == null || mainInfo.getStatus().isEmpty()) {
            mainInfo.setStatus("Новая");
        }
        MainInfo saved = mainInfoRepository.save(mainInfo);

        contactInfoRepository.save(new ContactInformation(saved.getId()));

        unit1Repository.save(new Unit1(saved.getId()));
        unit2Repository.save(new Unit2(saved.getId()));
        unit3Service.save(new Unit3(saved.getId()));
        unit4Service.save(new Unit4(saved.getId()));
        unit5Service.save(new Unit5(saved.getId()));
        unit6Service.save(new Unit6(saved.getId()));
        unit7Service.save(new Unit7(saved.getId()));
        unit8Service.save(new Unit8(saved.getId()));
        unit9Repository.save(new Unit9(saved.getId()));
        unit10Repository.save(new Unit10(saved.getId()));
        unit11Repository.save(new Unit11(saved.getId()));
        unit12Repository.save(new Unit12(saved.getId()));
        unit13Repository.save(new Unit13(saved.getId()));
        unit14Repository.save(new Unit14(saved.getId()));
        unit15Repository.save(new Unit15(saved.getId()));
        unit16Repository.save(new Unit16(saved.getId()));
        unit17Repository.save(new Unit17(saved.getId()));
        unit18Repository.save(new Unit18(saved.getId()));

        return saved;
    }
}
