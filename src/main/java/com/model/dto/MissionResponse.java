package com.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionResponse {
    private Long id;
    private String missionId;
    private LocalDate date;
    private String location;
    private String outcome;
    private Long damageCost;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private CurseResponse curse;
    private List<SorcererResponse> sorcerers = new ArrayList<>();
    private List<TechniqueResponse> techniques = new ArrayList<>();
    private EconomicAssessmentResponse economicAssessment;

    // Конструкторы
    public MissionResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }

    public Long getDamageCost() { return damageCost; }
    public void setDamageCost(Long damageCost) { this.damageCost = damageCost; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public CurseResponse getCurse() { return curse; }
    public void setCurse(CurseResponse curse) { this.curse = curse; }

    public List<SorcererResponse> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererResponse> sorcerers) { this.sorcerers = sorcerers; }

    public List<TechniqueResponse> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueResponse> techniques) { this.techniques = techniques; }

    public EconomicAssessmentResponse getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessmentResponse economicAssessment) { this.economicAssessment = economicAssessment; }

    // Внутренние классы
    public static class CurseResponse {
        private String name;
        private String threatLevel;

        public CurseResponse() {}
        public CurseResponse(String name, String threatLevel) {
            this.name = name;
            this.threatLevel = threatLevel;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getThreatLevel() { return threatLevel; }
        public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }
    }

    public static class SorcererResponse {
        private String name;
        private String rank;

        public SorcererResponse() {}
        public SorcererResponse(String name, String rank) {
            this.name = name;
            this.rank = rank;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRank() { return rank; }
        public void setRank(String rank) { this.rank = rank; }
    }

    public static class TechniqueResponse {
        private String name;
        private String type;
        private String owner;
        private Long damage;

        public TechniqueResponse() {}
        public TechniqueResponse(String name, String type, String owner, Long damage) {
            this.name = name;
            this.type = type;
            this.owner = owner;
            this.damage = damage;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
        public Long getDamage() { return damage; }
        public void setDamage(Long damage) { this.damage = damage; }
    }

    public static class EconomicAssessmentResponse {
        private Long totalDamageCost;
        private Long infrastructureDamage;
        private Long commercialDamage;
        private Long transportDamage;
        private Integer recoveryEstimateDays;
        private Boolean insuranceCovered;

        public EconomicAssessmentResponse() {}

        public Long getTotalDamageCost() { return totalDamageCost; }
        public void setTotalDamageCost(Long totalDamageCost) { this.totalDamageCost = totalDamageCost; }
        public Long getInfrastructureDamage() { return infrastructureDamage; }
        public void setInfrastructureDamage(Long infrastructureDamage) { this.infrastructureDamage = infrastructureDamage; }
        public Long getCommercialDamage() { return commercialDamage; }
        public void setCommercialDamage(Long commercialDamage) { this.commercialDamage = commercialDamage; }
        public Long getTransportDamage() { return transportDamage; }
        public void setTransportDamage(Long transportDamage) { this.transportDamage = transportDamage; }
        public Integer getRecoveryEstimateDays() { return recoveryEstimateDays; }
        public void setRecoveryEstimateDays(Integer recoveryEstimateDays) { this.recoveryEstimateDays = recoveryEstimateDays; }
        public Boolean getInsuranceCovered() { return insuranceCovered; }
        public void setInsuranceCovered(Boolean insuranceCovered) { this.insuranceCovered = insuranceCovered; }
    }
}
