package dao;

import model.SystemLog;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SystemLogDAO {

    // CREATE
    public boolean addLog(SystemLog log) {

        String sql = "INSERT INTO system_logs " +
                     "(user_id, action, log_level) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (log.getUserId() > 0) {
                statement.setInt(1, log.getUserId());
            } else {
                statement.setNull(1, java.sql.Types.INTEGER);
            }

            statement.setString(2, log.getAction());
            statement.setString(3, log.getLogLevel());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding system log: " + e.getMessage());
            return false;
        }
    }

    // READ
    public List<SystemLog> getAllLogs() {

        List<SystemLog> logs = new ArrayList<>();

        String sql = "SELECT * FROM system_logs ORDER BY created_at DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Timestamp timestamp = result.getTimestamp("created_at");

                SystemLog log = new SystemLog(
                        result.getInt("log_id"),
                        result.getInt("user_id"),
                        result.getString("action"),
                        result.getString("log_level"),
                        timestamp != null ? timestamp.toLocalDateTime() : null
                );

                logs.add(log);
            }

        } catch (SQLException e) {
            System.out.println("Error getting system logs: " + e.getMessage());
        }

        return logs;
    }

    // READ - Logs by user
    public List<SystemLog> getLogsByUser(int userId) {

        List<SystemLog> logs = new ArrayList<>();

        String sql = "SELECT * FROM system_logs " +
                     "WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Timestamp timestamp = result.getTimestamp("created_at");

                    SystemLog log = new SystemLog(
                            result.getInt("log_id"),
                            result.getInt("user_id"),
                            result.getString("action"),
                            result.getString("log_level"),
                            timestamp != null ? timestamp.toLocalDateTime() : null
                    );

                    logs.add(log);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error getting user logs: " + e.getMessage());
        }

        return logs;
    }

    // DELETE
    public boolean deleteLog(int logId) {

        String sql = "DELETE FROM system_logs WHERE log_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, logId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting system log: " + e.getMessage());
            return false;
        }
    }
}