package akhilanmart.service;

import akhilanmart.dao.UserDAO;
import akhilanmart.model.Role;
import akhilanmart.model.User;
import akhilanmart.util.PasswordUtil;
import akhilanmart.util.ValidationUtil;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String name, String email, String password, String confirmPassword, Role role) throws Exception {
        if (ValidationUtil.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
        if (role == null || role == Role.ADMIN) {
            throw new IllegalArgumentException("Invalid role selected. Public admin registration is strictly prohibited.");
        }

        if (userDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("An account with this email address already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(name.trim(), email.trim().toLowerCase(), hashedPassword, role);
        return userDAO.createUser(newUser);
    }

    public User authenticate(String email, String password) throws Exception {
        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return user;
    }
}
