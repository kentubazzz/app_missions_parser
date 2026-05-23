package com.model;

public class EnvironmentConditions {
    private String weather;           // Погода (CLEAR/RAIN/STORM/FOG/CURSED_MIST)
    private String timeOfDay;         // Время суток (DAWN/MORNING/NOON/EVENING/NIGHT/MIDNIGHT)
    private String visibility;        // Видимость (EXCELLENT/GOOD/MODERATE/POOR/NONE)
    private Double cursedEnergyDensity; // Плотность проклятой энергии

    public EnvironmentConditions() {}

    // Геттеры и сеттеры
    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public Double getCursedEnergyDensity() { return cursedEnergyDensity; }
    public void setCursedEnergyDensity(Double cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }

    @Override
    public String toString() {
        return String.format("Погода: %s, Время: %s, Видимость: %s, Плотность энергии: %.2f",
                weather, timeOfDay, visibility, cursedEnergyDensity != null ? cursedEnergyDensity : 0);
    }
}
