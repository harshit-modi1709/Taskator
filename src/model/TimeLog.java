package model;

import java.time.LocalDateTime;

public class TimeLog {

    private int logId;
    private int taskId;
    private int userId;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int durationMinutes;

    public TimeLog() {
    }

    public TimeLog(int logId, int taskId, int userId,
                   LocalDateTime startTime, LocalDateTime endTime,
                   int durationMinutes) {

        this.logId = logId;
        this.taskId = taskId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
    }

    public TimeLog(int taskId, int userId, LocalDateTime startTime) {
        this.taskId = taskId;
        this.userId = userId;
        this.startTime = startTime;
        this.durationMinutes = 0;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "TimeLog{" +
                "logId=" + logId +
                ", taskId=" + taskId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}