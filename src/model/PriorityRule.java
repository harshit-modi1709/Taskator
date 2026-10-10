package model;

public class PriorityRule {

    private int ruleId;
    private String ruleName;
    private int deadlineWeight;
    private int importanceWeight;
    private boolean active;

    public PriorityRule() {
    }

    public PriorityRule(int ruleId, String ruleName,
                        int deadlineWeight, int importanceWeight,
                        boolean active) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.deadlineWeight = deadlineWeight;
        this.importanceWeight = importanceWeight;
        this.active = active;
    }

    public PriorityRule(String ruleName,
                        int deadlineWeight, int importanceWeight,
                        boolean active) {
        this.ruleName = ruleName;
        this.deadlineWeight = deadlineWeight;
        this.importanceWeight = importanceWeight;
        this.active = active;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getDeadlineWeight() {
        return deadlineWeight;
    }

    public void setDeadlineWeight(int deadlineWeight) {
        this.deadlineWeight = deadlineWeight;
    }

    public int getImportanceWeight() {
        return importanceWeight;
    }

    public void setImportanceWeight(int importanceWeight) {
        this.importanceWeight = importanceWeight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "PriorityRule{" +
                "ruleId=" + ruleId +
                ", ruleName='" + ruleName + '\'' +
                ", deadlineWeight=" + deadlineWeight +
                ", importanceWeight=" + importanceWeight +
                ", active=" + active +
                '}';
    }
}