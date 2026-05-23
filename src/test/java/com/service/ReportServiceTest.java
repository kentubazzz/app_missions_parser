package com.service;

import com.mapper.MissionMapper;
import com.model.Mission;
import com.model.dto.ReportResponse;
import com.model.entity.*;
import com.repository.MissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionMapper missionMapper;

    @InjectMocks
    private ReportService reportService;

    private MissionEntity missionEntity;

    @BeforeEach
    void setUp() {
        missionEntity = new MissionEntity();
        missionEntity.setId(1L);
        missionEntity.setMissionId("M-001");
        missionEntity.setDate(java.time.LocalDate.of(2024, 12, 1));
        missionEntity.setLocation("Токио");
        missionEntity.setOutcome("SUCCESS");
        missionEntity.setDamageCost(1000000L);
    }

    @Test
    void testGenerateReport_SimpleType_Success() {
        // Given
        when(missionRepository.findById(1L)).thenReturn(Optional.of(missionEntity));

        // When
        ReportResponse response = reportService.generateReport(1L, "SIMPLE");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMissionId()).isEqualTo(1L);
        assertThat(response.getMissionIdentifier()).isEqualTo("M-001");
        assertThat(response.getReportType()).isEqualTo("SIMPLE");
        assertThat(response.getContent()).isNotNull();
    }

    @Test
    void testGenerateReport_RiskType_Success() {
        // Given
        when(missionRepository.findById(1L)).thenReturn(Optional.of(missionEntity));

        // When
        ReportResponse response = reportService.generateReport(1L, "RISK");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getReportType()).isEqualTo("RISK");
        assertThat(response.getContent()).contains("риск");
    }

    @Test
    void testGenerateReport_MissionNotFound_ThrowsException() {
        // Given
        when(missionRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reportService.generateReport(999L, "SIMPLE"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Миссия не найдена");
    }
}