package akhilanmart.controller;

import akhilanmart.model.CartItem;
import akhilanmart.model.Order;
import akhilanmart.model.User;
import akhilanmart.service.CartService;
import akhilanmart.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<CartItem> cartItems = cartService.getCart(user.getId());
            if (cartItems.isEmpty()) {
                session.setAttribute("flashError", "Your shopping cart is empty.");
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            BigDecimal cartTotal = cartService.calculateCartTotal(cartItems);

            req.setAttribute("cartItems", cartItems);
            req.setAttribute("cartTotal", cartTotal);
            req.setAttribute("user", user);

            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error loading checkout page: " + e.getMessage());
            req.getRequestDispatcher("/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String address = req.getParameter("address");
        String phone = req.getParameter("phone");

        if (address == null || address.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            session.setAttribute("flashError", "Please provide shipping address and contact phone number.");
            resp.sendRedirect(req.getContextPath() + "/checkout");
            return;
        }

        try {
            Order order = orderService.placeOrder(user.getId());
            session.setAttribute("lastCreatedOrder", order);
            resp.sendRedirect(req.getContextPath() + "/order-success?orderId=" + order.getId());
        } catch (Exception e) {
            session.setAttribute("flashError", "Order Placement Failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/checkout");
        }
    }
}
