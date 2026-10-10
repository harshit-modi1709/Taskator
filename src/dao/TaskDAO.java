package dao;

import interfaces.Repository;
import model.Task;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements Repository<Task, Integer> {

    @Override
    public boolean save(Task task) { return addTask(task); }

    @Override
    public Task findById(Integer id) { return getTaskById(id); }

    @Override
    public List<Task> findAll() { return getAllTasks(); }

    @Override
    public boolean update(Task task) { return updateTask(task); }

    @Override
    public boolean deleteById(Integer id) { return deleteTask(id); }

    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, category_id, title, description, deadline, importance, priority_score, status, progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            fillTask(ps, task);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding task: " + e.getMessage());
            return false;
        }
    }

    // Transaction: task insertion + system log insertion happen together.
    public boolean addTaskTransactionally(Task task, String action) {
        String taskSql = "INSERT INTO tasks (user_id, category_id, title, description, deadline, importance, priority_score, status, progress) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String logSql = "INSERT INTO system_logs (user_id, action, log_level) VALUES (?, ?, 'INFO')";

        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement taskPs = c.prepareStatement(taskSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement logPs = c.prepareStatement(logSql)) {

                fillTask(taskPs, task);
                if (taskPs.executeUpdate() == 0) throw new SQLException("Task was not inserted.");

                try (ResultSet keys = taskPs.getGeneratedKeys()) {
                    if (keys.next()) task.setTaskId(keys.getInt(1));
                }

                logPs.setInt(1, task.getUserId());
                logPs.setString(2, action);
                logPs.executeUpdate();

                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Transaction failed: " + e.getMessage());
            return false;
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY priority_score DESC, deadline ASC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) tasks.add(map(rs));
        } catch (SQLException e) {
            System.out.println("Error getting tasks: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getTasksByUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id=? ORDER BY priority_score DESC, deadline ASC";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tasks.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting user tasks: " + e.getMessage());
        }
        return tasks;
    }

    public Task getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE task_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            System.out.println("Error getting task: " + e.getMessage());
            return null;
        }
    }

    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET category_id=?, title=?, description=?, deadline=?, importance=?, priority_score=?, status=?, progress=? WHERE task_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, task.getCategoryId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(task.getDeadline()));
            ps.setInt(5, task.getImportance());
            ps.setDouble(6, task.getPriorityScore());
            ps.setString(7, task.getStatus());
            ps.setInt(8, task.getProgress());
            ps.setInt(9, task.getTaskId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE task_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting task: " + e.getMessage());
            return false;
        }
    }

    private void fillTask(PreparedStatement ps, Task task) throws SQLException {
        ps.setInt(1, task.getUserId());
        ps.setInt(2, task.getCategoryId());
        ps.setString(3, task.getTitle());
        ps.setString(4, task.getDescription());
        ps.setTimestamp(5, Timestamp.valueOf(task.getDeadline()));
        ps.setInt(6, task.getImportance());
        ps.setDouble(7, task.getPriorityScore());
        ps.setString(8, task.getStatus());
        ps.setInt(9, task.getProgress());
    }

    private Task map(ResultSet rs) throws SQLException {
        return new Task(
            rs.getInt("task_id"),
            rs.getInt("user_id"),
            rs.getInt("category_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getTimestamp("deadline").toLocalDateTime(),
            rs.getInt("importance"),
            rs.getDouble("priority_score"),
            rs.getString("status"),
            rs.getInt("progress")
        );
    }
}
