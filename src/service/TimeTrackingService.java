package service;

import dao.TimeLogDAO;
import model.TimeLog;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TimeTrackingService {
    private final TimeLogDAO dao = new TimeLogDAO();

    public TimeLog startTracking(int taskId, int userId) {
        TimeLog existing = dao.getActiveLog(taskId, userId);
        if (existing != null) return existing;
        TimeLog log = new TimeLog(taskId, userId, LocalDateTime.now());
        return dao.startTimeLog(log) ? log : null;
    }

    public boolean stopTracking(TimeLog log) {
        if (log == null || log.getStartTime() == null || log.getLogId() <= 0) return false;
        LocalDateTime end = LocalDateTime.now();
        log.setEndTime(end);
        log.setDurationMinutes((int) Duration.between(log.getStartTime(), end).toMinutes());
        return dao.stopTimeLog(log);
    }

    public List<TimeLog> getTaskTimeLogs(int taskId) { return dao.getLogsByTask(taskId); }
    public List<TimeLog> getUserTimeLogs(int userId) { return dao.getLogsByUser(userId); }
}
