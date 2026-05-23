package com.controller;

import com.exception.InvalidMissionFormatException;
import com.model.dto.MissionResponse;
import com.service.MissionService;
import com.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission Archive", description = "API для работы с архивом миссий")
public class MissionController {

    private final MissionService missionService;
    private final ReportService reportService;

    public MissionController(MissionService missionService, ReportService reportService) {
        this.missionService = missionService;
        this.reportService = reportService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Загрузить миссию",
            description = "Загружает файл миссии, парсит его и сохраняет данные в базу данных"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Миссия успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Ошибка формата файла"),
            @ApiResponse(responseCode = "409", description = "Миссия с таким ID уже существует")
    })
    public ResponseEntity<MissionResponse> uploadMission(
            @Parameter(
                    description = "Файл миссии в формате JSON, XML, TXT, YAML или Event",
                    required = true
            )
            @RequestPart("file") MultipartFile file
    ) throws InvalidMissionFormatException {

        MissionResponse response = missionService.saveMission(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}