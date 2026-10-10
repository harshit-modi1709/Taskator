package service;

import dao.CategoryDAO;
import dao.PriorityRuleDAO;
import dao.SystemLogDAO;
import model.Category;
import model.PriorityRule;
import model.SystemLog;

import java.util.List;

public class AdminService {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final PriorityRuleDAO priorityRuleDAO = new PriorityRuleDAO();
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();

    public boolean addCategory(Category c) {
        return c != null && c.getCategoryName() != null && !c.getCategoryName().isBlank() && categoryDAO.addCategory(c);
    }
    public List<Category> getCategories() { return categoryDAO.getAllCategories(); }
    public boolean updateCategory(Category c) { return categoryDAO.updateCategory(c); }
    public boolean deleteCategory(int id) { return categoryDAO.deleteCategory(id); }

    public boolean addPriorityRule(PriorityRule r) {
        if (r == null || r.getRuleName() == null || r.getRuleName().isBlank()) return false;
        if (r.getDeadlineWeight() < 0 || r.getImportanceWeight() < 0 ||
            r.getDeadlineWeight() + r.getImportanceWeight() != 100) return false;
        return priorityRuleDAO.addRule(r);
    }
    public List<PriorityRule> getPriorityRules() { return priorityRuleDAO.getAllRules(); }
    public boolean updatePriorityRule(PriorityRule r) { return priorityRuleDAO.updateRule(r); }
    public boolean deletePriorityRule(int id) { return priorityRuleDAO.deleteRule(id); }

    public List<SystemLog> getSystemLogs() { return systemLogDAO.getAllLogs(); }
    public void recordAction(int userId, String action, String level) {
        systemLogDAO.addLog(new SystemLog(userId, action, level));
    }
}
