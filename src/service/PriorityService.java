package service;

import dao.PriorityRuleDAO;
import dao.TaskDAO;
import model.PriorityRule;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PriorityService {
    private final PriorityRuleDAO ruleDAO = new PriorityRuleDAO();
    private final TaskDAO taskDAO = new TaskDAO();

    public double calculatePriority(Task task, PriorityRule rule) {
        if (task == null || task.getDeadline() == null || rule == null) return 0;
        long hours = Duration.between(LocalDateTime.now(), task.getDeadline()).toHours();
        double deadlineScore = hours <= 0 ? 100 : hours <= 24 ? 90 : hours <= 72 ? 70 : hours <= 168 ? 50 : 30;
        double importanceScore = (task.getImportance() / 5.0) * 100;
        return Math.round(
            (deadlineScore * rule.getDeadlineWeight() / 100.0
            + importanceScore * rule.getImportanceWeight() / 100.0) * 100.0
        ) / 100.0;
    }

    public void updateTaskPriority(Task task, PriorityRule rule) {
        task.setPriorityScore(calculatePriority(task, rule));
        taskDAO.updateTask(task);
    }

    public List<PriorityRule> getAllRules() { return ruleDAO.getAllRules(); }

    public PriorityRule getActiveRule() {
        for (PriorityRule r : ruleDAO.getAllRules()) if (r.isActive()) return r;
        return null;
    }
}
