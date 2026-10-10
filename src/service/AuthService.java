package service;

import dao.UserDAO;
import exceptions.AuthenticationException;
import model.User;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws AuthenticationException {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new AuthenticationException("Email and password are required.");
        }
        User user = userDAO.getUserByEmail(email.trim());
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid email or password.");
        }
        return user;
    }
}
