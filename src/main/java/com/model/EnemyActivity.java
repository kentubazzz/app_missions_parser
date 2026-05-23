package com.model;

import java.util.ArrayList;
import java.util.List;

public class EnemyActivity {
    private String behaviorType;      // Тип поведения (AGGRESSIVE/DEFENSIVE/STEALTH/ERRATIC)
    private List<String> targetPriority; // Приоритет целей
    private List<String> attackPatterns; // Паттерны атак
    private String mobility;           // Мобильность (HIGH/MEDIUM/LOW/STATIC)
    private String escalationRisk;     // Риск эскалации (LOW/MEDIUM/HIGH/CRITICAL)

    public EnemyActivity() {
        this.targetPriority = new ArrayList<>();
        this.attackPatterns = new ArrayList<>();
    }

    // Геттеры и сеттеры
    public String getBehaviorType() { return behaviorType; }
    public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }

    public List<String> getTargetPriority() { return targetPriority; }
    public void setTargetPriority(List<String> targetPriority) { this.targetPriority = targetPriority; }
    public void addTargetPriority(String priority) { this.targetPriority.add(priority); }

    public List<String> getAttackPatterns() { return attackPatterns; }
    public void setAttackPatterns(List<String> attackPatterns) { this.attackPatterns = attackPatterns; }
    public void addAttackPattern(String pattern) { this.attackPatterns.add(pattern); }

    public String getMobility() { return mobility; }
    public void setMobility(String mobility) { this.mobility = mobility; }

    public String getEscalationRisk() { return escalationRisk; }
    public void setEscalationRisk(String escalationRisk) { this.escalationRisk = escalationRisk; }

    @Override
    public String toString() {
        return String.format("Тип: %s, Мобильность: %s, Риск эскалации: %s",
                behaviorType, mobility, escalationRisk);
    }
}
