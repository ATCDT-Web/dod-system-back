package com.example.database.parser;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParsedReport {
    private final Map<String, Map<String, Object>> sections = new LinkedHashMap<>();
    private final List<ParseError> errors = new LinkedList<>();
    private final Instant createdAt;
    private final String sourceFile;

    public ParsedReport(String sourceFile) {
        this.sourceFile = sourceFile;
        this.createdAt = Instant.now();
    }

    public void registerValue(String section, String field, Object value) {
        sections.computeIfAbsent(section, k -> new LinkedHashMap<>()).put(field, value);
    }

    public void addError(ParseError error) {
        errors.add(error);
    }

    public Map<String, Map<String, Object>> getSections() {
        return sections;
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getSourceFile() {
        return sourceFile;
    }
}
