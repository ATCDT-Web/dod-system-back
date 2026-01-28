package com.example.database.parser;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExcelReportParser {

    public ParsedReport parse(Path filePath, TemplateDefinition template) throws IOException {
        try (InputStream is = java.nio.file.Files.newInputStream(filePath)) {
            return parse(is, filePath.getFileName().toString(), template);
        }
    }

    public ParsedReport parse(InputStream inputStream, String sourceName, TemplateDefinition template) throws IOException {
        ParsedReport report = new ParsedReport(sourceName);
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Map<Integer, String> sheetByNumber = mapSheets(workbook);
            for (TemplateEntry entry : template.getEntries()) {
                String sectionKey = normalizeSection(entry.getSection());
                String sheetName = resolveSheetName(workbook, sheetByNumber, entry);
                if (sheetName == null) {
                    report.addError(new ParseError(sectionKey, entry.getField(), entry.getCell(),
                            "Sheet not found"));
                    continue;
                }
                Sheet sheet = workbook.getSheet(sheetName);
                CellReferenceHelper.CellReference coords = CellReferenceHelper.parse(entry.getCell());
                if (coords == null) {
                    report.addError(new ParseError(sectionKey, entry.getField(), entry.getCell(),
                            "Invalid cell reference"));
                    continue;
                }
                Row row = sheet.getRow(coords.getRow());
                if (row == null) {
                    report.addError(new ParseError(sectionKey, entry.getField(), entry.getCell(),
                            "Row not found"));
                    continue;
                }
                Cell cell = row.getCell(coords.getCol());
                Object value = readCell(cell);
                report.registerValue(sectionKey, entry.getField(), value);
            }
        }
        return report;
    }

    private String normalizeSection(String raw) {
        if (raw == null) {
            return "unknown";
        }
        String text = raw.toString().trim();
        try {
            double value = Double.parseDouble(text.replace(",", "."));
            if (Math.floor(value) == value) {
                return new DecimalFormat("0").format(value);
            }
        } catch (NumberFormatException ignored) {
        }
        return text;
    }

    private Map<Integer, String> mapSheets(Workbook workbook) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            String name = workbook.getSheetName(i);
            Integer number = CellReferenceHelper.extractSheetNumber(name);
            if (number != null && !map.containsKey(number)) {
                map.put(number, name);
            }
        }
        return map;
    }

    private String resolveSheetName(Workbook workbook, Map<Integer, String> sheetByNumber, TemplateEntry entry) {
        if (entry.getSheetNum() != null && sheetByNumber.containsKey(entry.getSheetNum())) {
            return sheetByNumber.get(entry.getSheetNum());
        }
        if (entry.getSheetRaw() != null && workbook.getSheet(entry.getSheetRaw()) != null) {
            return entry.getSheetRaw();
        }
        return null;
    }

    private Object readCell(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) yield cell.getLocalDateTimeCellValue().toString();
                yield cell.getNumericCellValue();
            }
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> readCell(evaluateFormula(cell));
            default -> null;
        };
    }

    private Cell evaluateFormula(Cell cell) {
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        return evaluator.evaluateInCell(cell);
    }
}
