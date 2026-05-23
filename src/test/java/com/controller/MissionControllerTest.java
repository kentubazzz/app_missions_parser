package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.dto.MissionResponse;
import com.service.MissionService;
import com.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionController.class)
class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MissionService missionService;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUploadMission_Success() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "mission.json",
                "application/json",
                "{\"missionId\":\"M-001\"}".getBytes()
        );

        MissionResponse response = new MissionResponse();
        response.setId(1L);
        response.setMissionId("M-001");

        when(missionService.saveMission(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(multipart("/api/missions/upload")
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.missionId").value("M-001"));
    }

    @Test
    void testGetAllMissions_Success() throws Exception {
        // Given
        MissionResponse mission1 = new MissionResponse();
        mission1.setId(1L);
        mission1.setMissionId("M-001");

        MissionResponse mission2 = new MissionResponse();
        mission2.setId(2L);
        mission2.setMissionId("M-002");

        List<MissionResponse> missions = Arrays.asList(mission1, mission2);

        when(missionService.getAllMissions()).thenReturn(missions);

        // When & Then
        mockMvc.perform(get("/api/missions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].missionId").value("M-001"))
                .andExpect(jsonPath("$[1].missionId").value("M-002"));
    }

    @Test
    void testGetMissionById_Success() throws Exception {
        // Given
        MissionResponse response = new MissionResponse();
        response.setId(1L);
        response.setMissionId("M-001");
        response.setLocation("Токио");

        when(missionService.getMissionById(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/missions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.missionId").value("M-001"))
                .andExpect(jsonPath("$.location").value("Токио"));
    }

    @Test
    void testGetMissionByMissionId_Success() throws Exception {
        // Given
        MissionResponse response = new MissionResponse();
        response.setId(1L);
        response.setMissionId("M-001");

        when(missionService.getMissionByMissionId("M-001")).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/missions/by-mission-id/M-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.missionId").value("M-001"));
    }

    @Test
    void testDeleteMission_Success() throws Exception {
        // Given
        doNothing().when(missionService).deleteMission(1L);

        // When & Then
        mockMvc.perform(delete("/api/missions/1"))
                .andExpect(status().isNoContent());
    }
}