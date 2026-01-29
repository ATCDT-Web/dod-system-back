package com.example.database.config;

import com.example.database.enteties.User;
import com.example.database.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setAdmin(true);
            admin.setEmail("admin@school.ru");
            admin.setName("admin");
            admin.setPassword("$2a$10$iJQcUp9c1EpxL/568TLd/..LSOnwr1i0fUTCku9meBT5kRDIQ5NFm");
            userRepository.save(admin);

            System.out.println("Тестовый admin создан!");
        }
    }
}

