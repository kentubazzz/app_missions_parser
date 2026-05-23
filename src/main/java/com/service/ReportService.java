package com.service;

import com.model.EconomicAssessment;
import com.model.*;
import com.model.dto.MissionResponse;
import com.model.dto.ReportResponse;
import com.model.entity.*;
import com.mapper.MissionMapper;
import com.report.ReportGenerator;
import com.report.RiskReport;
import com.report.TextReport;
import com.repository.MissionRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final MissionRepository missionRepository;
    private final MissionMapper missionMapper;

    public ReportService(MissionRepository missionRepository, MissionMapper missionMapper) {
        this.missionRepository = missionRepository;
        this.missionMapper = missionMapper;
    }

    public ReportResponse generateReport(Long missionId, String reportType) {
        MissionEntity entity = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Миссия не найдена: " + missionId));

        Mission mission = convertToDomain(entity);

        ReportGenerator generator = getReportGenerator(reportType);
        String content = generator.generate(mission);

        return new ReportResponse(
                entity.getId(),
                entity.getMissionId(),
                reportType,
                content
        );
    }

    private Mission convertToDomain(MissionEntity entity) {
        Mission mission = new Mission();
        mission.setMissionId(entity.getMissionId());
        mission.setDate(entity.getDate().toString());
        mission.setLocation(entity.getLocation());
        mission.setOutcome(entity.getOutcome());
        mission.setDamageCost(entity.getDamageCost() != null ? entity.getDamageCost() : 0L);

        if (entity.getCurse() != null) {
            mission.setCurse(new Curse(
                    entity.getCurse().getName(),
                    entity.getCurse().getThreatLevel()
            ));
        }

        for (SorcererEntity s : entity.getSorcerers()) {
            mission.addSorcerer(new Sorcerer(s.getName(), s.getRank()));
        }

        for (TechniqueEntity t : entity.getTechniques()) {
            mission.addTechnique(new Technique(
                    t.getName(), t.getType(), t.getOwner(), t.getDamage() != null ? t.getDamage() : 0L
            ));
        }

        if (entity.getEconomicAssessment() != null) {
            EconomicAssessment ea = new EconomicAssessment();
            ea.setTotalDamageCost(entity.getEconomicAssessment().getTotalDamageCost());
            ea.setInfrastructureDamage(entity.getEconomicAssessment().getInfrastructureDamage());
            ea.setCommercialDamage(entity.getEconomicAssessment().getCommercialDamage());
            ea.setTransportDamage(entity.getEconomicAssessment().getTransportDamage());
            ea.setRecoveryEstimateDays(entity.getEconomicAssessment().getRecoveryEstimateDays());
            ea.setInsuranceCovered(entity.getEconomicAssessment().getInsuranceCovered());
            mission.setEconomicAssessment(ea);
        }

        return mission;
    }

    private ReportGenerator getReportGenerator(String reportType) {
        if (reportType.toUpperCase().equals("RISK")) {
            return new RiskReport();
        }
        return new TextReport();
    }
}
