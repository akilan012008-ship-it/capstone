package akhilanmart.controller;

import akhilanmart.model.Order;
import akhilanmart.model.User;
import akhilanmart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/orders", "/order-success"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();

        try {
            if ("/order-success".equals(path)) {
                String idStr = req.getParameter("orderId");
                if (idStr != null) {
                    Long orderId = Long.parseLong(idStr);
                    Order order = orderService.getOrderDetails(orderId, user.getId(), false);
                    req.setAttribute("order", order);
                } else {
                    Order order = (Order) session.getAttribute("lastCreatedOrder");
                    req.setAttribute("order", order);
                }
                req.getRequestDispatcher("/order-success.jsp").forward(req, resp);
            } else {
                List<Order> orders = orderService.getBuyerOrders(user.getId());
                req.setAttribute("orders", orders);
                req.getRequestDispatcher("/orders.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading order information: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }
}
