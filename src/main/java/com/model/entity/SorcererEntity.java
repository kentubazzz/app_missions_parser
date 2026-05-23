package com.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sorcerers")
public class SorcererEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String rank;

    @ManyToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private MissionEntity mission;

    public SorcererEntity() {}

    public SorcererEntity(String name, String rank) {
        this.name = name;
        this.rank = rank;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public MissionEntity getMission() { return mission; }
    public void setMission(MissionEntity mission) { this.mission = mission; }
}
