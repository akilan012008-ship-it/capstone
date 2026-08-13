package akhilanmart.service;

import akhilanmart.dao.UserDAO;
import akhilanmart.model.Role;
import akhilanmart.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public List<User> getUsersByRole(Role role) throws SQLException {
        if (role == null) {
            return getAllUsers();
        }
        return userDAO.findByRole(role);
    }

    public User getUserById(Long id) throws SQLException {
        return userDAO.findById(id);
    }

    public boolean deleteUser(Long id) throws SQLException {
        return userDAO.deleteUser(id);
    }
}
