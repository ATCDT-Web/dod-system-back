package com.example.database.service;

import com.example.database.dto.ImportExcelResponse;
import com.example.database.enteties.*;
import com.example.database.parser.*;
import com.example.database.repositories.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ExcelImportService {
    private static final Pattern SECTION_PATTERN = Pattern.compile("Раздел\\s+(\\d+)", Pattern.CASE_INSENSITIVE);
    private static final DataFormatter FORMATTER = new DataFormatter();

    private final MainInfoRepository mainInfoRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final Unit1Repository unit1Repository;
    private final Unit2Repository unit2Repository;
    private final Unit3Repository unit3Repository;
    private final Unit4Repository unit4Repository;
    private final Unit5Repository unit5Repository;
    private final Unit6Repository unit6Repository;
    private final Unit7Repository unit7Repository;
    private final Unit8Repository unit8Repository;
    private final Unit9Repository unit9Repository;
    private final Unit10Repository unit10Repository;
    private final Unit11Repository unit11Repository;
    private final Unit12Repository unit12Repository;
    private final Unit13Repository unit13Repository;
    private final Unit14Repository unit14Repository;
    private final Unit15Repository unit15Repository;
    private final Unit16Repository unit16Repository;
    private final Unit17Repository unit17Repository;
    private final Unit18Repository unit18Repository;
    private final TemplateRegistry templateRegistry;
    private final ExcelReportParser excelReportParser;
    private final LabelOrderService labelOrderService;

    @Autowired
    public ExcelImportService(
            MainInfoRepository mainInfoRepository,
            ContactInfoRepository contactInfoRepository,
            Unit1Repository unit1Repository,
            Unit2Repository unit2Repository,
            Unit3Repository unit3Repository,
            Unit4Repository unit4Repository,
            Unit5Repository unit5Repository,
            Unit6Repository unit6Repository,
            Unit7Repository unit7Repository,
            Unit8Repository unit8Repository,
            Unit9Repository unit9Repository,
            Unit10Repository unit10Repository,
            Unit11Repository unit11Repository,
            Unit12Repository unit12Repository,
            Unit13Repository unit13Repository,
            Unit14Repository unit14Repository,
            Unit15Repository unit15Repository,
            Unit16Repository unit16Repository,
            Unit17Repository unit17Repository,
            Unit18Repository unit18Repository,
            TemplateRegistry templateRegistry,
            ExcelReportParser excelReportParser,
            LabelOrderService labelOrderService
    ) {
        this.mainInfoRepository = mainInfoRepository;
        this.contactInfoRepository = contactInfoRepository;
        this.unit1Repository = unit1Repository;
        this.unit2Repository = unit2Repository;
        this.unit3Repository = unit3Repository;
        this.unit4Repository = unit4Repository;
        this.unit5Repository = unit5Repository;
        this.unit6Repository = unit6Repository;
        this.unit7Repository = unit7Repository;
        this.unit8Repository = unit8Repository;
        this.unit9Repository = unit9Repository;
        this.unit10Repository = unit10Repository;
        this.unit11Repository = unit11Repository;
        this.unit12Repository = unit12Repository;
        this.unit13Repository = unit13Repository;
        this.unit14Repository = unit14Repository;
        this.unit15Repository = unit15Repository;
        this.unit16Repository = unit16Repository;
        this.unit17Repository = unit17Repository;
        this.unit18Repository = unit18Repository;
        this.templateRegistry = templateRegistry;
        this.excelReportParser = excelReportParser;
        this.labelOrderService = labelOrderService;
    }

    public ImportExcelResponse importExcel(Long reportId, List<MultipartFile> files, String mode) {
        ImportExcelResponse response = new ImportExcelResponse();
        if (files == null || files.isEmpty()) {
            response.addError(new ParseError("file", "files", "-", "Не переданы файлы для импорта"));
            return response;
        }

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                response.addError(new ParseError("file", "files", "-", "Файл пустой"));
                continue;
            }
            if ("full".equalsIgnoreCase(mode)) {
                importFull(reportId, file, response);
            } else {
                try (InputStream inputStream = file.getInputStream();
                     Workbook workbook = WorkbookFactory.create(inputStream)) {
                    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                        Sheet sheet = workbook.getSheetAt(i);
                        parseSheet(reportId, sheet, response);
                    }
                } catch (Exception ex) {
                    response.addError(new ParseError("file", file.getOriginalFilename(), "-", "Ошибка чтения файла: " + ex.getMessage()));
                }
            }
        }

        return response;
    }

    private void parseSheet(Long reportId, Sheet sheet, ImportExcelResponse response) {
        List<SectionHeader> headers = findSectionHeaders(sheet);
        if (headers.isEmpty()) {
            response.addError(new ParseError("sheet", sheet.getSheetName(), "-", "Не удалось определить раздел по заголовку"));
            return;
        }

        headers.sort(Comparator.comparingInt(h -> h.rowIndex));
        for (int i = 0; i < headers.size(); i++) {
            SectionHeader current = headers.get(i);
            int startRow = current.rowIndex;
            int endRow = (i + 1 < headers.size()) ? headers.get(i + 1).rowIndex - 1 : sheet.getLastRowNum();
            switch (current.sectionNumber) {
                case 4 -> parseSection4(reportId, sheet, startRow, endRow, response);
                case 5 -> parseSection5(reportId, sheet, startRow, endRow, response);
                case 6 -> parseSection6(reportId, sheet, startRow, endRow, response);
                case 7 -> parseSection7(reportId, sheet, startRow, endRow, response);
                case 12 -> parseSection12(reportId, sheet, startRow, endRow, response);
                default -> response.addError(new ParseError("section", String.valueOf(current.sectionNumber),
                        sheet.getSheetName(), "Раздел не поддерживается для импорта"));
            }
        }
    }

    private List<SectionHeader> findSectionHeaders(Sheet sheet) {
        List<SectionHeader> headers = new ArrayList<>();
        for (int r = 0; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            for (int c = 0; c < row.getLastCellNum(); c++) {
                String text = readText(row.getCell(c));
                if (text == null) continue;
                Matcher matcher = SECTION_PATTERN.matcher(text);
                if (matcher.find()) {
                    int section = Integer.parseInt(matcher.group(1));
                    headers.add(new SectionHeader(section, r));
                    break;
                }
            }
        }
        return headers;
    }

    private void parseSection4(Long reportId, Sheet sheet, int startRow, int endRow, ImportExcelResponse response) {
        SectionParseContext ctx = findTableContext(sheet, startRow, endRow, 3, 19);
        if (!ctx.isValid()) {
            response.addError(new ParseError("Раздел 4", "-", sheet.getSheetName(), "Не найдены заголовки таблицы"));
            return;
        }

        Map<Integer, String> codeToField = new HashMap<>();
        codeToField.put(14, "technical");
        codeToField.put(15, "naturalScience");
        codeToField.put(16, "tourismAndLocalHistory");
        codeToField.put(17, "socialAndHumanitarian");
        codeToField.put(18, "artisticOrientation");
        codeToField.put(19, "physicalEducationAndSports");
        codeToField.put(20, "preprofessionalProgramsInTheFieldOfArts");
        codeToField.put(21, "additionalEducationalProgramsSportsTraining");
        codeToField.put(22, "numberOfStudentsAdditionalGeneralEducationPrograms");

        Unit4 unit4 = unit4Repository.findById(reportId).orElse(new Unit4(reportId));
        boolean updated = false;

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            Integer code = readRowCode(row, ctx.rowCodeCol);
            if (code == null || !codeToField.containsKey(code)) continue;
            Integer[] values = readIntegerRow(row, ctx.columnIndexByNumber, 3, 19);
            String field = codeToField.get(code);
            if (field == null) continue;
            if (hasAnyValue(values)) {
                applyUnit4Field(unit4, field, values);
                updated = true;
            }
        }

        if (updated) {
            unit4Repository.save(unit4);
            response.addUpdatedSection("Раздел 4");
        }
    }

    private void parseSection5(Long reportId, Sheet sheet, int startRow, int endRow, ImportExcelResponse response) {
        SectionParseContext ctx = findTableContext(sheet, startRow, endRow, 3, 7);
        if (!ctx.isValid()) {
            response.addError(new ParseError("Раздел 5", "-", sheet.getSheetName(), "Не найдены заголовки таблицы"));
            return;
        }

        Unit5 unit5 = unit5Repository.findById(reportId).orElse(new Unit5(reportId));
        boolean updated = false;

        Map<Integer, String> codeToField = new HashMap<>();
        codeToField.put(23, "technical");
        codeToField.put(24, "naturalScience");
        codeToField.put(25, "tourismAndLocalHistory");
        codeToField.put(26, "socialAndHumanitarian");
        codeToField.put(28, "physicalEducationAndSports");
        codeToField.put(29, "preprofessionalProgramsInTheFieldOfArts");
        codeToField.put(30, "additionalEducationalProgramsSportsTraining");

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = readRowLabel(row);
            Integer code = readRowCode(row, ctx.rowCodeCol);
            Integer[] values = readIntegerRow(row, ctx.columnIndexByNumber, 3, 7);
            if (!hasAnyValue(values)) continue;

            String field = null;
            if (code != null && codeToField.containsKey(code)) {
                field = codeToField.get(code);
            } else if (label != null) {
                String normalized = normalize(label);
                if (normalized.contains("художествен")) field = "artisticOrientation";
                else if (normalized.contains("физкультур") || normalized.contains("спортив")) field = "physicalEducationAndSports";
                else if (normalized.contains("техничес")) field = "technical";
                else if (normalized.contains("естествен")) field = "naturalScience";
                else if (normalized.contains("турист") || normalized.contains("краевед")) field = "tourismAndLocalHistory";
                else if (normalized.contains("социаль") || normalized.contains("гуманитар")) field = "socialAndHumanitarian";
                else if (normalized.contains("предпрофессион")) field = "preprofessionalProgramsInTheFieldOfArts";
                else if (normalized.contains("спортивной подготовки")) field = "additionalEducationalProgramsSportsTraining";
            }

            if (field != null) {
                applyUnit5Field(unit5, field, values);
                updated = true;
            }
        }

        if (updated) {
            unit5Repository.save(unit5);
            response.addUpdatedSection("Раздел 5");
        }
    }

    private void parseSection6(Long reportId, Sheet sheet, int startRow, int endRow, ImportExcelResponse response) {
        SectionParseContext ctx = findTableContext(sheet, startRow, endRow, 3, 3);
        if (!ctx.isValid()) {
            response.addError(new ParseError("Раздел 6", "-", sheet.getSheetName(), "Не найдены заголовки таблицы"));
            return;
        }

        Unit6 unit6 = unit6Repository.findById(reportId).orElse(new Unit6(reportId));
        boolean updated = false;

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = readRowLabel(row);
            if (label == null) continue;
            Integer value = readInt(row.getCell(ctx.columnIndexByNumber.get(3)));
            if (value == null) continue;
            String normalized = normalize(label);
            if (normalized.contains("поход")) {
                unit6.setHiking(value);
                updated = true;
            } else if (normalized.contains("экскурс")) {
                unit6.setExcursions(value);
                updated = true;
            } else if (normalized.contains("экспед")) {
                unit6.setInFieldExpeditions(value);
                updated = true;
            }
        }

        if (updated) {
            unit6Repository.save(unit6);
            response.addUpdatedSection("Раздел 6");
        }
    }

    private void parseSection7(Long reportId, Sheet sheet, int startRow, int endRow, ImportExcelResponse response) {
        SectionParseContext ctx = findTableContext(sheet, startRow, endRow, 3, 13);
        if (!ctx.isValid()) {
            response.addError(new ParseError("Раздел 7", "-", sheet.getSheetName(), "Не найдены заголовки таблицы"));
            return;
        }

        Unit7 unit7 = unit7Repository.findById(reportId).orElse(new Unit7(reportId));
        boolean updated = false;

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            String label = readRowLabel(row);
            Integer code = readRowCode(row, ctx.rowCodeCol);
            Integer[] values = readIntegerRow(row, ctx.columnIndexByNumber, 3, 13);
            if (label == null && code == null) continue;

            String field = null;
            if (code != null) {
                switch (code) {
                    case 34 -> field = "theNumberOfEmployeesIsTotal";
                    case 36 -> field = "seniorStaff";
                    case 37 -> field = "deputyHeads";
                    case 38 -> field = "branchManager";
                    case 39 -> field = "teachingStaffTotal";
                    case 40 -> field = "teachersOfAdditionalEducation";
                    case 41 -> field = "trainingAndSupportStaff";
                    case 42 -> field = "otherStaff";
                    case 43 -> field = "professionalDevelopment";
                    default -> { }
                }
            }

            if (field == null && label != null) {
                String normalized = normalize(label);
                if (normalized.contains("руководящие работники") && normalized.contains("всего")) {
                    field = "seniorStaffTotal";
                } else if (normalized.contains("численность работников") && normalized.contains("всего")) {
                    field = "theNumberOfEmployeesIsTotal";
                } else if (normalized.contains("педагогические работники") && normalized.contains("всего")) {
                    field = "teachingStaffTotal";
                } else if (normalized.contains("педагоги дополнительного")) {
                    field = "teachersOfAdditionalEducation";
                } else if (normalized.contains("учебно-вспомогательный")) {
                    field = "trainingAndSupportStaff";
                } else if (normalized.contains("иной персонал")) {
                    field = "otherStaff";
                } else if (normalized.contains("повышение квалификации")) {
                    field = "professionalDevelopment";
                }
            }

            if (field == null) continue;

            if (field.equals("professionalDevelopment")) {
                Integer total = values != null && values.length > 0 ? values[0] : null;
                if (total != null) {
                    unit7.setProfessionalDevelopment(total);
                    updated = true;
                }
                continue;
            }

            if (values != null && hasAnyValue(values)) {
                applyUnit7Field(unit7, field, values);
                updated = true;
            }
        }

        if (updated) {
            unit7Repository.save(unit7);
            response.addUpdatedSection("Раздел 7");
        }
    }

    private void parseSection12(Long reportId, Sheet sheet, int startRow, int endRow, ImportExcelResponse response) {
        int rowCodeCol = findRowCodeColumn(sheet, startRow, endRow);
        if (rowCodeCol < 0) {
            response.addError(new ParseError("Раздел 12", "-", sheet.getSheetName(), "Не найден столбец с номером строки"));
            return;
        }

        HeaderColumns cols = findUnit12Columns(sheet, startRow, endRow);
        if (!cols.isValid()) {
            response.addError(new ParseError("Раздел 12", "-", sheet.getSheetName(), "Не найдены столбцы 'Всего/в том числе/из них'"));
            return;
        }

        Unit12 unit12 = unit12Repository.findById(reportId).orElse(new Unit12(reportId));
        boolean updated = false;

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            Integer code = readRowCode(row, rowCodeCol);
            if (code == null) continue;

            if (code >= 81 && code <= 88) {
                String[] values = readStringRow(row, cols);
                if (!hasAnyValue(values)) continue;
                switch (code) {
                    case 81 -> unit12.setPersonalComputersTotal(values);
                    case 82 -> unit12.setPortablePersonalComputers(values);
                    case 83 -> unit12.setTabletComputers(values);
                    case 84 -> unit12.setGraphicTabletsComputers(values);
                    case 85 -> unit12.setAsPartOfLocalComputerNetworks(values);
                    case 86 -> unit12.setHavingAccessToTheInternet(values);
                    case 87 -> unit12.setOrganizationsWithAccessToTheIntranetPortal(values);
                    case 88 -> unit12.setReceivedInTheReportingYear(values);
                    default -> { }
                }
                updated = true;
            } else {
                String totalValue = readText(row.getCell(cols.totalCol));
                if (totalValue == null) continue;
                switch (code) {
                    case 89 -> unit12.setElectronicTerminals(totalValue);
                    case 90 -> unit12.setElectronicTerminalsWithAccessToInternetResources(totalValue);
                    case 91 -> unit12.setMultimediaProjectors(totalValue);
                    case 92 -> unit12.setInteractiveWhiteboards(totalValue);
                    case 93 -> unit12.setPrinters(totalValue);
                    case 94 -> unit12.setPrinters3D(totalValue);
                    case 95 -> unit12.setScanners(totalValue);
                    case 96 -> unit12.setMultifunctionDevices(totalValue);
                    default -> { }
                }
                updated = true;
            }
        }

        if (updated) {
            unit12Repository.save(unit12);
            response.addUpdatedSection("Раздел 12");
        }
    }

    private void importFull(Long reportId, MultipartFile file, ImportExcelResponse response) {
        TemplateDefinition template = templateRegistry.getById("default");
        if (template == null) {
            response.addError(new ParseError("template", "default", "-", "Шаблон не найден"));
            return;
        }
        try (InputStream inputStream = file.getInputStream()) {
            ParsedReport parsed = excelReportParser.parse(inputStream, file.getOriginalFilename(), template);
            parsed.getErrors().forEach(response::addError);
            applyParsedReport(reportId, parsed, response);
        } catch (Exception ex) {
            response.addError(new ParseError("file", file.getOriginalFilename(), "-", "Ошибка чтения файла: " + ex.getMessage()));
        }
    }

    private void applyParsedReport(Long reportId, ParsedReport parsed, ImportExcelResponse response) {
        for (Map.Entry<String, Map<String, Object>> entry : parsed.getSections().entrySet()) {
            String sectionKey = normalizeSectionKey(entry.getKey());
            Map<String, Object> sectionValues = entry.getValue();
            if (sectionValues == null || sectionValues.isEmpty()) {
                continue;
            }
            applyFullSection(reportId, sectionKey, sectionValues, response);
        }
    }

    private void applyFullSection(Long reportId, String sectionKey, Map<String, Object> sectionValues, ImportExcelResponse response) {
        switch (sectionKey) {
            case "0" -> {
                MainInfo info = mainInfoRepository.findById(reportId).orElse(new MainInfo());
                setId(info, reportId);
                applyMainInfo(info, sectionValues);
                mainInfoRepository.save(info);
                response.addUpdatedSection("Общая информация");
            }
            case "20" -> {
                ContactInformation contact = contactInfoRepository.findById(reportId).orElse(new ContactInformation());
                setId(contact, reportId);
                applyContactInfo(contact, sectionValues);
                contactInfoRepository.save(contact);
                response.addUpdatedSection("Контактная информация");
            }
            default -> applySectionBySchema(reportId, sectionKey, sectionValues, response);
        }
    }

    private void applySectionBySchema(Long reportId, String sectionKey, Map<String, Object> sectionValues, ImportExcelResponse response) {
        List<FieldDef> schema = SECTION_SCHEMA.get(sectionKey);
        if (schema == null) {
            return;
        }

        List<String> labels = labelOrderService.getLabels(sectionKey);
        List<Object> orderedValues = new ArrayList<>();
        if (!labels.isEmpty()) {
            for (String label : labels) {
                orderedValues.add(sectionValues.get(label));
            }
        } else {
            orderedValues.addAll(sectionValues.values());
        }

        Object entity = resolveEntity(reportId, sectionKey);
        if (entity == null) {
            return;
        }

        int offset = 0;
        for (FieldDef def : schema) {
            if (def.count > 0) {
                List<Object> slice = orderedValues.subList(Math.min(offset, orderedValues.size()),
                        Math.min(offset + def.count, orderedValues.size()));
                offset += def.count;
                applyArrayField(entity, def.key, slice);
            } else {
                Object value = offset < orderedValues.size() ? orderedValues.get(offset) : null;
                offset += 1;
                applyField(entity, def.key, value);
            }
        }

        saveEntity(sectionKey, entity);
        response.addUpdatedSection("Раздел " + sectionKey);
    }

    private Object resolveEntity(Long reportId, String sectionKey) {
        return switch (sectionKey) {
            case "1" -> unit1Repository.findById(reportId).orElse(new Unit1(reportId));
            case "2" -> unit2Repository.findById(reportId).orElse(new Unit2(reportId));
            case "3" -> unit3Repository.findById(reportId).orElse(new Unit3(reportId));
            case "4" -> unit4Repository.findById(reportId).orElse(new Unit4(reportId));
            case "5" -> unit5Repository.findById(reportId).orElse(new Unit5(reportId));
            case "6" -> unit6Repository.findById(reportId).orElse(new Unit6(reportId));
            case "7" -> unit7Repository.findById(reportId).orElse(new Unit7(reportId));
            case "8" -> unit8Repository.findById(reportId).orElse(new Unit8(reportId));
            case "9" -> unit9Repository.findById(reportId).orElse(new Unit9(reportId));
            case "10" -> unit10Repository.findById(reportId).orElse(new Unit10(reportId));
            case "11" -> unit11Repository.findById(reportId).orElse(new Unit11(reportId));
            case "12" -> unit12Repository.findById(reportId).orElse(new Unit12(reportId));
            case "13" -> unit13Repository.findById(reportId).orElse(new Unit13(reportId));
            case "14" -> unit14Repository.findById(reportId).orElse(new Unit14(reportId));
            case "15" -> unit15Repository.findById(reportId).orElse(new Unit15(reportId));
            case "16" -> unit16Repository.findById(reportId).orElse(new Unit16(reportId));
            case "17" -> unit17Repository.findById(reportId).orElse(new Unit17(reportId));
            case "18" -> unit18Repository.findById(reportId).orElse(new Unit18(reportId));
            default -> null;
        };
    }

    private void saveEntity(String sectionKey, Object entity) {
        switch (sectionKey) {
            case "1" -> unit1Repository.save((Unit1) entity);
            case "2" -> unit2Repository.save((Unit2) entity);
            case "3" -> unit3Repository.save((Unit3) entity);
            case "4" -> unit4Repository.save((Unit4) entity);
            case "5" -> unit5Repository.save((Unit5) entity);
            case "6" -> unit6Repository.save((Unit6) entity);
            case "7" -> unit7Repository.save((Unit7) entity);
            case "8" -> unit8Repository.save((Unit8) entity);
            case "9" -> unit9Repository.save((Unit9) entity);
            case "10" -> unit10Repository.save((Unit10) entity);
            case "11" -> unit11Repository.save((Unit11) entity);
            case "12" -> unit12Repository.save((Unit12) entity);
            case "13" -> unit13Repository.save((Unit13) entity);
            case "14" -> unit14Repository.save((Unit14) entity);
            case "15" -> unit15Repository.save((Unit15) entity);
            case "16" -> unit16Repository.save((Unit16) entity);
            case "17" -> unit17Repository.save((Unit17) entity);
            case "18" -> unit18Repository.save((Unit18) entity);
            default -> { }
        }
    }

    private void applyMainInfo(MainInfo info, Map<String, Object> values) {
        info.setOrganizationName(readString(values.get("Наименование отчитывающейся организации")));
        info.setPostalAddress(readString(values.get("Почтовый адрес")));
        info.setOkudFormCode(readString(values.get("Код формы по ОКУД")));
        info.setOkpoOrgCode(readString(values.get("Код отчитывающейся организации по ОКПО")));
        info.setChangeDate1(readSqlDate(values.get("О внесении изменений (при наличии) (дата 1)")));
        info.setChangeNumber1(readString(values.get("О внесении изменений (при наличии) (номер 1)")));
        info.setChangeDate2(readSqlDate(values.get("О внесении изменений (при наличии) (дата 2)")));
        info.setChangeNumber2(readString(values.get("О внесении изменений (при наличии) (номер 2)")));
    }

    private void applyContactInfo(ContactInformation contact, Map<String, Object> values) {
        contact.setPosition(readString(values.get("Должность ответственного лица")));
        contact.setFullName(readString(values.get("ФИО ответственного лица")));
        contact.setPhoneNumber(readString(values.get("Номер телефона ответственного лица")));
        contact.setEmail(readString(values.get("Email ответственного лица")));
        contact.setDocumentDay(readString(values.get("День составления документа")));
        contact.setDocumentMonth(readString(values.get("Месяц составления документа")));
        contact.setDocumentYear(readString(values.get("Год составления документа (2 последние цифры)")));
    }

    private void applyField(Object entity, String fieldName, Object value) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object converted = convertValue(value, field.getType());
            field.set(entity, converted);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    private void applyArrayField(Object entity, String fieldName, List<Object> values) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Class<?> componentType = field.getType().getComponentType();
            if (componentType == null) {
                return;
            }
            Object array = java.lang.reflect.Array.newInstance(componentType, values.size());
            for (int i = 0; i < values.size(); i++) {
                Object converted = convertValue(values.get(i), componentType);
                java.lang.reflect.Array.set(array, i, converted);
            }
            field.set(entity, array);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;
        String text = readString(value);
        if (targetType == String.class) {
            return text;
        }
        if (text == null) return null;
        try {
            if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(normalizeNumber(text));
            }
            if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(normalizeNumber(text));
            }
            if (targetType == Byte.class || targetType == byte.class) {
                return Byte.parseByte(normalizeNumber(text));
            }
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    private String normalizeNumber(String text) {
        return text.replaceAll("\\s+", "").replace(",", ".").replace("х", "").replace("-", "");
    }

    private String readString(Object value) {
        if (value == null) return null;
        String text = value.toString().trim();
        return text.isEmpty() ? null : text;
    }

    private Date readSqlDate(Object value) {
        String text = readString(value);
        if (text == null) return null;
        if (text.contains("T")) {
            try {
                return Date.valueOf(LocalDate.parse(text.substring(0, 10)));
            } catch (Exception ignored) {
            }
        }
        List<DateTimeFormatter> formats = List.of(
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("dd.MM.yy")
        );
        for (DateTimeFormatter formatter : formats) {
            try {
                LocalDate date = LocalDate.parse(text, formatter);
                return Date.valueOf(date);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private void setId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    private String normalizeSectionKey(String raw) {
        if (raw == null) return "";
        String text = raw.trim();
        try {
            double value = Double.parseDouble(text.replace(",", "."));
            if (Math.floor(value) == value) {
                return String.valueOf((int) value);
            }
        } catch (NumberFormatException ignored) {
        }
        return text.replaceAll("\\D+", "");
    }

    private SectionParseContext findTableContext(Sheet sheet, int startRow, int endRow, int numberFrom, int numberTo) {
        int rowCodeCol = findRowCodeColumn(sheet, startRow, endRow);
        Map<Integer, Integer> columnIndexByNumber = new HashMap<>();

        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            for (int c = 0; c < row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                Integer number = readHeaderNumber(cell);
                if (number != null && number >= numberFrom && number <= numberTo) {
                    columnIndexByNumber.put(number, c);
                }
            }
            if (columnIndexByNumber.size() >= (numberTo - numberFrom + 1)) {
                break;
            }
        }

        return new SectionParseContext(rowCodeCol, columnIndexByNumber);
    }

    private int findRowCodeColumn(Sheet sheet, int startRow, int endRow) {
        for (int r = startRow; r <= endRow; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            for (int c = 0; c < row.getLastCellNum(); c++) {
                String text = readText(row.getCell(c));
                if (text == null) continue;
                String normalized = normalize(text);
                if (normalized.contains("№") && normalized.contains("стр")) {
                    return c;
                }
            }
        }
        return -1;
    }

    private HeaderColumns findUnit12Columns(Sheet sheet, int startRow, int endRow) {
        int totalCol = -1;
        int usedCol = -1;
        int availableCol = -1;

        for (int r = startRow; r <= Math.min(endRow, startRow + 8); r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;
            for (int c = 0; c < row.getLastCellNum(); c++) {
                String text = readText(row.getCell(c));
                if (text == null) continue;
                String normalized = normalize(text);
                if (normalized.contains("всего") && totalCol < 0) totalCol = c;
                if (normalized.contains("учеб") && normalized.contains("в том числе")) usedCol = c;
                if (normalized.contains("доступ") && normalized.contains("из них")) availableCol = c;
            }
        }

        return new HeaderColumns(totalCol, usedCol, availableCol);
    }

    private Integer readHeaderNumber(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) Math.round(cell.getNumericCellValue());
        }
        String text = readText(cell);
        if (text == null) return null;
        text = text.replaceAll("\\s+", "");
        if (!text.matches("\\d+")) return null;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private Integer readRowCode(Row row, int rowCodeCol) {
        if (rowCodeCol < 0) return null;
        Cell cell = row.getCell(rowCodeCol);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) Math.round(cell.getNumericCellValue());
        }
        String text = readText(cell);
        if (text == null) return null;
        text = text.replaceAll("\\D+", "");
        if (text.isEmpty()) return null;
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String readRowLabel(Row row) {
        if (row == null) return null;
        for (int c = 0; c < Math.min(5, row.getLastCellNum()); c++) {
            String text = readText(row.getCell(c));
            if (text != null && !text.isBlank()) {
                return text;
            }
        }
        return null;
    }

    private Integer[] readIntegerRow(Row row, Map<Integer, Integer> columnIndexByNumber, int from, int to) {
        Integer[] values = new Integer[to - from + 1];
        for (int num = from; num <= to; num++) {
            Integer col = columnIndexByNumber.get(num);
            if (col == null) continue;
            values[num - from] = readInt(row.getCell(col));
        }
        return values;
    }

    private String[] readStringRow(Row row, HeaderColumns cols) {
        String[] values = new String[3];
        values[0] = readText(row.getCell(cols.totalCol));
        values[1] = readText(row.getCell(cols.usedCol));
        values[2] = readText(row.getCell(cols.availableCol));
        return values;
    }

    private String readText(Cell cell) {
        if (cell == null) return null;
        String text = FORMATTER.formatCellValue(cell);
        if (text == null) return null;
        text = text.trim();
        return text.isEmpty() ? null : text;
    }

    private Integer readInt(Cell cell) {
        String text = readText(cell);
        if (text == null) return null;
        String normalized = text.replaceAll("\\s+", "").replace(",", ".");
        if (normalized.equalsIgnoreCase("х") || normalized.equals("-")) return null;
        try {
            double value = Double.parseDouble(normalized);
            return (int) Math.round(value);
        } catch (NumberFormatException ex) {
            String digits = normalized.replaceAll("\\D+", "");
            if (digits.isEmpty()) return null;
            try {
                return Integer.parseInt(digits);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    private boolean hasAnyValue(Integer[] values) {
        if (values == null) return false;
        for (Integer value : values) {
            if (value != null) return true;
        }
        return false;
    }

    private boolean hasAnyValue(String[] values) {
        if (values == null) return false;
        for (String value : values) {
            if (value != null && !value.isBlank()) return true;
        }
        return false;
    }

    private void applyUnit4Field(Unit4 unit4, String field, Integer[] values) {
        switch (field) {
            case "technical" -> unit4.setTechnical(values);
            case "naturalScience" -> unit4.setNaturalScience(values);
            case "tourismAndLocalHistory" -> unit4.setTourismAndLocalHistory(values);
            case "socialAndHumanitarian" -> unit4.setSocialAndHumanitarian(values);
            case "artisticOrientation" -> unit4.setArtisticOrientation(values);
            case "physicalEducationAndSports" -> unit4.setPhysicalEducationAndSports(values);
            case "preprofessionalProgramsInTheFieldOfArts" -> unit4.setPreprofessionalProgramsInTheFieldOfArts(values);
            case "additionalEducationalProgramsSportsTraining" -> unit4.setAdditionalEducationalProgramsSportsTraining(values);
            case "numberOfStudentsAdditionalGeneralEducationPrograms" -> unit4.setNumberOfStudentsAdditionalGeneralEducationPrograms(values);
            default -> { }
        }
    }

    private void applyUnit5Field(Unit5 unit5, String field, Integer[] values) {
        switch (field) {
            case "technical" -> unit5.setTechnical(values);
            case "naturalScience" -> unit5.setNaturalScience(values);
            case "tourismAndLocalHistory" -> unit5.setTourismAndLocalHistory(values);
            case "socialAndHumanitarian" -> unit5.setSocialAndHumanitarian(values);
            case "artisticOrientation" -> unit5.setArtisticOrientation(values);
            case "physicalEducationAndSports" -> unit5.setPhysicalEducationAndSports(values);
            case "preprofessionalProgramsInTheFieldOfArts" -> unit5.setPreprofessionalProgramsInTheFieldOfArts(values);
            case "additionalEducationalProgramsSportsTraining" -> unit5.setAdditionalEducationalProgramsSportsTraining(values);
            default -> { }
        }
    }

    private void applyUnit7Field(Unit7 unit7, String field, Integer[] values) {
        switch (field) {
            case "theNumberOfEmployeesIsTotal" -> unit7.setTheNumberOfEmployeesIsTotal(values);
            case "seniorStaffTotal" -> unit7.setSeniorStaffTotal(values);
            case "seniorStaff" -> unit7.setSeniorStaff(values);
            case "deputyHeads" -> unit7.setDeputyHeads(values);
            case "branchManager" -> unit7.setBranchManager(values);
            case "teachingStaffTotal" -> unit7.setTeachingStaffTotal(values);
            case "teachersOfAdditionalEducation" -> unit7.setTeachersOfAdditionalEducation(values);
            case "trainingAndSupportStaff" -> unit7.setTrainingAndSupportStaff(values);
            case "otherStaff" -> unit7.setOtherStaff(values);
            default -> { }
        }
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase().replace("\n", " ").replaceAll("\\s+", " ").trim();
    }

    private static class SectionHeader {
        private final int sectionNumber;
        private final int rowIndex;

        private SectionHeader(int sectionNumber, int rowIndex) {
            this.sectionNumber = sectionNumber;
            this.rowIndex = rowIndex;
        }
    }

    private static class SectionParseContext {
        private final int rowCodeCol;
        private final Map<Integer, Integer> columnIndexByNumber;

        private SectionParseContext(int rowCodeCol, Map<Integer, Integer> columnIndexByNumber) {
            this.rowCodeCol = rowCodeCol;
            this.columnIndexByNumber = columnIndexByNumber;
        }

        private boolean isValid() {
            return rowCodeCol >= 0 && columnIndexByNumber != null && !columnIndexByNumber.isEmpty();
        }
    }

    private static class HeaderColumns {
        private final int totalCol;
        private final int usedCol;
        private final int availableCol;

        private HeaderColumns(int totalCol, int usedCol, int availableCol) {
            this.totalCol = totalCol;
            this.usedCol = usedCol;
            this.availableCol = availableCol;
        }

        private boolean isValid() {
            return totalCol >= 0 && usedCol >= 0 && availableCol >= 0;
        }
    }

    private static final Map<String, List<FieldDef>> SECTION_SCHEMA = buildSchema();

    private static Map<String, List<FieldDef>> buildSchema() {
        Map<String, List<FieldDef>> schema = new HashMap<>();
        schema.put("1", List.of(
                new FieldDef("organizationType"),
                new FieldDef("terrainType")
        ));
        schema.put("2", List.of(
                new FieldDef("personalizedFinancingOfChildrensAdditionalEducation"),
                new FieldDef("newWageSystem"),
                new FieldDef("theEducationalActivityLicense")
        ));
        schema.put("3", List.of(
                new FieldDef("technical", 16),
                new FieldDef("naturalScience", 16),
                new FieldDef("tourismAndLocalHistory", 16),
                new FieldDef("socialAndHumanitarian", 16),
                new FieldDef("artisticOrientation", 16),
                new FieldDef("physicalEducationAndSports", 16),
                new FieldDef("preprofessionalProgramsInTheFieldOfArts", 16),
                new FieldDef("additionalEducationalProgramsSportsTraining", 16)
        ));
        schema.put("4", List.of(
                new FieldDef("technical", 17),
                new FieldDef("naturalScience", 17),
                new FieldDef("tourismAndLocalHistory", 17),
                new FieldDef("socialAndHumanitarian", 17),
                new FieldDef("artisticOrientation", 17),
                new FieldDef("physicalEducationAndSports", 17),
                new FieldDef("preprofessionalProgramsInTheFieldOfArts", 17),
                new FieldDef("additionalEducationalProgramsSportsTraining", 17),
                new FieldDef("numberOfStudentsAdditionalGeneralEducationPrograms", 17)
        ));
        schema.put("5", List.of(
                new FieldDef("technical", 5),
                new FieldDef("naturalScience", 5),
                new FieldDef("tourismAndLocalHistory", 5),
                new FieldDef("socialAndHumanitarian", 5),
                new FieldDef("artisticOrientation", 5),
                new FieldDef("physicalEducationAndSports", 5),
                new FieldDef("preprofessionalProgramsInTheFieldOfArts", 5),
                new FieldDef("additionalEducationalProgramsSportsTraining", 5)
        ));
        schema.put("6", List.of(
                new FieldDef("hiking"),
                new FieldDef("excursions"),
                new FieldDef("inFieldExpeditions")
        ));
        schema.put("7", List.of(
                new FieldDef("theNumberOfEmployeesIsTotal", 11),
                new FieldDef("seniorStaffTotal", 11),
                new FieldDef("seniorStaff", 11),
                new FieldDef("deputyHeads", 11),
                new FieldDef("branchManager", 11),
                new FieldDef("teachingStaffTotal", 11),
                new FieldDef("teachersOfAdditionalEducation", 11),
                new FieldDef("trainingAndSupportStaff", 11),
                new FieldDef("otherStaff", 11),
                new FieldDef("professionalDevelopment")
        ));
        schema.put("8", List.of(
                new FieldDef("theNumberOfEmployeesIsTotal", 11),
                new FieldDef("seniorStaffTotal", 11),
                new FieldDef("seniorStaff", 11),
                new FieldDef("deputyHeads", 11),
                new FieldDef("branchManager", 11),
                new FieldDef("teachingStaffTotal", 11),
                new FieldDef("teachersOfAdditionalEducation", 11),
                new FieldDef("trainingAndSupportStaff", 11),
                new FieldDef("otherStaff", 11)
        ));
        schema.put("9", List.of(
                new FieldDef("organizationBuildings", 13),
                new FieldDef("partOfTheBuilding", 13)
        ));
        schema.put("10", List.of(
                new FieldDef("assemblyHallInOrganization"),
                new FieldDef("assemblyHallThirdPartyOrganization"),
                new FieldDef("concertHallInOrganization"),
                new FieldDef("concertHallThirdPartyOrganization"),
                new FieldDef("gameRoomInOrganization"),
                new FieldDef("gameRoomThirdPartyOrganization"),
                new FieldDef("trainingClassInOrganization"),
                new FieldDef("trainingClassThirdPartyOrganization"),
                new FieldDef("laboratoryInOrganization"),
                new FieldDef("laboratoryThirdPartyOrganization"),
                new FieldDef("workshopInOrganization"),
                new FieldDef("workshopThirdPartyOrganization"),
                new FieldDef("choreographyClassesInOrganization"),
                new FieldDef("choreographyClassesThirdPartyOrganization"),
                new FieldDef("choreographyClassesWithShowerInOrganization"),
                new FieldDef("choreographyClassesWithShowerThirdPartyOrganization"),
                new FieldDef("gymInOrganization"),
                new FieldDef("gymThirdPartyOrganization"),
                new FieldDef("gymWithShowerInOrganization"),
                new FieldDef("gymWithShowerThirdPartyOrganization"),
                new FieldDef("indoorSwimmingPoolInOrganization"),
                new FieldDef("indoorSwimmingPoolThirdPartyOrganization"),
                new FieldDef("lectureHallInOrganization"),
                new FieldDef("lectureHallThirdPartyOrganization"),
                new FieldDef("computerRoomInOrganization"),
                new FieldDef("computerRoomThirdPartyOrganization"),
                new FieldDef("medicalCenterInOrganization"),
                new FieldDef("medicalCenterThirdPartyOrganization"),
                new FieldDef("diningRoomInOrganization"),
                new FieldDef("diningRoomThirdPartyOrganization"),
                new FieldDef("museumInOrganization"),
                new FieldDef("museumThirdPartyOrganization"),
                new FieldDef("wildlifeCornerInOrganization"),
                new FieldDef("wildlifeCornerThirdPartyOrganization"),
                new FieldDef("climbingWallInOrganization"),
                new FieldDef("climbingWallThirdPartyOrganization"),
                new FieldDef("touristBaseInOrganization"),
                new FieldDef("touristBaseThirdPartyOrganization"),
                new FieldDef("libraryInOrganization"),
                new FieldDef("libraryThirdPartyOrganization")
        ));
        schema.put("11", List.of(
                new FieldDef("totalAreaOfBuildingsTotal", 5),
                new FieldDef("theAreaForEducationalActivities", 5),
                new FieldDef("theAreaOfThePremisesForStudentsLeisureActivities", 5),
                new FieldDef("totalLandArea", 5),
                new FieldDef("theAreaOfTheSportsGround", 5),
                new FieldDef("theAreaOfTheTrainingAndExperimentalSite", 5)
        ));
        schema.put("12", List.of(
                new FieldDef("personalComputersTotal", 3),
                new FieldDef("portablePersonalComputers", 3),
                new FieldDef("tabletComputers", 3),
                new FieldDef("graphicTabletsComputers", 3),
                new FieldDef("asPartOfLocalComputerNetworks", 3),
                new FieldDef("havingAccessToTheInternet", 3),
                new FieldDef("organizationsWithAccessToTheIntranetPortal", 3),
                new FieldDef("receivedInTheReportingYear", 3),
                new FieldDef("electronicTerminals"),
                new FieldDef("electronicTerminalsWithAccessToInternetResources"),
                new FieldDef("multimediaProjectors"),
                new FieldDef("interactiveWhiteboards"),
                new FieldDef("printers"),
                new FieldDef("printers3D"),
                new FieldDef("scanners"),
                new FieldDef("multifunctionDevices")
        ));
        schema.put("13", List.of(
                new FieldDef("availabilityOfFixedTelephoneService"),
                new FieldDef("emailAddress"),
                new FieldDef("websiteOnTheInternet"),
                new FieldDef("availabilityOfInformationOnTheWebsiteAboutOrganization")
        ));
        schema.put("14", List.of(
                new FieldDef("maximumInternetAccessSpeed"),
                new FieldDef("maximumSpeedOfFixedWiredInternetAccess"),
                new FieldDef("maximumSpeedOfFixedWirelessInternetAccess"),
                new FieldDef("maximumSpeedOfMobileInternetAccess")
        ));
        schema.put("15", List.of(
                new FieldDef("theAmountOfFundsReceived", 5),
                new FieldDef("includingFundsBudgetsOfAllLevelsTotal", 5),
                new FieldDef("includingTheBudgetOfFederal", 5),
                new FieldDef("includingTheBudgetOfTheSubjectOfTheRussiaFederation", 5),
                new FieldDef("localBudget", 5),
                new FieldDef("organizations", 5),
                new FieldDef("population", 5),
                new FieldDef("socialFunds", 5),
                new FieldDef("foreignSources", 5),
                new FieldDef("balanceBeginning"),
                new FieldDef("balanceEnd")
        ));
        schema.put("16", List.of(
                new FieldDef("totalExpensesTotal"),
                new FieldDef("paymentOfLaborTotal"),
                new FieldDef("paymentForWorkTotal"),
                new FieldDef("socialSecurityTotal"),
                new FieldDef("otherExpensesTotal"),
                new FieldDef("receiptOfNonFinancialAssetsTotal"),
                new FieldDef("totalExpensesBudget"),
                new FieldDef("paymentOfLaborBudget"),
                new FieldDef("paymentForWorkBudget"),
                new FieldDef("socialSecurityBudget"),
                new FieldDef("otherExpensesBudget"),
                new FieldDef("receiptOfNonFinancialAssetsBudget")
        ));
        schema.put("17", List.of(
                new FieldDef("totalDigitalTechCostsTotal"),
                new FieldDef("internalDigitalTechCosts"),
                new FieldDef("externalDigitalTechCosts"),
                new FieldDef("securityProductsServices")
        ));
        schema.put("18", List.of(
                new FieldDef("internalDigitalTechCostsTotal"),
                new FieldDef("ownFunds"),
                new FieldDef("budgetFunds"),
                new FieldDef("otherAttractedFunds")
        ));
        return schema;
    }

    private static class FieldDef {
        private final String key;
        private final int count;

        private FieldDef(String key) {
            this.key = key;
            this.count = 0;
        }

        private FieldDef(String key, int count) {
            this.key = key;
            this.count = count;
        }
    }
}
