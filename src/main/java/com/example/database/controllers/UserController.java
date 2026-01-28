package com.example.database.controllers;

import com.example.database.dto.UpdateUserRequest;
import com.example.database.enteties.User;
import com.example.database.repositories.UserRepository;
import com.example.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("getAllUsers")
    List<User> getAllUsers(){
        return  repository.findAll();
    }

    @GetMapping("{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> existingUser = repository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(existingUser.get());
    }

    @PutMapping("update/{id}")
    ResponseEntity<User> updateUser(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody UpdateUserRequest request) {
        Optional<User> existingUser = repository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            Optional<User> userByEmail = repository.findByEmail(request.getEmail());
            if (userByEmail.isPresent() && !userByEmail.get().getId().equals(id)) {
                return ResponseEntity.badRequest().build();
            }
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getDistrict() != null && !request.getDistrict().isBlank()) {
            user.setDistrict(request.getDistrict());
        }

        if (request.getEducationalInstitution() != null && !request.getEducationalInstitution().isBlank()) {
            user.setEducationalInstitution(request.getEducationalInstitution());
        }

        if (request.getPosition() != null && !request.getPosition().isBlank()) {
            user.setPosition(request.getPosition());
        }

        if (request.getAdmin() != null) {
            user.setAdmin(request.getAdmin());
        }

        User saved = repository.save(user);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("delete/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = repository.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
