package com.example.database.dto;

import com.example.database.parser.ParseError;

import java.util.ArrayList;
import java.util.List;

public class ImportExcelResponse {
    private final List<String> updatedSections = new ArrayList<>();
    private final List<ParseError> errors = new ArrayList<>();

    public List<String> getUpdatedSections() {
        return updatedSections;
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public void addUpdatedSection(String section) {
        if (!updatedSections.contains(section)) {
            updatedSections.add(section);
        }
    }

    public void addError(ParseError error) {
        errors.add(error);
    }
}
