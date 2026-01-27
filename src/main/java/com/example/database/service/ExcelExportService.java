package com.example.database.service;

import com.example.database.common.HeaderType;
import com.example.database.dto.Unit4Response;
import com.example.database.dto.Unit6Response;
import com.example.database.dto.Unit7Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

@Service
public class ExcelExportService {
    public ByteArrayInputStream exportUnit345SummaryToExcel(Object summary, int size, int num, HeaderType type) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");
            int startRow = 1;
            Row headerRow = sheet.createRow(0);
            switch (type) {
                case UNIT3 -> {
                    startRow = 4;
                    headerUnit3(sheet, headerRow);
                }
                case UNIT4 -> {
                    startRow = 2;
                    headerUnit4(sheet,headerRow);
                }
                case UNIT5 -> {
                    startRow = 2;
                    headerUnit5(sheet,headerRow);
                }
            }


            headerRow.createCell(0).setCellValue("Направления дополнительных общеобразовательных программ");
            sheet.addMergedRegion(new CellRangeAddress(0, startRow - 1, 0, 0));
            headerRow.createCell(1).setCellValue("№ строки");
            sheet.addMergedRegion(new CellRangeAddress(0, startRow - 1, 1, 1));


            Row firstRow = sheet.createRow(startRow);
            firstRow.createCell(0).setCellValue("A");
            firstRow.createCell(1).setCellValue("Б");

            for (int i = 2; i <= size + 1; i++) {
                Cell cell = firstRow.createCell(i);
                cell.setCellValue(i + 1);
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

            int rowNum = startRow + 1;
            int len = fields.length;
            if (summary.getClass() == Unit4Response.class) len--;
            for (int k = 0; k < len; k++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rows[k]);
                row.createCell(1).setCellValue(num++);
                Long[] array = getArrayFromSummary(summary, fields[k], size);

                for (int i = 0; i < size && i < array.length; i++) {
                    row.createCell(i + 2).setCellValue(array[i] != null ? array[i] : 0);
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

    private void headerUnit3(Sheet sheet, Row headerRow) {
        headerRow.createCell(2).setCellValue("Численность обучающихся");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));

        headerRow.createCell(6).setCellValue("Лица с ограниченными возможностями здоровья");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));

        headerRow.createCell(10).setCellValue("Из гр. 7 - дети-инвалиды");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 13));

        headerRow.createCell(14).setCellValue("Дети-инвалиды (кроме учтенных в гр. 11)");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 17));

        Row secondRow = sheet.createRow(1);
        secondRow.createCell(2).setCellValue("всего");
        sheet.addMergedRegion(new CellRangeAddress(1, 3, 2, 2));
        secondRow.createCell(3).setCellValue("(из гр. 3)");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));

        secondRow.createCell(6).setCellValue("Всего \n" +
                "(из гр. \n" +
                "3)");
        sheet.addMergedRegion(new CellRangeAddress(1, 3, 6, 6));
        secondRow.createCell(7).setCellValue("(из гр. 7)");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 9));

        secondRow.createCell(10).setCellValue("Всего");
        sheet.addMergedRegion(new CellRangeAddress(1, 3, 10, 10));
        secondRow.createCell(11).setCellValue("(из гр. 11)");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 11, 13));

        secondRow.createCell(14).setCellValue("Всего \n" +
                "(из гр. \n" +
                "3)");
        sheet.addMergedRegion(new CellRangeAddress(1, 3, 14, 14));
        secondRow.createCell(15).setCellValue("(из гр. 15)");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 15, 17));

        Row thirdRow = sheet.createRow(2);
        Row fourthRow = sheet.createRow(3);
        for (int i = 0; i < 4; i++) {
            thirdRow.createCell(3 + 4 * i).setCellValue("девочки");
            thirdRow.createCell(4 + 4 * i).setCellValue("обучались");
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 4 + i * 4, 5 + i * 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 3, 3 + i * 4, 3 + i * 4));

            fourthRow.createCell(4 + 4 * i).setCellValue("в сетевой форме обучения");
            fourthRow.createCell(5 + 4 * i).setCellValue("с\n применением\nэлектронного обучения \nи дистан-ционных образова-тельных \n технологий");
        }
    }
    private void headerUnit4(Sheet sheet, Row headerRow) {
        headerRow.createCell(2).setCellValue("Всего (сумма граф 4 - 19)");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        headerRow.createCell(3).setCellValue("Число полных лет обучающихся по состоянию на 1 января года, следующего за отчетным");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 18));

        Row secondRow = sheet.createRow(1);
        secondRow.createCell(3).setCellValue("до 3 лет");
        secondRow.createCell(4).setCellValue("3 года");
        secondRow.createCell(5).setCellValue("4 года");

        for (int i=5;i<=17;i++){
            secondRow.createCell(i+1).setCellValue(i+" лет");
        }

    }
    private void headerUnit5(Sheet sheet, Row headerRow) {
        headerRow.createCell(2).setCellValue("Обучались за счет бюджетных ассигнований");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));

        headerRow.createCell(5).setCellValue("Обучались только по\n" +
                "договорам \n" +
                "об оказании платных образовательных\n" +
                "услуг");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));

        headerRow.createCell(6).setCellValue("Обучались за счет бюджетных \n" +
                "ассигнований\n" +
                "и по договорам \n" +
                "об оказании платных образовательных услуг");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));

        Row secondRow = sheet.createRow(1);
        secondRow.createCell(2).setCellValue("федерального бюджета");
        secondRow.createCell(3).setCellValue("бюджета субъекта Российской Федерации");
        secondRow.createCell(4).setCellValue("местного бюджета");
    }

    public ByteArrayInputStream exportUnit6SummaryToExcel(Unit6Response summary) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Наименование");
            headerRow.createCell(1).setCellValue("№ строки");
            headerRow.createCell(2).setCellValue("Численность обучающихся по дополнительным общеобразовательным программам");

            Row rowFirst = sheet.createRow(1);
            rowFirst.createCell(0).setCellValue("A");
            rowFirst.createCell(1).setCellValue("Б");
            rowFirst.createCell(2).setCellValue("3");

            Row rowHiking = sheet.createRow(2);
            rowHiking.createCell(0).setCellValue("В походах");
            rowHiking.createCell(1).setCellValue("31");
            rowHiking.createCell(2).setCellValue(summary.getHiking());

            Row rowExcursions = sheet.createRow(3);
            rowExcursions.createCell(0).setCellValue("В экскурсиях");
            rowExcursions.createCell(1).setCellValue("32");
            rowExcursions.createCell(2).setCellValue(summary.getExcursions());

            Row rowInFieldExpeditions = sheet.createRow(4);
            rowInFieldExpeditions.createCell(0).setCellValue("В экспедициях, проводимых в полевых условиях");
            rowInFieldExpeditions.createCell(1).setCellValue("33");
            rowInFieldExpeditions.createCell(2).setCellValue(summary.getInFieldExpeditions());

            for (int i = 0; i <= 2; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания Excel", e);
        }
    }

    public ByteArrayInputStream exportUnit78SummaryToExcel(Object summary, int num) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");

            Row headerRow = sheet.createRow(0);
            if(summary.getClass() == Unit7Response.class){
                headerUnit7(sheet,headerRow);
            }else{
                headerUnit8(sheet,headerRow);
            }

            headerRow.createCell(0).setCellValue("Наименование показателей");
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            headerRow.createCell(1).setCellValue("№ строки");
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));


            Row firstRow = sheet.createRow(2);
            firstRow.createCell(0).setCellValue("A");
            firstRow.createCell(1).setCellValue("Б");

            for (int i = 2; i <= 11; i++) {
                Cell cell = firstRow.createCell(i);
                cell.setCellValue(i + 1);
            }

            String[] fields = {"theNumberOfEmployeesIsTotal", "seniorStaffTotal", "seniorStaff",
                    "deputyHeads", "branchManager",
                    "teachingStaffTotal", "teachersOfAdditionalEducation",
                    "trainingAndSupportStaff", "otherStaff"};

            String[] rows = {"Численность работников - всего", "в том числе: руководящие работники - всего",
                    "из них: руководитель", "заместители руководителя", "руководитель филиала",
                    "педагогические работники, осуществляющие образовательную деятельность по дополнительным общеобразовательным программам для детей - всего",
                    "из них: педагоги дополнительного", "учебно-вспомогательный персонал", "иной персонал"};

            int rowNum = 2;
            int len = fields.length;

            for (int k = 0; k < len; k++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rows[k]);
                row.createCell(1).setCellValue(num++);

                Long[] array = getArrayFromSummary(summary, fields[k], 11);
                for (int i = 0; i < 11 && i < array.length; i++) {
                    row.createCell(i + 2).setCellValue(array[i] != null ? array[i] : 0);

                }
            }
            if (summary.getClass() == Unit7Response.class) {
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
    private Sheet headerUnit7(Sheet sheet, Row headerRow) {
        headerRow.createCell(2).setCellValue("Всего\n" +
                "работников\n" +
                "списочного состава");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        headerRow.createCell(3).setCellValue("из них (из гр. 3) имеют образование:");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 6));

        headerRow.createCell(7).setCellValue("из них  (из гр. 3) обучаю-щиеся по образова-тельным программам высшего образова-ния");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));

        headerRow.createCell(8).setCellValue("из них (из гр. 3) имеют квалификационные категории");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));

        headerRow.createCell(10).setCellValue("из них (из гр. 3) женщины");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));

        headerRow.createCell(11).setCellValue("Кроме того, численность внешних совместителей");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));

        headerRow.createCell(12).setCellValue("Число вакансий");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 12, 12));

        Row secondRow = sheet.createRow(1);
        secondRow.createCell(3).setCellValue("высшее");
        secondRow.createCell(4).setCellValue("из них (из гр. 4) педагоги-ческое");
        secondRow.createCell(5).setCellValue("среднее профессио-нальное образование по программам подготовки специалистов среднего звена");
        secondRow.createCell(6).setCellValue("из них (из гр. 6) педагогическое");

        secondRow.createCell(8).setCellValue("высшую");
        secondRow.createCell(9).setCellValue("первую");

        return sheet;
    }
    private Sheet headerUnit8(Sheet sheet, Row headerRow) {
        headerRow.createCell(2).setCellValue("Всего (сумма граф 4 - 13)");
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        headerRow.createCell(3).setCellValue("Число полных лет по состоянию на 1 января следующего за отчетным года");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 12));

        Row secondRow = sheet.createRow(1);
        secondRow.createCell(3).setCellValue("моложе 25");
        secondRow.createCell(12).setCellValue("65 и старше");

        int year = 25;
        for (int i=4;i<=11;i++){
            secondRow.createCell(i).setCellValue(year+" - "+year+4);
            year+=5;
        }

        return sheet;
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

