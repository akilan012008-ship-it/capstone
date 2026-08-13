package akhilanmart.controller;

import akhilanmart.model.Role;
import akhilanmart.model.User;
import akhilanmart.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String roleStr = req.getParameter("role");

        try {
            Role role = Role.fromString(roleStr);
            if (role == Role.ADMIN) {
                throw new IllegalArgumentException("Public registration for Administrator role is not permitted.");
            }

            User user = authService.registerUser(name, email, password, confirmPassword, role != null ? role : Role.BUYER);
            
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("flashSuccess", "Registration successful! Welcome to Akhilan Mart, " + user.getName());

            if (user.getRole() == Role.SELLER) {
                resp.sendRedirect(req.getContextPath() + "/seller/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/products");
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("role", roleStr);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
