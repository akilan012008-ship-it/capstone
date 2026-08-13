package akhilanmart.controller;

import akhilanmart.model.Role;
import akhilanmart.model.User;
import akhilanmart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/users", "/admin/user/delete"})
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roleFilterStr = req.getParameter("role");
        try {
            Role roleFilter = Role.fromString(roleFilterStr);
            List<User> users = userService.getUsersByRole(roleFilter);

            req.setAttribute("users", users);
            req.setAttribute("selectedRole", roleFilterStr != null ? roleFilterStr : "ALL");
            req.getRequestDispatcher("/admin-users.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading users: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User adminUser = (User) session.getAttribute("user");
        String path = req.getServletPath();

        try {
            if ("/admin/user/delete".equals(path)) {
                Long userId = Long.parseLong(req.getParameter("id"));
                User targetUser = userService.getUserById(userId);

                if (targetUser != null && targetUser.getId().equals(adminUser.getId())) {
                    session.setAttribute("flashError", "Cannot delete your own admin account!");
                } else if (targetUser != null && targetUser.getRole() == Role.ADMIN) {
                    session.setAttribute("flashError", "Cannot delete another administrator account!");
                } else {
                    userService.deleteUser(userId);
                    session.setAttribute("flashSuccess", "User account removed successfully.");
                }
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to delete user: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
