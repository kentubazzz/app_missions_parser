package com.controller;

import com.exception.InvalidMissionFormatException;
import com.exception.MissionNotFoundException;
import com.model.dto.MissionResponse;
import com.service.MissionService;
import com.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@Tag(name = "Mission Archive", description = "API для работы с архивом миссий")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService, ReportService reportService) {
        this.missionService = missionService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить миссию", description = "Загружает файл миссии, парсит и сохраняет в БД")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Миссия успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Ошибка формата файла"),
            @ApiResponse(responseCode = "409", description = "Миссия с таким ID уже существует")
    })
    public ResponseEntity<MissionResponse> uploadMission(
            @Parameter(
                    description = "Файл миссии (JSON, XML, TXT, YAML, Event)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary", description = "Файл миссии"))
            )
            @RequestParam("file") MultipartFile file) throws InvalidMissionFormatException {
        MissionResponse response = missionService.saveMission(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Получить все миссии", description = "Возвращает список всех миссий в архиве")
    public ResponseEntity<List<MissionResponse>> getAllMissions() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить миссию по ID", description = "Возвращает детальную информацию о миссии")
    public ResponseEntity<MissionResponse> getMissionById(
            @Parameter(description = "ID миссии в БД")
            @PathVariable Long id) throws MissionNotFoundException {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @GetMapping("/by-mission-id/{missionId}")
    @Operation(summary = "Получить миссию по missionId", description = "Возвращает детальную информацию о миссии по её уникальному идентификатору")
    public ResponseEntity<MissionResponse> getMissionByMissionId(
            @Parameter(description = "Уникальный идентификатор миссии (например, M-2024-017)")
            @PathVariable String missionId) throws MissionNotFoundException {
        return ResponseEntity.ok(missionService.getMissionByMissionId(missionId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить миссию", description = "Удаляет миссию из архива")
    public ResponseEntity<Void> deleteMission(
            @Parameter(description = "ID миссии в БД")
            @PathVariable Long id) throws MissionNotFoundException {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }
}
