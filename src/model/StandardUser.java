package model;

public class StandardUser extends User {
    public StandardUser(int userId, String fullName, String email, String password) {
        super(userId, fullName, email, password, "USER");
    }

    @Override
    public String getRole() {
        return "USER";
    }
}
