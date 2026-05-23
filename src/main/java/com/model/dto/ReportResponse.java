package com.model.dto;

public class ReportResponse {
    private Long missionId;
    private String missionIdentifier;
    private String reportType;
    private String content;
    private long generatedAt;

    public ReportResponse() {}

    public ReportResponse(Long missionId, String missionIdentifier, String reportType, String content) {
        this.missionId = missionId;
        this.missionIdentifier = missionIdentifier;
        this.reportType = reportType;
        this.content = content;
        this.generatedAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public Long getMissionId() { return missionId; }
    public void setMissionId(Long missionId) { this.missionId = missionId; }

    public String getMissionIdentifier() { return missionIdentifier; }
    public void setMissionIdentifier(String missionIdentifier) { this.missionIdentifier = missionIdentifier; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(long generatedAt) { this.generatedAt = generatedAt; }
}
