package com.mapper;

import com.model.Mission;
import com.model.dto.MissionResponse;
import com.model.entity.*;
import com.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Component
public class MissionMapper {

    // Entity → Response
    public MissionResponse toResponse(MissionEntity entity) {
        if (entity == null) return null;

        MissionResponse response = new MissionResponse();
        response.setId(entity.getId());
        response.setMissionId(entity.getMissionId());
        response.setDate(entity.getDate());
        response.setLocation(entity.getLocation());
        response.setOutcome(entity.getOutcome());
        response.setDamageCost(entity.getDamageCost());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCurse() != null) {
            MissionResponse.CurseResponse curseResponse = new MissionResponse.CurseResponse(
                    entity.getCurse().getName(),
                    entity.getCurse().getThreatLevel()
            );
            response.setCurse(curseResponse);
        }

        if (entity.getSorcerers() != null) {
            response.setSorcerers(entity.getSorcerers().stream()
                    .map(s -> new MissionResponse.SorcererResponse(s.getName(), s.getRank()))
                    .collect(Collectors.toList()));
        }

        if (entity.getTechniques() != null) {
            response.setTechniques(entity.getTechniques().stream()
                    .map(t -> new MissionResponse.TechniqueResponse(t.getName(), t.getType(), t.getOwner(), t.getDamage()))
                    .collect(Collectors.toList()));
        }

        if (entity.getEconomicAssessment() != null) {
            MissionResponse.EconomicAssessmentResponse eaResponse = new MissionResponse.EconomicAssessmentResponse();
            eaResponse.setTotalDamageCost(entity.getEconomicAssessment().getTotalDamageCost());
            eaResponse.setInfrastructureDamage(entity.getEconomicAssessment().getInfrastructureDamage());
            eaResponse.setCommercialDamage(entity.getEconomicAssessment().getCommercialDamage());
            eaResponse.setTransportDamage(entity.getEconomicAssessment().getTransportDamage());
            eaResponse.setRecoveryEstimateDays(entity.getEconomicAssessment().getRecoveryEstimateDays());
            eaResponse.setInsuranceCovered(entity.getEconomicAssessment().getInsuranceCovered());
            response.setEconomicAssessment(eaResponse);
        }

        return response;
    }

    // Domain Model → Entity
    public MissionEntity toEntity(Mission mission) {
        if (mission == null) return null;

        MissionEntity entity = new MissionEntity();
        entity.setMissionId(mission.getMissionId());
        entity.setDate(LocalDate.parse(mission.getDate()));
        entity.setLocation(mission.getLocation());
        entity.setOutcome(mission.getOutcome());
        entity.setDamageCost(mission.getDamageCost());

        return entity;
    }

    // Curse Domain → Curse Entity
    public CurseEntity toCurseEntity(Curse curse) {
        if (curse == null) return null;
        return new CurseEntity(curse.getName(), curse.getThreatLevel());
    }

    // Sorcerer Domain → Sorcerer Entity
    public SorcererEntity toSorcererEntity(Sorcerer sorcerer) {
        if (sorcerer == null) return null;
        return new SorcererEntity(sorcerer.getName(), sorcerer.getRank());
    }

    // Technique Domain → Technique Entity
    public TechniqueEntity toTechniqueEntity(Technique technique) {
        if (technique == null) return null;
        return new TechniqueEntity(technique.getName(), technique.getType(),
                technique.getOwner(), technique.getDamage());
    }

    // EconomicAssessment Domain → EconomicAssessment Entity
    public EconomicAssessmentEntity toEconomicAssessmentEntity(EconomicAssessment assessment) {
        if (assessment == null) return null;

        EconomicAssessmentEntity entity = new EconomicAssessmentEntity();
        entity.setTotalDamageCost(assessment.getTotalDamageCost());
        entity.setInfrastructureDamage(assessment.getInfrastructureDamage());
        entity.setCommercialDamage(assessment.getCommercialDamage());
        entity.setTransportDamage(assessment.getTransportDamage());
        entity.setRecoveryEstimateDays(assessment.getRecoveryEstimateDays());
        entity.setInsuranceCovered(assessment.getInsuranceCovered());

        return entity;
    }
}
