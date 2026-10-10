package model;

import java.time.LocalDateTime;

public class SystemLog {

    private int logId;
    private int userId;
    private String action;
    private String logLevel;
    private LocalDateTime createdAt;

    public SystemLog() {
    }

    public SystemLog(int logId, int userId,
                     String action, String logLevel,
                     LocalDateTime createdAt) {

        this.logId = logId;
        this.userId = userId;
        this.action = action;
        this.logLevel = logLevel;
        this.createdAt = createdAt;
    }

    public SystemLog(int userId, String action, String logLevel) {
        this.userId = userId;
        this.action = action;
        this.logLevel = logLevel;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SystemLog{" +
                "logId=" + logId +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}