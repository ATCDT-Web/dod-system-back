package com.example.database.parser;

import java.util.Objects;

public class TemplateEntry {
    private final String sheetRaw;
    private final Integer sheetNum;
    private final String section;
    private final String cell;
    private final String field;

    public TemplateEntry(String sheetRaw, Integer sheetNum, String section, String cell, String field) {
        this.sheetRaw = sheetRaw;
        this.sheetNum = sheetNum;
        this.section = section;
        this.cell = cell;
        this.field = field;
    }

    public String getSheetRaw() {
        return sheetRaw;
    }

    public Integer getSheetNum() {
        return sheetNum;
    }

    public String getSection() {
        return section;
    }

    public String getCell() {
        return cell;
    }

    public String getField() {
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateEntry that = (TemplateEntry) o;
        return Objects.equals(sheetRaw, that.sheetRaw) &&
                Objects.equals(sheetNum, that.sheetNum) &&
                Objects.equals(section, that.section) &&
                Objects.equals(cell, that.cell) &&
                Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetRaw, sheetNum, section, cell, field);
    }
}
