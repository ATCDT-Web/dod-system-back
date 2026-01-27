package com.example.database.service;

import com.example.database.dto.ReferenceResponse;
import com.example.database.enteties.*;
import com.example.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class InitService {
    @Autowired private MainInfoRepository mainInfoRepository;
    @Autowired private ContactInfoRepository contactInfoRepository;
    @Autowired
    private Unit1Repository unit1Repository;
    @Autowired
    private Unit2Repository unit2Repository;
    @Autowired
    private Unit3Repository unit3Repository;
    @Autowired
    private Unit4Repository unit4Repository;
    @Autowired
    private Unit5Repository unit5Repository;
    @Autowired
    private Unit6Repository unit6Repository;
    @Autowired
    private Unit7Repository unit7Repository;
    @Autowired
    private Unit8Repository unit8Repository;
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

    public ResponseEntity<ReferenceResponse> initReference( MainInfo mainInfo) {
        if (mainInfo.getStatus() == null || mainInfo.getStatus().isEmpty()) {
            mainInfo.setStatus("Новая");
        }
        mainInfoRepository.save(mainInfo);
        contactInfoRepository.save(new ContactInformation(mainInfo.getId()));

        unit1Repository.save(new Unit1(mainInfo.getId()));
        unit2Repository.save(new Unit2(mainInfo.getId()));
        unit3Repository.save(new Unit3(mainInfo.getId()));
        unit4Repository.save(new Unit4(mainInfo.getId()));
        unit5Repository.save(new Unit5(mainInfo.getId()));
        unit6Repository.save(new Unit6(mainInfo.getId()));
        unit7Repository.save(new Unit7(mainInfo.getId()));
        unit8Repository.save(new Unit8(mainInfo.getId()));
        unit9Repository.save(new Unit9(mainInfo.getId()));
        unit10Repository.save(new Unit10(mainInfo.getId()));
        unit11Repository.save(new Unit11(mainInfo.getId()));
        unit12Repository.save(new Unit12(mainInfo.getId()));
        unit13Repository.save(new Unit13(mainInfo.getId()));
        unit14Repository.save(new Unit14(mainInfo.getId()));
        unit15Repository.save(new Unit15(mainInfo.getId()));
        unit16Repository.save(new Unit16(mainInfo.getId()));
        unit17Repository.save(new Unit17(mainInfo.getId()));
        unit18Repository.save(new Unit18(mainInfo.getId()));
        return ResponseEntity.ok(new ReferenceResponse(mainInfo.getId()));
    }
}
