package com.controller;

import com.model.dto.ReportResponse;
import com.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report Generator", description = "API для генерации отчетов по миссиям")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/mission/{missionId}")
    @Operation(summary = "Сгенерировать отчет", description = "Генерирует отчет по сохраненной миссии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет успешно сгенерирован"),
            @ApiResponse(responseCode = "404", description = "Миссия не найдена")
    })
    public ResponseEntity<ReportResponse> generateReport(
            @Parameter(description = "ID миссии в БД")
            @PathVariable Long missionId,
            @Parameter(description = "Тип отчета: SIMPLE, DETAILED, RISK")
            @RequestParam(defaultValue = "SIMPLE") String type) {
        return ResponseEntity.ok(reportService.generateReport(missionId, type));
    }
}
