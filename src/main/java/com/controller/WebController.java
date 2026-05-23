package com.controller;

import com.exception.MissionNotFoundException;
import com.model.dto.MissionResponse;
import com.model.dto.ReportResponse;
import com.service.MissionService;
import com.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WebController {

    private final MissionService missionService;
    private final ReportService reportService;

    public WebController(MissionService missionService, ReportService reportService) {
        this.missionService = missionService;
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<MissionResponse> missions = missionService.getAllMissions();
        model.addAttribute("missions", missions);

        // Вычисляем статистику в Java
        int total = missions.size();
        int success = 0;
        int failure = 0;

        for (MissionResponse m : missions) {
            if ("SUCCESS".equals(m.getOutcome())) {
                success++;
            } else if ("FAILURE".equals(m.getOutcome())) {
                failure++;
            }
        }

        model.addAttribute("totalMissions", total);
        model.addAttribute("successMissions", success);
        model.addAttribute("failureMissions", failure);

        return "index";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadMission(@RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            MissionResponse response = missionService.saveMission(file);
            redirectAttributes.addFlashAttribute("successMessage",
                    "✅ Миссия успешно загружена! ID: " + response.getId() + ", Mission ID: " + response.getMissionId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Ошибка загрузки: " + e.getMessage());
        }
        return "redirect:/upload";
    }

    @GetMapping("/missions")
    public String missionsList(Model model) {
        List<MissionResponse> missions = missionService.getAllMissions();
        model.addAttribute("missionList", missions);  // ← ИЗМЕНЕНО: используем missionList вместо missions
        return "missions";
    }

    @GetMapping("/mission/{id}")
    public String missionDetail(@PathVariable Long id, Model model) throws MissionNotFoundException {
        MissionResponse mission = missionService.getMissionById(id);
        model.addAttribute("mission", mission);
        return "mission-detail";
    }

    @GetMapping("/mission/{id}/report")
    public String generateReport(@PathVariable Long id,
                                 @RequestParam(defaultValue = "SIMPLE") String type,
                                 Model model) throws MissionNotFoundException {
        MissionResponse mission = missionService.getMissionById(id);
        ReportResponse report = reportService.generateReport(id, type);

        model.addAttribute("mission", mission);
        model.addAttribute("report", report);
        model.addAttribute("reportType", type);

        return "report";
    }

    @PostMapping("/mission/{id}/delete")
    public String deleteMission(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            missionService.deleteMission(id);
            redirectAttributes.addFlashAttribute("successMessage", "✅ Миссия удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Ошибка удаления: " + e.getMessage());
        }
        return "redirect:/missions";
    }
}
