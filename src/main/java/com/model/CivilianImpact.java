package com.model;

public class CivilianImpact {
    private Long evacuated;           // Эвакуировано
    private Long injured;              // Пострадавшие
    private Long missing;              // Пропавшие
    private String publicExposureRisk; // Риск раскрытия (LOW/MEDIUM/HIGH/CRITICAL)

    public CivilianImpact() {}

    // Геттеры и сеттеры
    public Long getEvacuated() { return evacuated; }
    public void setEvacuated(Long evacuated) { this.evacuated = evacuated; }

    public Long getInjured() { return injured; }
    public void setInjured(Long injured) { this.injured = injured; }

    public Long getMissing() { return missing; }
    public void setMissing(Long missing) { this.missing = missing; }

    public String getPublicExposureRisk() { return publicExposureRisk; }
    public void setPublicExposureRisk(String publicExposureRisk) { this.publicExposureRisk = publicExposureRisk; }

    @Override
    public String toString() {
        return String.format("Эвакуировано: %d, Пострадало: %d, Пропало: %d, Риск: %s",
                evacuated != null ? evacuated : 0,
                injured != null ? injured : 0,
                missing != null ? missing : 0,
                publicExposureRisk != null ? publicExposureRisk : "не указан");
    }
}
