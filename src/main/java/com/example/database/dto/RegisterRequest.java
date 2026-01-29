package com.example.database.dto;

import jakarta.persistence.Column;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String district;
    private String educationalInstitution;
    private String position;
    private String phone;
    private String address;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String password, String district, String educationalInstitution, String position, String phone, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.district = district;
        this.educationalInstitution = educationalInstitution;
        this.position = position;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEducationalInstitution() {
        return educationalInstitution;
    }

    public void setEducationalInstitution(String educationalInstitution) {
        this.educationalInstitution = educationalInstitution;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
