package dao;

import model.PriorityRule;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriorityRuleDAO {

    // CREATE
    public boolean addRule(PriorityRule rule) {

        String sql = "INSERT INTO priority_rules " +
                     "(rule_name, deadline_weight, importance_weight, is_active) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rule.getRuleName());
            statement.setInt(2, rule.getDeadlineWeight());
            statement.setInt(3, rule.getImportanceWeight());
            statement.setBoolean(4, rule.isActive());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error adding priority rule: " + e.getMessage());
            return false;
        }
    }

    // READ
    public List<PriorityRule> getAllRules() {

        List<PriorityRule> rules = new ArrayList<>();

        String sql = "SELECT * FROM priority_rules";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                PriorityRule rule = new PriorityRule(
                        result.getInt("rule_id"),
                        result.getString("rule_name"),
                        result.getInt("deadline_weight"),
                        result.getInt("importance_weight"),
                        result.getBoolean("is_active")
                );

                rules.add(rule);
            }

        } catch (SQLException e) {
            System.out.println("Error getting priority rules: " + e.getMessage());
        }

        return rules;
    }

    // UPDATE
    public boolean updateRule(PriorityRule rule) {

        String sql = "UPDATE priority_rules SET rule_name = ?, " +
                     "deadline_weight = ?, importance_weight = ?, is_active = ? " +
                     "WHERE rule_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, rule.getRuleName());
            statement.setInt(2, rule.getDeadlineWeight());
            statement.setInt(3, rule.getImportanceWeight());
            statement.setBoolean(4, rule.isActive());
            statement.setInt(5, rule.getRuleId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating priority rule: " + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean deleteRule(int ruleId) {

        String sql = "DELETE FROM priority_rules WHERE rule_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, ruleId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting priority rule: " + e.getMessage());
            return false;
        }
    }
}