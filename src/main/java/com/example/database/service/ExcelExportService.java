package com.example.database.service;

import com.example.database.dto.Unit4Response;
import com.example.database.dto.Unit6Response;
import com.example.database.dto.Unit7Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

@Service
public class ExcelExportService {
    public ByteArrayInputStream exportUnit345SummaryToExcel(Object summary, int size) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Направления дополнительных общеобразовательных программ");
            for (int i = 1; i <= size; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(i+2);
            }

            String[] fields = {"technical", "naturalScience", "tourismAndLocalHistory",
                    "socialAndHumanitarian", "artisticOrientation",
                    "physicalEducationAndSports", "preprofessionalProgramsInTheFieldOfArts",
                    "additionalEducationalProgramsSportsTraining"};

            String[] rows = {"Техническое", "Естественнонаучное", "Туристско-краеведческое",
                    "Социально-гуманитарное", "Общеразвивающие программы художественной направленности",
                    "Общеразвивающие программы физкультурно-спортивной направленности",
                    "Предпрофессиональные программы в области искусств",
                    "Дополнительные образовательные программы спортивной подготовки"};

            int rowNum = 1;
            int len = fields.length;
            if(summary.getClass() == Unit4Response.class) len--;
            for (int k = 0; k < len; k++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rows[k]);

                Long[] array = getArrayFromSummary(summary, fields[k], size);
                for (int i = 0; i < size && i < array.length; i++) {
                    row.createCell(i + 1).setCellValue(array[i] != null ? array[i] : 0);
                }
            }

            for (int i = 0; i <= size; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания Excel", e);
        }
    }
    public ByteArrayInputStream exportUnit6SummaryToExcel(Unit6Response summary) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Наименование");
            headerRow.createCell(1).setCellValue("Численность обучающихся по дополнительным общеобразовательным программам");

            Row rowHiking = sheet.createRow(1);
            rowHiking.createCell(0).setCellValue("В походах");
            rowHiking.createCell(1).setCellValue(summary.getHiking());

            Row rowExcursions = sheet.createRow(2);
            rowExcursions.createCell(0).setCellValue("В экскурсиях");
            rowExcursions.createCell(1).setCellValue(summary.getExcursions());

            Row rowInFieldExpeditions = sheet.createRow(3);
            rowInFieldExpeditions.createCell(0).setCellValue("В экспедициях, проводимых в полевых условиях");
            rowInFieldExpeditions.createCell(1).setCellValue(summary.getInFieldExpeditions());


            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания Excel", e);
        }
    }

    public ByteArrayInputStream exportUnit78SummaryToExcel(Object summary) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Наименование показателей");
            for (int i = 1; i <= 11; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(i+2);
            }

            String[] fields = {"theNumberOfEmployeesIsTotal", "seniorStaffTotal", "seniorStaff",
                    "deputyHeads", "branchManager",
                    "teachingStaffTotal", "teachersOfAdditionalEducation",
                    "trainingAndSupportStaff", "otherStaff"};

            String[] rows = {"Численность работников - всего", "в том числе: руководящие работники - всего",
                    "из них: руководитель", "заместители руководителя", "руководитель филиала",
                    "педагогические работники, осуществляющие образовательную деятельность по дополнительным общеобразовательным программам для детей - всего",
                    "из них: педагоги дополнительного", "учебно-вспомогательный персонал", "иной персонал"};

            int rowNum = 1;
            int len = fields.length;

            for (int k = 0; k < len; k++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rows[k]);

                Long[] array = getArrayFromSummary(summary, fields[k], 11);
                for (int i = 0; i < 11 && i < array.length; i++) {
                    row.createCell(i + 1).setCellValue(array[i] != null ? array[i] : 0);
                }
            }
            if(summary.getClass() == Unit7Response.class){
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue("в течение последних трех лет повышение квалификации и (или) профессиональную переподготовку");
                row.createCell(1).setCellValue(((Unit7Response) summary).getProfessionalDevelopment());
            }

            for (int i = 0; i <= 11; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания Excel", e);
        }
    }
    private Long[] getArrayFromSummary(Object summary, String field, int size) {
        try {
            Class<?> summaryClass = summary.getClass();
            Method getter = summaryClass.getMethod("get" + capitalize(field));
            return (Long[]) getter.invoke(summary);
        } catch (Exception e) {
            return new Long[size];
        }
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }



}

