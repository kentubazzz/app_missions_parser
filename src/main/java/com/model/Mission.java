package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mission {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;
    private List<Sorcerer> sorcerers;
    private List<Technique> techniques;
    private EconomicAssessment economicAssessment;
    private Map<String, Object> extensions;

    // Конструктор по умолчанию
    public Mission() {
        this.sorcerers = new ArrayList<>();
        this.techniques = new ArrayList<>();
        this.extensions = new HashMap<>();
    }

    // Конструктор с основными полями
    public Mission(String missionId, String date, String location, String outcome) {
        this();
        this.missionId = missionId;
        this.date = date;
        this.location = location;
        this.outcome = outcome;
    }

    // Геттеры и сеттеры
    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }

    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }

    public Curse getCurse() { return curse; }
    public void setCurse(Curse curse) { this.curse = curse; }

    public List<Sorcerer> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<Sorcerer> sorcerers) { this.sorcerers = sorcerers; }
    public void addSorcerer(Sorcerer sorcerer) { this.sorcerers.add(sorcerer); }

    public List<Technique> getTechniques() { return techniques; }
    public void setTechniques(List<Technique> techniques) { this.techniques = techniques; }
    public void addTechnique(Technique technique) { this.techniques.add(technique); }

    public EconomicAssessment getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessment economicAssessment) {
        this.economicAssessment = economicAssessment;
    }

    public Map<String, Object> getExtensions() { return extensions; }
    public void setExtensions(Map<String, Object> extensions) { this.extensions = extensions; }
    public void addExtension(String key, Object value) { this.extensions.put(key, value); }



}