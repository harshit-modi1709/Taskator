package model;

import interfaces.Prioritizable;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task implements Prioritizable {
    private int taskId, userId, categoryId;
    private String title, description, status;
    private LocalDateTime deadline;
    private int importance, progress;
    private double priorityScore;

    public Task() {}

    public Task(int taskId, int userId, int categoryId, String title, String description,
                LocalDateTime deadline, int importance, double priorityScore,
                String status, int progress) {
        this.taskId = taskId; this.userId = userId; this.categoryId = categoryId;
        this.title = title; this.description = description; this.deadline = deadline;
        this.importance = importance; this.priorityScore = priorityScore;
        this.status = status; this.progress = progress;
    }

    public Task(int userId, int categoryId, String title, String description,
                LocalDateTime deadline, int importance) {
        this(0, userId, categoryId, title, description, deadline, importance, 0, "PENDING", 0);
    }

    public int getTaskId() { return taskId; }
    public void setTaskId(int v) { taskId=v; }
    public int getUserId() { return userId; }
    public void setUserId(int v) { userId=v; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int v) { categoryId=v; }
    public String getTitle() { return title; }
    public void setTitle(String v) { title=v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description=v; }
    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime v) { deadline=v; }
    public int getImportance() { return importance; }
    public void setImportance(int v) { importance=v; }
    public double getPriorityScore() { return priorityScore; }
    public void setPriorityScore(double v) { priorityScore=v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status=v; }
    public int getProgress() { return progress; }
    public void setProgress(int v) { progress=v; }

    @Override
    public double calculatePriorityScore() {
        if (deadline == null) return importance * 10.0;
        long hours = Duration.between(LocalDateTime.now(), deadline).toHours();
        double urgency = hours <= 0 ? 100 : Math.min(100, 100.0 / (hours + 1) * 20);
        return Math.round(((importance / 5.0) * 70 + urgency * 0.30) * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return title + " (" + status + ", " + progress + "%)";
    }
}
