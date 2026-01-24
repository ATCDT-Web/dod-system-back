package com.example.database.dto;

public class UpdateUserRequest {
    private String name;
    private String email;
    private String password;
    private String district;
    private String educationalInstitution;
    private String position;
    private Boolean admin;

    public UpdateUserRequest() {
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
