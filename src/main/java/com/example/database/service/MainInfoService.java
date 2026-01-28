package com.example.database.service;

import com.example.database.enteties.MainInfo;
import com.example.database.repositories.MainInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MainInfoService {
    private final MainInfoRepository mainInfoRepository;

    public MainInfoService(MainInfoRepository mainInfoRepository) {
        this.mainInfoRepository = mainInfoRepository;
    }


    public void save(MainInfo mainInfo) {
        mainInfoRepository.save(mainInfo);
    }

    public MainInfo findById(Long id) {
        return mainInfoRepository.findById(id).get();
    }

    public Page<MainInfo> findAllProjected(Pageable pageable) {
        return mainInfoRepository.findAllProjected(pageable);
    }
}
