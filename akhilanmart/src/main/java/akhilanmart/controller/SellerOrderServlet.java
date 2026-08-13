package akhilanmart.controller;

import akhilanmart.model.OrderItem;
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

@WebServlet(urlPatterns = {"/seller/orders", "/seller/order/update-status"})
public class SellerOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User seller = (User) session.getAttribute("user");

        try {
            List<OrderItem> orderItems = orderService.getSellerOrders(seller.getId());
            req.setAttribute("orderItems", orderItems);
            req.getRequestDispatcher("/seller-orders.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading seller orders: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String orderIdStr = req.getParameter("orderId");
        String status = req.getParameter("status");

        try {
            Long orderId = Long.parseLong(orderIdStr);
            orderService.updateOrderStatus(orderId, status);
            session.setAttribute("flashSuccess", "Order #" + orderId + " status updated to " + status + ".");
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to update status: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/seller/orders");
    }
}
