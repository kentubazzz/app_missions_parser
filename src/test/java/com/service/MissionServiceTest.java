package com.service;

import com.exception.InvalidMissionFormatException;
import com.exception.MissionNotFoundException;
import com.factory.ParserFactory;
import com.mapper.MissionMapper;
import com.model.Mission;
import com.model.dto.MissionResponse;
import com.model.entity.*;
import com.parser.MissionParser;
import com.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private CurseRepository curseRepository;

    @Mock
    private SorcererRepository sorcererRepository;

    @Mock
    private TechniqueRepository techniqueRepository;

    @Mock
    private EconomicAssessmentRepository economicAssessmentRepository;

    @Mock
    private MissionMapper missionMapper;

    @InjectMocks
    private MissionService missionService;

    private MultipartFile validJsonFile;
    private MultipartFile invalidFile;

    @BeforeEach
    void setUp() {
        String jsonContent = """
                {
                  "missionId": "M-TEST-001",
                  "date": "2024-12-01",
                  "location": "Токио",
                  "outcome": "SUCCESS",
                  "damageCost": 1000000,
                  "curse": {
                    "name": "Тестовое проклятие",
                    "threatLevel": "HIGH"
                  },
                  "sorcerers": [
                    {"name": "Тестовый маг", "rank": "GRADE_1"}
                  ],
                  "techniques": [
                    {"name": "Тестовая техника", "type": "INNATE", "owner": "Тестовый маг", "damage": 500000}
                  ]
                }
                """;

        validJsonFile = new MockMultipartFile(
                "file",
                "mission.json",
                "application/json",
                jsonContent.getBytes()
        );

        invalidFile = new MockMultipartFile(
                "file",
                "invalid.txt",
                "text/plain",
                "Это невалидный файл".getBytes()
        );
    }


    @Test
    void testSaveMission_DuplicateId_ThrowsException() throws Exception {
        // Given
        when(missionRepository.existsByMissionId("M-TEST-001")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> missionService.saveMission(validJsonFile))
                .isInstanceOf(InvalidMissionFormatException.class)
                .hasMessageContaining("уже существует");
    }

    @Test
    void testGetMissionById_ExistingId_ReturnsMission() throws Exception {
        // Given
        MissionEntity entity = new MissionEntity();
        entity.setId(1L);
        entity.setMissionId("M-001");

        MissionResponse response = new MissionResponse();
        response.setId(1L);
        response.setMissionId("M-001");

        when(missionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(missionMapper.toResponse(entity)).thenReturn(response);

        // When
        MissionResponse result = missionService.getMissionById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMissionId()).isEqualTo("M-001");
    }

    @Test
    void testGetMissionById_NonExistingId_ThrowsException() {
        // Given
        when(missionRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> missionService.getMissionById(999L))
                .isInstanceOf(MissionNotFoundException.class)
                .hasMessageContaining("Миссия не найдена");
    }

    @Test
    void testGetMissionByMissionId_ExistingId_ReturnsMission() throws Exception {
        // Given
        MissionEntity entity = new MissionEntity();
        entity.setId(1L);
        entity.setMissionId("M-001");

        MissionResponse response = new MissionResponse();
        response.setId(1L);
        response.setMissionId("M-001");

        when(missionRepository.findByMissionId("M-001")).thenReturn(Optional.of(entity));
        when(missionMapper.toResponse(entity)).thenReturn(response);

        // When
        MissionResponse result = missionService.getMissionByMissionId("M-001");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMissionId()).isEqualTo("M-001");
    }

    @Test
    void testGetAllMissions_ReturnsList() {
        // Given
        when(missionRepository.findAll()).thenReturn(java.util.Collections.emptyList());

        // When
        var result = missionService.getAllMissions();

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void testDeleteMission_ExistingId_Success() throws Exception {
        // Given
        when(missionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(missionRepository).deleteById(1L);

        // When & Then
        missionService.deleteMission(1L);
        verify(missionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMission_NonExistingId_ThrowsException() {
        // Given
        when(missionRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> missionService.deleteMission(999L))
                .isInstanceOf(MissionNotFoundException.class);
    }
}