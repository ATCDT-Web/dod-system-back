package com.example.database.parser;

public class ParseError {
    private final String section;
    private final String field;
    private final String cell;
    private final String message;

    public ParseError(String section, String field, String cell, String message) {
        this.section = section;
        this.field = field;
        this.cell = cell;
        this.message = message;
    }

    public String getSection() {
        return section;
    }

    public String getField() {
        return field;
    }

    public String getCell() {
        return cell;
    }

    public String getMessage() {
        return message;
    }
}
