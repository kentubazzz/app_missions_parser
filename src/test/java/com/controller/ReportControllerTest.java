package com.controller;

import com.model.dto.ReportResponse;
import com.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    void testGenerateReport_Success() throws Exception {
        // Given
        ReportResponse response = new ReportResponse(
                1L,
                "M-001",
                "SIMPLE",
                "Это текст отчета"
        );

        when(reportService.generateReport(anyLong(), eq("SIMPLE"))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/reports/mission/1")
                        .param("type", "SIMPLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missionId").value(1L))
                .andExpect(jsonPath("$.missionIdentifier").value("M-001"))
                .andExpect(jsonPath("$.reportType").value("SIMPLE"));
    }

    @Test
    void testGenerateReport_WithRiskType() throws Exception {
        // Given
        ReportResponse response = new ReportResponse(
                1L,
                "M-001",
                "RISK",
                "Анализ рисков"
        );

        when(reportService.generateReport(anyLong(), eq("RISK"))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/reports/mission/1")
                        .param("type", "RISK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportType").value("RISK"));
    }
}