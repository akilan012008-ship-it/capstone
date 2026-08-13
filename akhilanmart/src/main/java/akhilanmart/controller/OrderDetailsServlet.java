package akhilanmart.controller;

import akhilanmart.model.Order;
import akhilanmart.model.Role;
import akhilanmart.model.User;
import akhilanmart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/order-details")
public class OrderDetailsServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(idStr);
            boolean isAdmin = user.getRole() == Role.ADMIN;
            Order order = orderService.getOrderDetails(orderId, user.getId(), isAdmin);

            req.setAttribute("order", order);
            req.getRequestDispatcher("/order-details.jsp").forward(req, resp);
        } catch (IllegalAccessException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading order details: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
