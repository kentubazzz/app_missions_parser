package com.service;

import com.exception.InvalidMissionFormatException;
import com.exception.MissionNotFoundException;
import com.mapper.MissionMapper;
import com.model.*;
import com.model.dto.*;
import com.model.entity.*;
import com.parser.MissionParser;
import com.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private MissionService missionService;

    private MissionEntity missionEntity;
    private MissionResponse missionResponse;
    private Mission mission;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        missionEntity = new MissionEntity();
        missionEntity.setId(1L);
        missionEntity.setMissionId("MISSION-001");
        missionEntity.setDate(LocalDate.now());
        missionEntity.setLocation("Tokyo");
        missionEntity.setOutcome("SUCCESS");
        missionEntity.setDamageCost(100000L);

        missionResponse = new MissionResponse();
        missionResponse.setId(1L);
        missionResponse.setMissionId("MISSION-001");

        mission = new Mission();
        mission.setMissionId("MISSION-001");
        mission.setDate(String.valueOf(LocalDate.now()));
        mission.setLocation("Tokyo");
        mission.setOutcome("SUCCESS");
        mission.setDamageCost(100000L);
    }


    @Test
    void testGetAllMissions_ReturnsList() {
        // Given
        List<MissionEntity> missions = Arrays.asList(missionEntity);
        when(missionRepository.findAllWithBasicData()).thenReturn(missions);
        when(missionRepository.findByIdWithSorcerers(anyLong())).thenReturn(Optional.of(missionEntity));
        when(missionRepository.findByIdWithTechniques(anyLong())).thenReturn(Optional.of(missionEntity));
        when(missionMapper.toResponse(any(MissionEntity.class))).thenReturn(missionResponse);

        // When
        List<MissionResponse> result = missionService.getAllMissions();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(missionRepository).findAllWithBasicData();
    }

    @Test
    void testGetMissionById_ExistingId_Success() throws MissionNotFoundException {
        // Given
        when(missionRepository.findById(1L)).thenReturn(Optional.of(missionEntity));
        when(missionRepository.findByIdWithSorcerers(1L)).thenReturn(Optional.of(missionEntity));
        when(missionRepository.findByIdWithTechniques(1L)).thenReturn(Optional.of(missionEntity));
        when(missionMapper.toResponse(any(MissionEntity.class))).thenReturn(missionResponse);

        // When
        MissionResponse result = missionService.getMissionById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(missionRepository).findById(1L);
    }

    @Test
    void testGetMissionById_NonExistingId_ThrowsException() {
        // Given
        when(missionRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> missionService.getMissionById(99L))
                .isInstanceOf(MissionNotFoundException.class)
                .hasMessageContaining("не найдена");
    }

    @Test
    void testGetMissionByMissionId_Success() throws MissionNotFoundException {
        // Given
        when(missionRepository.findByMissionId("MISSION-001")).thenReturn(Optional.of(missionEntity));
        when(missionRepository.findByMissionIdWithSorcerers("MISSION-001")).thenReturn(Optional.of(missionEntity));
        when(missionRepository.findByMissionIdWithTechniques("MISSION-001")).thenReturn(Optional.of(missionEntity));
        when(missionMapper.toResponse(any(MissionEntity.class))).thenReturn(missionResponse);

        // When
        MissionResponse result = missionService.getMissionByMissionId("MISSION-001");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getMissionId()).isEqualTo("MISSION-001");
    }

    @Test
    void testDeleteMission_ExistingId_Success() throws MissionNotFoundException {
        // Given
        when(missionRepository.findById(1L)).thenReturn(Optional.of(missionEntity));
        doNothing().when(missionRepository).delete(any(MissionEntity.class));

        // When
        missionService.deleteMission(1L);

        // Then
        verify(missionRepository).findById(1L);
        verify(missionRepository).delete(missionEntity);
    }

    @Test
    void testDeleteMission_NonExistingId_ThrowsException() {
        // Given
        when(missionRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> missionService.deleteMission(99L))
                .isInstanceOf(MissionNotFoundException.class)
                .hasMessageContaining("не найдена");

        verify(missionRepository, never()).delete(any());
    }


}