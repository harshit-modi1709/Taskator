package dao;

import model.TimeLog;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimeLogDAO {

    public boolean startTimeLog(TimeLog log) {
        String sql = "INSERT INTO time_logs (task_id,user_id,start_time,duration_minutes) VALUES (?,?,?,0)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, log.getTaskId());
            ps.setInt(2, log.getUserId());
            ps.setTimestamp(3, Timestamp.valueOf(log.getStartTime()));
            boolean ok = ps.executeUpdate() > 0;
            if (ok) try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) log.setLogId(keys.getInt(1));
            }
            return ok;
        } catch (SQLException e) {
            System.out.println("Error starting time log: " + e.getMessage());
            return false;
        }
    }

    public boolean stopTimeLog(TimeLog log) {
        String sql = "UPDATE time_logs SET end_time=?, duration_minutes=? WHERE log_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(log.getEndTime()));
            ps.setInt(2, log.getDurationMinutes());
            ps.setInt(3, log.getLogId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error stopping time log: " + e.getMessage());
            return false;
        }
    }

    public TimeLog getActiveLog(int taskId, int userId) {
        String sql = "SELECT * FROM time_logs WHERE task_id=? AND user_id=? AND end_time IS NULL ORDER BY start_time DESC LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            System.out.println("Error finding active log: " + e.getMessage());
            return null;
        }
    }

    public List<TimeLog> getLogsByTask(int taskId) {
        return getLogs("SELECT * FROM time_logs WHERE task_id=? ORDER BY start_time DESC", taskId);
    }

    public List<TimeLog> getLogsByUser(int userId) {
        return getLogs("SELECT * FROM time_logs WHERE user_id=? ORDER BY start_time DESC", userId);
    }

    private List<TimeLog> getLogs(String sql, int id) {
        List<TimeLog> logs = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) logs.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting time logs: " + e.getMessage());
        }
        return logs;
    }

    public boolean deleteTimeLog(int logId) {
        String sql = "DELETE FROM time_logs WHERE log_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, logId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting time log: " + e.getMessage());
            return false;
        }
    }

    private TimeLog map(ResultSet rs) throws SQLException {
        Timestamp end = rs.getTimestamp("end_time");
        return new TimeLog(
            rs.getInt("log_id"),
            rs.getInt("task_id"),
            rs.getInt("user_id"),
            rs.getTimestamp("start_time").toLocalDateTime(),
            end == null ? null : end.toLocalDateTime(),
            rs.getInt("duration_minutes")
        );
    }
}
