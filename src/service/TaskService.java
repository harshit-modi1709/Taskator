package service;

import dao.TaskDAO;
import exceptions.ValidationException;
import model.Task;

import java.util.*;
import java.time.LocalDateTime;

public class TaskService {
    private final TaskDAO taskDAO = new TaskDAO();

    public boolean addTask(Task task) throws ValidationException {
        validate(task);
        task.setPriorityScore(task.calculatePriorityScore());
        return taskDAO.addTaskTransactionally(task, "Created task: " + task.getTitle());
    }

    public List<Task> getUserTasks(int userId) {
        return taskDAO.getTasksByUser(userId);
    }

    /**
     * Seeds a small, realistic student workload for the demo user without
     * changing the database schema. Existing tasks with the same titles are
     * left untouched, so the method is safe to call whenever the dashboard opens.
     */
    public void ensureDemoTasks(int userId) {
        List<Task> existing = taskDAO.getTasksByUser(userId);
        Set<String> titles = new HashSet<>();
        for (Task task : existing) titles.add(task.getTitle());

        LocalDateTime base = LocalDateTime.now().withSecond(0).withNano(0);
        addDemoIfMissing(userId, 1, "COA Unit Test Preparation",
                "Revise RTL, bus system, registers and control unit MCQs.",
                base.plusDays(1).withHour(18).withMinute(0), 5, 40, "IN_PROGRESS", titles);
        addDemoIfMissing(userId, 1, "Java Codekata Practice",
                "Complete Java programming problems and revise OOP concepts.",
                base.plusDays(2).withHour(20).withMinute(0), 5, 35, "IN_PROGRESS", titles);
        addDemoIfMissing(userId, 1, "SQLkata Practice Set",
                "Practice SELECT, JOIN, GROUP BY, subqueries and aggregate queries.",
                base.plusDays(3).withHour(19).withMinute(0), 4, 0, "PENDING", titles);
        addDemoIfMissing(userId, 2, "Oracle DBMS Revision",
                "Revise database fundamentals, SQL queries and Oracle commands.",
                base.plusDays(4).withHour(18).withMinute(30), 4, 30, "IN_PROGRESS", titles);
        addDemoIfMissing(userId, 1, "Scilab Practical File",
                "Complete practical programs, screenshots and final file formatting.",
                base.plusDays(5).withHour(21).withMinute(0), 3, 40, "IN_PROGRESS", titles);
        addDemoIfMissing(userId, 4, "DS Trees & Graphs Practice",
                "Practice tree traversal, graphs, BFS, DFS and important questions.",
                base.plusDays(6).withHour(20).withMinute(0), 5, 25, "IN_PROGRESS", titles);
    }

    private void addDemoIfMissing(int userId, int categoryId, String title, String description,
                                   LocalDateTime deadline, int importance, int progress,
                                   String status, Set<String> existingTitles) {
        if (existingTitles.contains(title)) return;

        Task task = new Task(userId, categoryId, title, description, deadline, importance);
        task.setProgress(progress);
        task.setStatus(status);
        task.setPriorityScore(task.calculatePriorityScore());
        if (taskDAO.addTaskTransactionally(task, "Created demo task: " + title)) {
            existingTitles.add(title);
        }
    }

    public boolean updateTask(Task task) throws ValidationException {
        validate(task);
        task.setPriorityScore(task.calculatePriorityScore());
        return taskDAO.updateTask(task);
    }

    public boolean deleteTask(int taskId) {
        return taskDAO.deleteTask(taskId);
    }

    public Map<String, Integer> getStatusSummary(int userId) {
        Map<String, Integer> summary = new LinkedHashMap<>();
        List<Task> tasks = getUserTasks(userId);
        summary.put("TOTAL", tasks.size());
        summary.put("PENDING", (int) tasks.stream().filter(t -> "PENDING".equals(t.getStatus())).count());
        summary.put("IN_PROGRESS", (int) tasks.stream().filter(t -> "IN_PROGRESS".equals(t.getStatus())).count());
        summary.put("COMPLETED", (int) tasks.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count());
        return summary;
    }

    public Set<Integer> getCategoryIds(int userId) {
        Set<Integer> ids = new HashSet<>();
        for (Task task : getUserTasks(userId)) ids.add(task.getCategoryId());
        return ids;
    }

    private void validate(Task task) throws ValidationException {
        if (task == null) throw new ValidationException("Task cannot be null.");
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new ValidationException("Task title is required.");
        }
        if (task.getDeadline() == null) {
            throw new ValidationException("Deadline is required.");
        }
        if (task.getImportance() < 1 || task.getImportance() > 5) {
            throw new ValidationException("Importance must be between 1 and 5.");
        }
    }
}
