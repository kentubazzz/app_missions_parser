package com.model;

public class OperationTimeline {
    private String timestamp;    // Время события (ISO 8601 или строка)
    private String type;         // Тип события (START/ENGAGEMENT/RETREAT/REINFORCEMENT/END)
    private String description;  // Описание события

    public OperationTimeline() {}

    // Геттеры и сеттеры
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", timestamp, type, description);
    }
}