package com.example.database.service;

import com.example.database.enteties.ContactInformation;
import com.example.database.repositories.ContactInfoRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;

    public ContactInfoService(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }


    public  ContactInformation findById(Long id) {
        return contactInfoRepository.findById(id).get();
    }

    public ContactInformation save(ContactInformation contactInformation) {
        return contactInfoRepository.save(contactInformation);
    }
}
