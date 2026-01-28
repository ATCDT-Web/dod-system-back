package com.example.database.parser;

import com.example.database.config.ExcelTemplateProperties;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class TemplateRegistry {

    private final Map<String, TemplateDefinition> definitions = new LinkedHashMap<>();

    @Autowired
    public TemplateRegistry(ExcelTemplateProperties properties, ResourceLoader resourceLoader) throws IOException {
        for (ExcelTemplateProperties.TemplateEntry entry : properties.getTemplates()) {
            Resource resource = resourceLoader.getResource("classpath:" + entry.getPath());
            if (!resource.exists()) {
                throw new IllegalStateException("Template not found: " + entry.getPath());
            }

            List<TemplateEntry> mapped = loadTemplate(resource);
            definitions.put(entry.getId(), new TemplateDefinition(entry.getId(), entry.getName(), entry.getDescription(), mapped));
        }
    }

    public TemplateDefinition getById(String id) {
        return definitions.get(id);
    }

    public Collection<TemplateDefinition> all() {
        return definitions.values();
    }

    private List<TemplateEntry> loadTemplate(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream(); Workbook wb = WorkbookFactory.create(is)) {
            Sheet sheet = wb.getSheetAt(0);
            Map<String, Integer> headerIndex = new HashMap<>();
            Row header = sheet.getRow(0);
            for (int i = 0; i < header.getLastCellNum(); i++) {
                if (header.getCell(i) != null) {
                    headerIndex.put(header.getCell(i).getStringCellValue().trim(), i);
                }
            }

            String[] required = {"Лист", "Раздел", "Ячейка", "Название поля"};
            for (String req : required) {
                if (!headerIndex.containsKey(req)) {
                    throw new IllegalStateException("Template missing column: " + req);
                }
            }

            List<TemplateEntry> entries = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String sheetRaw = getString(row, headerIndex.get("Лист"));
                String section = getString(row, headerIndex.get("Раздел"));
                String cell = getString(row, headerIndex.get("Ячейка"));
                String field = getString(row, headerIndex.get("Название поля"));
                if (cell == null || field == null) continue;
                Integer sheetNum = extractSheetNumber(sheetRaw);
                entries.add(new TemplateEntry(sheetRaw, sheetNum, section, cell, field));
            }
            return entries;
        }
    }

    private String getString(Row row, int idx) {
        if (row == null || idx < 0) return null;
        if (row.getCell(idx) == null) return null;
        String text = new DataFormatter().formatCellValue(row.getCell(idx));
        if (text == null) return null;
        text = text.trim();
        return text.isEmpty() ? null : text;
    }

    private Integer extractSheetNumber(String raw) {
        if (raw == null) return null;
        String digits = raw.replaceAll("\\D+", "");
        if (digits.isEmpty()) return null;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
