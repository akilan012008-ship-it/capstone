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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            redirectByUserRole(resp, req.getContextPath(), user);
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = authService.authenticate(email, password);
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("flashSuccess", "Welcome back, " + user.getName() + "!");

            String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            if (redirectUrl != null) {
                session.removeAttribute("redirectAfterLogin");
                resp.sendRedirect(redirectUrl);
            } else {
                redirectByUserRole(resp, req.getContextPath(), user);
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void redirectByUserRole(HttpServletResponse resp, String contextPath, User user) throws IOException {
        if (user.getRole() == Role.ADMIN) {
            resp.sendRedirect(contextPath + "/admin/dashboard");
        } else if (user.getRole() == Role.SELLER) {
            resp.sendRedirect(contextPath + "/seller/dashboard");
        } else {
            resp.sendRedirect(contextPath + "/products");
        }
    }
}
