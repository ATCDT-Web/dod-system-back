package com.example.database.controllers;

import com.example.database.dto.CreateInstitutionRequest;
import com.example.database.dto.CreateReportRequest;
import com.example.database.dto.ReferenceResponse;
import com.example.database.enteties.MainInfo;
import com.example.database.enteties.User;
import com.example.database.repositories.UserRepository;
import com.example.database.service.AuthenticationService;
import com.example.database.service.ReportCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportCreationService reportCreationService;

    @PostMapping("/institutions")
    public ResponseEntity<User> createInstitution(@RequestBody CreateInstitutionRequest request) {
        if (request.getInstitutionName() == null || request.getEmail() == null || request.getAdminName() == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setName(request.getAdminName());
        user.setEmail(request.getEmail());
        user.setPassword(UUID.randomUUID().toString());
        user.setDistrict(request.getDistrict());
        user.setEducationalInstitution(request.getInstitutionName());
        user.setPosition(request.getPosition());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAdmin(false);

        User created = authenticationService.registerUser(user);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/reports")
    public ResponseEntity<ReferenceResponse> createReport(@RequestBody CreateReportRequest request) {
        if (request.getUserId() == null || request.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.getEducationalInstitution() == null) {
            throw new RuntimeException("У пользователя не привязано образовательное учреждение");
        }

        MainInfo mainInfo = new MainInfo();
        mainInfo.setOrganizationName(user.getEducationalInstitution());
        mainInfo.setReportTitle(request.getTitle());
        mainInfo.setStatus("Новая");

        MainInfo created = reportCreationService.createEmptyReport(mainInfo);
        return ResponseEntity.ok(new ReferenceResponse(created.getId()));
    }
}
