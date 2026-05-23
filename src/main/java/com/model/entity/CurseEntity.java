package com.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "curses")
public class CurseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "threat_level", nullable = false, length = 50)
    private String threatLevel;

    @OneToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private MissionEntity mission;

    public CurseEntity() {}

    public CurseEntity(String name, String threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getThreatLevel() { return threatLevel; }
    public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }

    public MissionEntity getMission() { return mission; }
    public void setMission(MissionEntity mission) { this.mission = mission; }
}
