package com.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "economic_assessments")
public class EconomicAssessmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_damage_cost")
    private Long totalDamageCost;

    @Column(name = "infrastructure_damage")
    private Long infrastructureDamage;

    @Column(name = "commercial_damage")
    private Long commercialDamage;

    @Column(name = "transport_damage")
    private Long transportDamage;

    @Column(name = "recovery_estimate_days")
    private Integer recoveryEstimateDays;

    @Column(name = "insurance_covered")
    private Boolean insuranceCovered;

    @OneToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private MissionEntity mission;

    public EconomicAssessmentEntity() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public MissionEntity getMission() { return mission; }
    public void setMission(MissionEntity mission) { this.mission = mission; }
}
