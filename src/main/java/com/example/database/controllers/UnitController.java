package com.example.database.controllers;


import com.example.database.common.HeaderType;
import com.example.database.dto.*;
import com.example.database.enteties.*;
import com.example.database.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/unit")
public class UnitController {

    @Autowired
    ExcelExportService excelService;

    @Autowired
    private ReportCreationService reportCreationService;
    @Autowired
    private ExcelImportService excelImportService;
    @Autowired
    private ReportDeletionService reportDeletionService;

    @Autowired
    MainInfoService mainInfoService;

    @Autowired
    ContactInfoService contactInfoService;

    @Autowired
    Unit1Service unit1Service;

    @Autowired
    Unit2Service unit2Service;

    @Autowired
    Unit3Service unit3Service;

    @Autowired
    Unit4Service unit4Service;

    @Autowired
    Unit5Service unit5Service;

    @Autowired
    Unit6Service unit6Service;
    @Autowired
    Unit7Service unit7Service;

    @Autowired
    Unit8Service unit8Service;

    @Autowired
    Unit9Service unit9Service;

    @Autowired
    Unit10Service unit10Service;
    @Autowired
    Unit11Service unit11Service;

    @Autowired
    Unit12Service unit12Service;

    @Autowired
    Unit13Service unit13Service;

    @Autowired
    Unit14Service unit14Service;

    @Autowired
    Unit15Service unit15Service;

    @Autowired
    Unit16Service unit16Service;
    @Autowired
    Unit17Service unit17Service;

    @Autowired
    Unit18Service unit18Service;
    @Autowired
    UserService userService;


    @PostMapping("initReference")
    ResponseEntity<ReferenceResponse> initReference(@RequestBody MainInfo mainInfo) {
        MainInfo created = reportCreationService.createEmptyReport(mainInfo);
        return ResponseEntity.ok(new ReferenceResponse(created.getId()));
    }

    @DeleteMapping("delete/{reportId}")
    ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        reportDeletionService.deleteReport(reportId);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping(value = "importExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ImportExcelResponse> importExcel(@RequestParam Long reportId,
                                                    @RequestParam(required = false, defaultValue = "partial") String mode,
                                                    @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(excelImportService.importExcel(reportId, files, mode));
    }
    @GetMapping("getReportUnit1")
    ResponseEntity<Unit1Response> getReportUnit1(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit1Service.sumAllByOrganizationName(organizationName));
    }
    @GetMapping("getReportUnit3")
    ResponseEntity<Unit3And5Response> getReportUnit3(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit3Service.sumAllByOrganizationName(organizationName));
    }

    @GetMapping("getReportUnit4")
    ResponseEntity<Unit4Response> getReportUnit4(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit4Service.sumAllByOrganizationName(organizationName));
    }
    @GetMapping("getReportUnit5")
    ResponseEntity<Unit3And5Response> getReportUnit5(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit5Service.sumAllByOrganizationName(organizationName));
    }
    @GetMapping("getReportUnit6")
    ResponseEntity<Unit6Response> getReportUnit6(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit6Service.sumAllByOrganizationName(organizationName));
    }
    @GetMapping("getReportUnit7")
    ResponseEntity<Unit7Response> getReportUnit7(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit7Service.sumAllByOrganizationName(organizationName));
    }
    @GetMapping("getReportUnit8")
    ResponseEntity<Unit8Response> getReportUnit8(@RequestParam String organizationName) {
        return ResponseEntity.ok(unit8Service.sumAllByOrganizationName(organizationName));
    }

    @GetMapping("/export/unit3/{organizationName}")
    public ResponseEntity<ByteArrayResource> exportUnit3Excel(@PathVariable String organizationName) {
        Unit3And5Response summary =  unit3Service.sumAllByOrganizationName(organizationName);
        ByteArrayInputStream in = excelService.exportUnit345SummaryToExcel(summary, 16,6, HeaderType.UNIT3);

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=unit3-report-" + organizationName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/export/unit4/{organizationName}")
    public ResponseEntity<ByteArrayResource> exportUnit4Excel(@PathVariable String organizationName) {
        Unit4Response summary =  unit4Service.sumAllByOrganizationName(organizationName);
        ByteArrayInputStream in = excelService.exportUnit345SummaryToExcel(summary, 17,14, HeaderType.UNIT4);

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=unit4-report-" + organizationName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/export/unit5/{organizationName}")
    public ResponseEntity<ByteArrayResource> exportUnit5Excel(@PathVariable String organizationName) {
        Unit3And5Response summary =  unit5Service.sumAllByOrganizationName(organizationName);
        ByteArrayInputStream in = excelService.exportUnit345SummaryToExcel(summary, 5,23, HeaderType.UNIT5);

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=unit5-report-" + organizationName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/export/unit6/{organizationName}")
    public ResponseEntity<ByteArrayResource> exportUnit6Excel(@PathVariable String organizationName) {
        Unit6Response summary =  unit6Service.sumAllByOrganizationName(organizationName);
        ByteArrayInputStream in = excelService.exportUnit6SummaryToExcel(summary);

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=unit6-report-" + organizationName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
    @GetMapping("/export/unit7/{organizationName}")
    public ResponseEntity<ByteArrayResource> exportUnit7Excel(@PathVariable String organizationName) {
        Unit7Response summary =  unit7Service.sumAllByOrganizationName(organizationName);
        ByteArrayInputStream in = excelService.exportUnit78SummaryToExcel(summary,34);

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=unit7-report-" + organizationName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }
    @GetMapping("/export/unit8/{organizationName}")
    public ResponseEntity<ByteArrayResource> exportUnit8Excel(@PathVariable String organizationName) {
        Unit8Response summary =  unit8Service.sumAllByOrganizationName(organizationName);
        ByteArrayInputStream in = excelService.exportUnit78SummaryToExcel(summary,44);

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=unit8-report-" + organizationName + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/export/unit/{unit}/district/{district}")
    public ResponseEntity<ByteArrayResource> exportUnitByDistrict(@PathVariable int unit, @PathVariable String district) {
        List<String> orgs = userService.findByDistrict(district).stream()
                .map(User::getEducationalInstitution)
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.toSet())
                .stream()
                .toList();

        if (orgs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayInputStream in;
        String filename;
        switch (unit) {
            case 3 -> {
                Unit3And5Response summary = unit3Service.sumAllByOrganizations(orgs);
                in = excelService.exportUnit345SummaryToExcel(summary, 16, 6, HeaderType.UNIT3);
                filename = "unit3-report-" + district + ".xlsx";
            }
            case 4 -> {
                Unit4Response summary = unit4Service.sumAllByOrganizations(orgs);
                in = excelService.exportUnit345SummaryToExcel(summary, 17, 14, HeaderType.UNIT4);
                filename = "unit4-report-" + district + ".xlsx";
            }
            case 5 -> {
                Unit3And5Response summary = unit5Service.sumAllByOrganizations(orgs);
                in = excelService.exportUnit345SummaryToExcel(summary, 5, 23, HeaderType.UNIT5);
                filename = "unit5-report-" + district + ".xlsx";
            }
            case 6 -> {
                Unit6Response summary = unit6Service.sumAllByOrganizations(orgs);
                in = excelService.exportUnit6SummaryToExcel(summary);
                filename = "unit6-report-" + district + ".xlsx";
            }
            case 7 -> {
                Unit7Response summary = unit7Service.sumAllByOrganizations(orgs);
                in = excelService.exportUnit78SummaryToExcel(summary, 34);
                filename = "unit7-report-" + district + ".xlsx";
            }
            case 8 -> {
                Unit8Response summary = unit8Service.sumAllByOrganizations(orgs);
                in = excelService.exportUnit78SummaryToExcel(summary, 44);
                filename = "unit8-report-" + district + ".xlsx";
            }
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }

        ByteArrayResource resource = new ByteArrayResource(in.readAllBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }



    @GetMapping("getMainInfoList")
    ResponseEntity<Page<MainInfo>> getMainInfo(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<MainInfo> result = mainInfoService.findAllProjected(pageable);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("getContactInfo")
    ResponseEntity<ContactInformation> getContactInfoById(Long id) {
        return ResponseEntity.ok(contactInfoService.findById(id));
    }

    @GetMapping("getMainInfo")
    ResponseEntity<MainInfo> getMainInfoById(Long id) {
        return ResponseEntity.ok(mainInfoService.findById(id));
    }

    @GetMapping("getUnit1")
    ResponseEntity<Unit1> getUnit1ById(Long id) {
        return ResponseEntity.ok(unit1Service.findById(id));
    }

    @GetMapping("getUnit2")
    ResponseEntity<Unit2> getUnit2ById(Long id) {
        return ResponseEntity.ok(unit2Service.findById(id));
    }

    @GetMapping("getUnit3")
    ResponseEntity<Unit3> getUnit3ById(Long id) {
        return ResponseEntity.ok(unit3Service.findById(id));
    }

    @GetMapping("getUnit4")
    ResponseEntity<Unit4> getUnit4ById(Long id) {
        return ResponseEntity.ok(unit4Service.findById(id));
    }

    @GetMapping("getUnit5")
    ResponseEntity<Unit5> getUnit5ById(Long id) {
        return ResponseEntity.ok(unit5Service.findById(id));
    }

    @GetMapping("getUnit6")
    ResponseEntity<Unit6> getUnit6ById(Long id) {
        return ResponseEntity.ok(unit6Service.findById(id));
    }

    @GetMapping("getUnit7")
    ResponseEntity<Unit7> getUnit7ById(Long id) {
        return ResponseEntity.ok(unit7Service.findById(id));
    }

    @GetMapping("getUnit8")
    ResponseEntity<Unit8> getUnit8ById(Long id) {
        return ResponseEntity.ok(unit8Service.findById(id));
    }

    @GetMapping("getUnit9")
    ResponseEntity<Unit9> getUnit9ById(Long id) {
        return ResponseEntity.ok(unit9Service.findById(id));
    }

    @GetMapping("getUnit10")
    ResponseEntity<Unit10> getUnit10ById(Long id) {
        return ResponseEntity.ok(unit10Service.findById(id));
    }

    @GetMapping("getUnit11")
    ResponseEntity<Unit11> getUnit11ById(Long id) {
        return ResponseEntity.ok(unit11Service.findById(id));
    }

    @GetMapping("getUnit12")
    ResponseEntity<Unit12> getUnit12ById(Long id) {
        return ResponseEntity.ok(unit12Service.findById(id));
    }

    @GetMapping("getUnit13")
    ResponseEntity<Unit13> getUnit13ById(Long id) {
        return ResponseEntity.ok(unit13Service.findById(id));
    }

    @GetMapping("getUnit14")
    ResponseEntity<Unit14> getUnit14ById(Long id) {
        return ResponseEntity.ok(unit14Service.findById(id));
    }

    @GetMapping("getUnit15")
    ResponseEntity<Unit15> getUnit15ById(Long id) {
        return ResponseEntity.ok(unit15Service.findById(id));
    }

    @GetMapping("getUnit16")
    ResponseEntity<Unit16> getUnit16ById(Long id) {
        return ResponseEntity.ok(unit16Service.findById(id));
    }

    @GetMapping("getUnit17")
    ResponseEntity<Unit17> getUnit17ById(Long id) {
        return ResponseEntity.ok(unit17Service.findById(id));
    }

    @GetMapping("getUnit18")
    ResponseEntity<Unit18> getUnit18ById(Long id) {
        return ResponseEntity.ok(unit18Service.findById(id));
    }


    @PutMapping("updateMainInfo")
    ResponseEntity<String> updateMainInfo(@RequestBody MainInfo mainInfo) {
        mainInfoService.save(mainInfo);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody UpdateReportStatusRequest request) {
        MainInfo info = mainInfoService.findById(request.getId());
        info.setStatus(request.getStatus());
        info.setRejectionReason(request.getRejectionReason());
        mainInfoService.save(info);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateContactInfo")
    ResponseEntity<String> updateContactInfo(@RequestBody ContactInformation contactInformation) {
        contactInfoService.save(contactInformation);
        return ResponseEntity.ok("Updated");
    }
    @PutMapping("updateUnit1")
    ResponseEntity<String> updateUnit1(@RequestBody Unit1 unit1) {
        unit1Service.save(unit1);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit2")
    ResponseEntity<String> updateUnit2(@RequestBody Unit2 unit2) {

        unit2Service.save(unit2);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit3")
    ResponseEntity<String> updateUnit3(@RequestBody Unit3 unit3) {

        unit3Service.save(unit3);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit4")
    ResponseEntity<String> updateUnit4(@RequestBody Unit4 unit4) {
        unit4Service.save(unit4);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit5")
    ResponseEntity<String> updateUnit5(@RequestBody Unit5 unit5) {

        unit5Service.save(unit5);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit6")
    ResponseEntity<String> updateUnit6(@RequestBody Unit6 unit6) {

        unit6Service.save(unit6);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit7")
    ResponseEntity<String> updateUnit7(@RequestBody Unit7 unit7) {
        unit7Service.save(unit7);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit8")
    ResponseEntity<String> updateUnit8(@RequestBody Unit8 unit8) {

        unit8Service.save(unit8);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit9")
    ResponseEntity<String> updateUnit9(@RequestBody Unit9 unit9) {

        unit9Service.save(unit9);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit10")
    ResponseEntity<String> updateUnit10(@RequestBody Unit10 unit10) {
        unit10Service.save(unit10);
        return ResponseEntity.ok("Updated");
    }
    @PutMapping("updateUnit11")
    ResponseEntity<String> updateUnit11(@RequestBody Unit11 unit11) {
        unit11Service.save(unit11);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit12")
    ResponseEntity<String> updateUnit12(@RequestBody Unit12 unit12) {

        unit12Service.save(unit12);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit13")
    ResponseEntity<String> updateUnit13(@RequestBody Unit13 unit13) {

        unit13Service.save(unit13);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit14")
    ResponseEntity<String> updateUnit14(@RequestBody Unit14 unit14) {
        unit14Service.save(unit14);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit15")
    ResponseEntity<String> updateUnit15(@RequestBody Unit15 unit15) {

        unit15Service.save(unit15);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit16")
    ResponseEntity<String> updateUnit16(@RequestBody Unit16 unit16) {

        unit16Service.save(unit16);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit17")
    ResponseEntity<String> updateUnit17(@RequestBody Unit17 unit17) {
        unit17Service.save(unit17);
        return ResponseEntity.ok("Updated");
    }

    @PutMapping("updateUnit18")
    ResponseEntity<String> updateUnit18(@RequestBody Unit18 unit18) {

        unit18Service.save(unit18);
        return ResponseEntity.ok("Updated");
    }




}
