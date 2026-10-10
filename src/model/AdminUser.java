package model;

public class AdminUser extends User {
    public AdminUser(int userId, String fullName, String email, String password) {
        super(userId, fullName, email, password, "ADMIN");
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
