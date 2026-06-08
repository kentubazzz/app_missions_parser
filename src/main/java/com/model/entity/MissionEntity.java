package com.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "missions")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mission_id", unique = true, nullable = false, length = 50)
    private String missionId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false, length = 20)
    private String outcome;

    @Column(name = "damage_cost")
    private Long damageCost;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CurseEntity curse;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "mission_sorcerers",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "sorcerer_id")
    )
    private List<SorcererEntity> sorcerers = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "mission_techniques",
            joinColumns = @JoinColumn(name = "mission_id"),
            inverseJoinColumns = @JoinColumn(name = "technique_id")
    )
    private List<TechniqueEntity> techniques = new ArrayList<>();

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private EconomicAssessmentEntity economicAssessment;

    public MissionEntity() {}

    public MissionEntity(String missionId, LocalDate date, String location, String outcome, Long damageCost) {
        this.missionId = missionId;
        this.date = date;
        this.location = location;
        this.outcome = outcome;
        this.damageCost = damageCost;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public CurseEntity getCurse() { return curse; }
    public void setCurse(CurseEntity curse) {
        this.curse = curse;
        if (curse != null) curse.setMission(this);
    }

    public List<SorcererEntity> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererEntity> sorcerers) { this.sorcerers = sorcerers; }

    public List<TechniqueEntity> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueEntity> techniques) { this.techniques = techniques; }

    public EconomicAssessmentEntity getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessmentEntity economicAssessment) {
        this.economicAssessment = economicAssessment;
        if (economicAssessment != null) economicAssessment.setMission(this);
    }

    // Helper methods for bidirectional relationships
    public void addSorcerer(SorcererEntity sorcerer) {
        sorcerers.add(sorcerer);
        sorcerer.getMissions().add(this);
    }

    public void removeSorcerer(SorcererEntity sorcerer) {
        sorcerers.remove(sorcerer);
        sorcerer.getMissions().remove(this);
    }

    public void addTechnique(TechniqueEntity technique) {
        techniques.add(technique);
        technique.getMissions().add(this);
    }

    public void removeTechnique(TechniqueEntity technique) {
        techniques.remove(technique);
        technique.getMissions().remove(this);
    }
}